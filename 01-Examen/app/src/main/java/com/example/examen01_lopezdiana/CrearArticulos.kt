package com.example.examen01_lopezdiana

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class CrearArticulos : AppCompatActivity() {

    val CODIGO_REPUESTA_INTENT_EXPLICITO = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_articulos)

        //Variable para el uso de la BD
        var baseDatos = ESqliteHelper(this)

        //Datos de la papeleria
        val papeleriaIntent = intent.getParcelableExtra<Papeleria>("papeleria")

        //Regresar al menú principal
        var botonRegresarArticulo = findViewById<Button>(R.id.btn_regresarCreacionArticulo)
        botonRegresarArticulo.setOnClickListener {
            abrirActividadConParametros(VisualizarArticulosPorPapeleria::class.java,
                papeleriaIntent!!
            )
        }

        //Crear articulo
        var botonCrearArticulo = findViewById<Button>(R.id.btn_crearArticulo)
        botonCrearArticulo.setOnClickListener {

            //Obtener los datos
            var nombreEditText = findViewById<EditText>(R.id.box_nombreArticuloCrear)
            var precioEditText = findViewById<EditText>(R.id.box_precioArticuloCrear)
            var cantidadEditText = findViewById<EditText>(R.id.box_cantidadArticuloCrear)
            var marcaEditText = findViewById<EditText>(R.id.box_marcaArticuloCrear)
            var descripcionEditText = findViewById<EditText>(R.id.box_descripcionArticuloCrear)

            // Obtener información

            val nombre = nombreEditText.text.toString()
            val precio = precioEditText.text.toString().toDouble()
            val cantidad = cantidadEditText.text.toString().toInt()
            val marca = marcaEditText.text.toString()
            val descripcion = descripcionEditText.text.toString()

            //Comprobar si no son nulos
            if(!nombre.isNullOrEmpty() and !precio.equals("") and !cantidad.equals("") and !marca.isNullOrEmpty() and !descripcion.isNullOrEmpty()){

                //Crear Articulo
                val respuesta = baseDatos.crearArticuloFormulario(nombre,precio,cantidad,marca,descripcion,
                    papeleriaIntent!!.idPapeleria)

                //Limpiar la vista
                nombreEditText.setText("")
                precioEditText.setText("")
                cantidadEditText.setText("")
                marcaEditText.setText("")
                descripcionEditText.setText("")

                //Muestra un mensaje de retroalimentación
                if (respuesta){
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Creación Exitosa")
                    builder.setMessage("Se ha creado un artículo de manera existosa")
                    builder.setPositiveButton(
                        "Aceptar",
                         DialogInterface.OnClickListener{ dialog, which ->
                            Log.i("Creacion", "Se desplego la alerta")
                        }
                    )
                    val dialogo = builder.create()
                    dialogo.show()
                    return@setOnClickListener
                }else{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Creación Fallida")
                    builder.setMessage("Complete todos los campos requeridos para crear un artículo")
                    builder.setPositiveButton(
                        "Aceptar",
                        DialogInterface.OnClickListener{ dialog, which ->
                            Log.i("Creacion", "Se desplego la alerta")
                        }
                    )
                    val dialogo = builder.create()
                    dialogo.show()
                    return@setOnClickListener
                }

            }

        }

    }

    fun abrirActividadConParametros(
        clase : Class<*>,
        papeleria: Papeleria
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        intentExplicito.putExtra("papeleria", papeleria)
        startActivityForResult(intentExplicito, CODIGO_REPUESTA_INTENT_EXPLICITO)
    }
}