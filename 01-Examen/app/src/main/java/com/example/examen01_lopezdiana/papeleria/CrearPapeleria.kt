package com.example.examen01_lopezdiana.papeleria

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.examen01_lopezdiana.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class CrearPapeleria : AppCompatActivity() {

    //Referencia a la base de datos
    val db = Firebase.firestore
    val referenciaPapeleria= db.collection("papeleria")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_papeleria)

        //Regresar al menú principal
        val botonRegresar = findViewById<Button>(R.id.btn_regresarCreacionPapeleria)
        botonRegresar.setOnClickListener {
            abrirActividad(MainActivity::class.java)
        }

        //Crear papelería
        val botonCrear = findViewById<Button>(R.id.btn_CrearPapeleria)
        botonCrear.setOnClickListener {

            //Obtener los EditText
            val nombre = findViewById<EditText>(R.id.box_nombrePapeleriaCrear).text
            val direccion = findViewById<EditText>(R.id.box_direccionCrear).text
            val fechaCreacionText = findViewById<EditText>(R.id.box_fechaCrear).text
            val mayorista = findViewById<CheckBox>(R.id.cb_MayoristaCrear)


            //Comprobar si no son nulos
            if (!nombre.isNullOrEmpty() and !direccion.isNullOrEmpty() and !fechaCreacionText.isNullOrEmpty()){
                // Formatear fecha
                val formatoFecha = SimpleDateFormat("dd/M/yyyy")
                val fechaCreacion = formatoFecha.parse(fechaCreacionText.toString())

                //Crear la papelería en la bd
                agregarPapeleria(nombre.toString(),direccion.toString(),fechaCreacion,mayorista.isChecked)

                //Limpiar los EditText para seguir creando
                nombre.clear()
                direccion.clear()
                fechaCreacionText.clear()
                mayorista.isChecked = false
            } else{
                mensaje(false)
            }
        }
    }

    fun agregarPapeleria(nombre: String, direccion: String, fechaCreacion: Date, mayorista: Boolean) {

        val idGenerado = referenciaPapeleria.document().id

        val nuevaPapeleria = hashMapOf<String,Any>(
            "id" to idGenerado,
            "nombre" to nombre,
            "direccion" to direccion,
            "fechaCreacion" to Timestamp(fechaCreacion),
            "mayorista" to mayorista
        )
        referenciaPapeleria
            .document(idGenerado)
            .set(nuevaPapeleria)
            .addOnSuccessListener {
                mensaje(true)
            }
            .addOnFailureListener {
                mensaje(false)
            }
    }

    fun mensaje(tipo : Boolean){
        val builder = AlertDialog.Builder(this)
        if(tipo == true){
            builder.setTitle("Creación existosa")
            builder.setMessage("Se ha creado una papelería de manera existosa")
        }else{
            builder.setTitle("Creación fallida")
            builder.setMessage("Compruebe los datos ingresados")
        }
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener{ dialog, which ->
                Log.i("Mensajes", "Se desplego la alerta")
            }
        )
        var dialogo = builder.create()
        dialogo.show()
    }

    //Abrir una Actividad sin necesidad de mandar parámetros
    fun abrirActividad(
        clase : Class<*>
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        startActivity(intentExplicito)
    }
}