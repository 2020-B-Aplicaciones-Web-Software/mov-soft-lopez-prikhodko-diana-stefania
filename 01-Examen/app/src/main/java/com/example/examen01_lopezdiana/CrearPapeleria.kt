package com.example.examen01_lopezdiana

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog

class CrearPapeleria : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_papeleria)

        //Variable para el uso de la BD
        var baseDatos = ESqliteHelper(this)

        //Regresar al menú principal
        val botonRegresar = findViewById<Button>(R.id.btn_regresarCreacionPapeleria)
        botonRegresar.setOnClickListener {
            abrirActividad(MainActivity::class.java)
        }
        //Crear papelería
        val botonCrear = findViewById<Button>(R.id.btn_CrearPapeleria)
        botonCrear.setOnClickListener {

            var nombreEditText = findViewById<EditText>(R.id.box_nombrePapeleriaCrear)
            var direccionEditText = findViewById<EditText>(R.id.box_direccionCrear)
            var fechaCrecionEdiText = findViewById<EditText>(R.id.box_fechaCrear)
            var mayoristaEditText = findViewById<CheckBox>(R.id.cb_MayoristaCrear)

            val nombre = nombreEditText.text.toString()
            val direccion = direccionEditText.text.toString()
            val fechaCreacion = fechaCrecionEdiText.text.toString()
            val mayorista = mayoristaEditText.isChecked

            //Comprobar si no son nulos
            if (!nombre.isNullOrEmpty() and !direccion.isNullOrEmpty() and !fechaCreacion.isNullOrEmpty()){

                //Crear el objeto en la bd
                val respuesta = baseDatos.crearPapeleriaFormulario(nombre,direccion, fechaCreacion,mayorista)

                //Limpiar
                nombreEditText.setText("")
                direccionEditText.setText("")
                fechaCrecionEdiText.setText("")
                mayoristaEditText.isChecked = false

                //Mensaje de retroalimentación
                if (respuesta){
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Creación existosa")
                    builder.setMessage("Se ha creado una papelería de manera existosa")
                    builder.setPositiveButton(
                        "Aceptar",
                        DialogInterface.OnClickListener{dialog,which ->
                            Log.i("Creacion", "Se desplego la alerta")
                        }
                    )
                    var dialogo = builder.create()
                    dialogo.show()
                    return@setOnClickListener
                }

            }else{
                //Mensaje que llene los campos requeridos
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Creación fallida")
                builder.setMessage("Complete todos los campos requeridos para crear una papelería")
                builder.setPositiveButton(
                    "Aceptar",
                    DialogInterface.OnClickListener{dialog,which ->
                        Log.i("Creacion", "Se desplego la alerta")
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