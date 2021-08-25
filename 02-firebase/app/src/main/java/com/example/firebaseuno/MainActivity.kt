package com.example.firebaseuno

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
                "uid" to usuarioLogeado.uid
            )
            db.collection("usuario")
            //Forma a) Firebase crea el identificador
                .add(nuevoUsuario)
            //Forma b) Yo seteo el identificado
                .addOnSuccessListener{
                    Log.i("firebase-firestore","Se creo")
                }
                .addOnFailureListener{
                    Log.i("firebase-firestore","Fallo")
                }

        }
    }
}