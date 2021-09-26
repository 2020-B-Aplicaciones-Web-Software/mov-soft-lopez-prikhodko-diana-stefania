package com.example.examen01_lopezdiana.papeleria

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.examen01_lopezdiana.R
import com.example.examen01_lopezdiana.entities.Papeleria
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class ActualizarPapeleria : AppCompatActivity() {

    //Referencia a la base de datos
    val db = Firebase.firestore
    val referenciaRestaurante= db.collection("papeleria")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_papeleria)

        //Regresar al menú principal
        val botonRegresar = findViewById<Button>(R.id.btn_regresarCreacionPapeleriaActualizar)
        botonRegresar.setOnClickListener {
            abrirActividad(MainActivity::class.java)
        }

        //Obtener los datos de la papelería
        val papeleria = intent.getParcelableExtra<Papeleria>("papeleria")


        //Obtener los EditText con la información
        var nombreEditText = findViewById<EditText>(R.id.box_nombrePapeleriaActualizar)
        var fechaCrecionEdiText = findViewById<EditText>(R.id.box_fechaPapeleriaActualizar)
        var direccionEditText = findViewById<EditText>(R.id.box_direccionPapeleriaActualizar)
        var mayoristaEditText = findViewById<CheckBox>(R.id.cb_MayoristaActualizar)

        // Formatear fecha
        val format = SimpleDateFormat("dd/MM/yyyy")
        val fechaCreacion = format.format(papeleria?.fechaAperturaPapeleria)

        //Colocar los datos de la papelería a actualizar
        nombreEditText.setText(papeleria?.nombrePapeleria)
        fechaCrecionEdiText.setText(fechaCreacion.toString())
        direccionEditText.setText(papeleria?.direccionPapeleria)
        if (papeleria!!.mayorista == true) {
            mayoristaEditText.isChecked = true
        }

        //Boton Actualizar
        val botonActualizar = findViewById<Button>(R.id.btn_ActualizarPapeleria)
        botonActualizar.setOnClickListener {

            //Obtener valores
            val nombre = nombreEditText.text.toString()
            val direccion = direccionEditText.text.toString()
            val fechaCreacion = fechaCrecionEdiText.text.toString()
            val mayorista = mayoristaEditText.isChecked

            //Comprobar si no son nulos
            if (!nombre.isNullOrEmpty() and !direccion.isNullOrEmpty() and !fechaCreacion.isNullOrEmpty()) {
                actualizarRestaurante(nombre,fechaCreacion,direccion,mayorista)
            }else{
                mensaje(false)
            }
        }
    }

    fun actualizarRestaurante(nombre : String, fecha:String, direccion: String, mayorista: Boolean){
        // Formatear fecha
        val formatoFecha = SimpleDateFormat("dd/M/yyyy")
        val fechaCreacion = formatoFecha.parse(fecha)

        db.runTransaction{actualizacion ->
            actualizacion.update(
                referenciaRestaurante.document(nombre),
                mapOf(
                    "nombre" to nombre,
                    "direccion" to direccion,
                    "mayorista" to mayorista,
                    "fechaCreacion" to Timestamp(fechaCreacion)
                )
            )
        }
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
            builder.setTitle("Actualización existosa")
            builder.setMessage("Se ha actualizado una papelería de manera existosa")
        }else{
            builder.setTitle("Actualización fallida")
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

    //Abrir la Actividad de regreso sin necesidad de mandar ningún parámetro
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