package com.example.examen01_lopezdiana

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class ActualizarPapeleria : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_papeleria)

        //Variable para el uso de la BD
        var baseDatos = ESqliteHelper(this)

        //Regresar al menú principal
        val botonRegresar = findViewById<Button>(R.id.btn_regresarCreacionPapeleriaActualizar)
        botonRegresar.setOnClickListener {
            abrirActividad(MainActivity::class.java)
        }

        //Datos quemados

        val papeleria = intent.getParcelableExtra<Papeleria>("papeleria")

        val id = papeleria?.idPapeleria!!.toInt()
        var nombreEditText = findViewById<EditText>(R.id.box_nombrePapeleriaActualizar)
        var fechaCrecionEdiText = findViewById<EditText>(R.id.box_fechaPapeleriaActualizar)
        var direccionEditText = findViewById<EditText>(R.id.box_direccionPapeleriaActualizar)
        var mayoristaEditText = findViewById<CheckBox>(R.id.cb_MayoristaActualizar)

        nombreEditText.setText(papeleria?.nombrePapeleria.toString())
        fechaCrecionEdiText.setText(papeleria?.fechaAperturaPapeleria.toString())
        direccionEditText.setText(papeleria?.direccionPapeleria.toString())


        if (papeleria.mayorista == true){
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
            if (!nombre.isNullOrEmpty() and !direccion.isNullOrEmpty() and !fechaCreacion.isNullOrEmpty()){
                //Crear el objeto en la bd
                val respuesta = baseDatos.actualizarPapeleriaFormulario(nombre,direccion, fechaCreacion,mayorista,id)
                //Mensaje de retroalimentación
                if (respuesta){
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Actualización existosa")
                    builder.setMessage("Se ha actualizado una papelería de manera existosa")
                    builder.setPositiveButton(
                        "Aceptar",
                        DialogInterface.OnClickListener{ dialog, which ->
                            Log.i("Actualizacion", "Se desplego la alerta")
                        }
                    )
                    var dialogo = builder.create()
                    dialogo.show()
                    return@setOnClickListener
                }

            }else{
                //Mensaje que llene los campos requeridos
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Actualización fallida")
                builder.setMessage("Complete todos los campos requeridos para actualizar una papelería")
                builder.setPositiveButton(
                    "Aceptar",
                    DialogInterface.OnClickListener{ dialog, which ->
                        Log.i("Actualizacion", "Se desplego la alerta")
                    }
                )
                var dialogo = builder.create()
                dialogo.show()
                return@setOnClickListener
            }

        }


    }
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