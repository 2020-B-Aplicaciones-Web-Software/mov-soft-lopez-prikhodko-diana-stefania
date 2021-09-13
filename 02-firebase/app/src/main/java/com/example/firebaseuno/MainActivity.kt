package com.example.firebaseuno

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.firebaseuno.dto.FireStoreUsuarioDto
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val CODIGO_INICIO_SESION = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonlogin = findViewById<Button>(R.id.btn_login)
        botonlogin.setOnClickListener{
            llamarLoginUsuario()
        }

        val botonsalir = findViewById<Button>(R.id.btn_salir)
        botonsalir.setOnClickListener{
            solicitarSalirDelAplicativo()
        }

        val botonProducto = findViewById<Button>(R.id.btn_producto)
        botonProducto.setOnClickListener{
            irProducto()
        }

        val botonDRestaurante = findViewById<Button>(R.id.btn_restaurante)
        botonDRestaurante.setOnClickListener{
            irRestaurante()
        }

        val botonOrdenes = findViewById<Button>(R.id.btn_ordenes)
        botonOrdenes.setOnClickListener{
            irOrdenes()
        }

        val botonGestionOrdenes = findViewById<Button>(R.id.btn_gestiónOrdenes)
        botonGestionOrdenes.setOnClickListener{
            irGestion()
        }

    }

    fun irGestion(){
        val intent = Intent(
            this,
            FGestionOrdenes::class.java
        )
        startActivity(intent)
    }

    fun irOrdenes(){
        val intent = Intent(
            this,
            ECrearOrdenes::class.java
        )
        startActivity(intent)
    }

    fun irRestaurante(){
        val intent = Intent(
            this,
            DCrearRestaurante::class.java
        )
        startActivity(intent)
    }

    fun irProducto(){
        val intent = Intent(
            this,
            CCrearProducto::class.java
        )
        startActivity(intent)
    }


    fun llamarLoginUsuario(){
        //Llamar al proveedor que activamos
        val proveedores = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(proveedores)
                .setTosAndPrivacyPolicyUrls(
                    "https://example.com/terms.html",
                    "https://example.com/privacy.html"
                )
                .build(),
        CODIGO_INICIO_SESION
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){
            CODIGO_INICIO_SESION -> {
                if (resultCode == Activity.RESULT_OK){
                    val usuario: IdpResponse? = IdpResponse.fromResultIntent(data)
                    if(usuario != null){
                        if(usuario.isNewUser == true){
                            Log.i("firebase-login","Nuevo usuario creado")
                            registrarUsuarioPorPrimeraVEz(usuario)
                        } else{
                            setearUsuarioFirebase()
                            Log.i("firebase-login","Usuario Antiguo")
                        }
                    }
                } else{
                    Log.i("firebase-login","El usuario cancelo")
                }
            }
        }
    }

    fun registrarUsuarioPorPrimeraVEz(usuario: IdpResponse){
        val usuarioLogeado = FirebaseAuth
            .getInstance()
            .getCurrentUser()
        if(usuario.email != null && usuarioLogeado != null){
            //guardar roles y uid
            val db = Firebase.firestore //obtener referencia
            val rolesUsuario = arrayListOf("usuario")
            val nuevoUsuario = hashMapOf<String, Any>(
                "roles" to rolesUsuario,
                "uid" to usuarioLogeado.uid,
                "email" to usuarioLogeado.email.toString()
            )

            val identificadorUsuario = usuario.email

            db.collection("usuario")
            //Forma a) Firebase crea el identificador
                //.add(nuevoUsuario)
            //Forma b) Yo seteo el identificado
                .document(identificadorUsuario.toString())
                .set(nuevoUsuario)
                .addOnSuccessListener{
                    Log.i("firebase-firestore","Se creo")
                    setearUsuarioFirebase()
                }
                .addOnFailureListener{
                    Log.i("firebase-firestore","Fallo")
                }

        }
    }

    fun setearUsuarioFirebase(){
        val instanciaAuth = FirebaseAuth.getInstance()
        val usuarioLocal = instanciaAuth.currentUser
        if(usuarioLocal != null){
            if(usuarioLocal.email != null){
                //Buscar en el firestore al usuario
                // y traerlo con todos los datos
                val db = Firebase.firestore
                val referencia = db
                    .collection("usuario")
                    .document(usuarioLocal.email.toString())

                referencia.get()
                    .addOnSuccessListener {
                        val usuarioCargado = it.toObject(FireStoreUsuarioDto::class.java)
                        if(usuarioCargado != null){
                            BAuthUsuario.usuario = BUsuarioFirebase(
                                usuarioCargado.uid,
                                usuarioCargado.email,
                                usuarioCargado.roles
                            )
                            setearBienvenida()
                        }
                        Log.i("firebase-firestore","Usuario cargado")
                    }
                    .addOnFailureListener{
                        Log.i("firebase-firestore","Fallo cargar usuario")
                    }
            }
        }
    }

    fun setearBienvenida(){

        val tvBienvenida = findViewById<TextView>(R.id.tv_bienvenida)
        val botonLogin = findViewById<Button>(R.id.btn_login)
        val botonSalir = findViewById<Button>(R.id.btn_salir)
        val botonProducto = findViewById<Button>(R.id.btn_producto)
        val botonRestaurante = findViewById<Button>(R.id.btn_restaurante)
        val botonOrden = findViewById<Button>(R.id.btn_ordenes)
        val botonGestionOrdenes = findViewById<Button>(R.id.btn_gestiónOrdenes)
        val botonVisualizarOrdenes = findViewById<Button>(R.id.btn_VisualizarOrden)

        if(BAuthUsuario.usuario != null){
            botonLogin.visibility = View.INVISIBLE
            botonSalir.visibility = View.VISIBLE
            tvBienvenida.text = "Bienvenido ${BAuthUsuario.usuario?.email}"
            if(BAuthUsuario.usuario!!.roles[0] == "usuario"){
                botonOrden.visibility = View.VISIBLE
                botonVisualizarOrdenes.visibility = View.VISIBLE
            }else{
                botonProducto.visibility = View.VISIBLE
                botonRestaurante.visibility = View.VISIBLE
                botonGestionOrdenes.visibility = View.VISIBLE
            }
        }else{
            tvBienvenida.text = "Ingresa al aplicativo"
            botonLogin.visibility = View.VISIBLE
            botonSalir.visibility = View.INVISIBLE
            botonProducto.visibility = View.INVISIBLE
            botonRestaurante.visibility = View.INVISIBLE
            botonGestionOrdenes.visibility = View.INVISIBLE
            botonOrden.visibility = View.INVISIBLE
            botonVisualizarOrdenes.visibility = View.INVISIBLE
        }
    }

    fun solicitarSalirDelAplicativo(){
        AuthUI
            .getInstance()
            .signOut(this)
            .addOnCompleteListener{
                BAuthUsuario.usuario = null
                setearBienvenida()
            }
    }
}