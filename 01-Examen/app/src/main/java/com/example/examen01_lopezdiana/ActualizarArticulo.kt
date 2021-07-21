package com.example.examen01_lopezdiana

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class ActualizarArticulo : AppCompatActivity() {

    //Variable para el uso de la BD
    var baseDatos = ESqliteHelper(this)
    //Envio de datos a otra Actividad
    val CODIGO_REPUESTA_INTENT_EXPLICITO = 101

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_articulo)

        //Obtener la papeleria y articulo
        val papeleriaIntet = intent.getParcelableExtra<Papeleria>("papeleria")
        val articuloIntet = intent.getParcelableExtra<Articulo>("articulo")

        //Boton regresar
        val botonRegresar = findViewById<Button>(R.id.btn_regresarActualizarArticulo)
        botonRegresar.setOnClickListener {
            abrirActividadParametro(VisualizarArticulosPorPapeleria::class.java, papeleriaIntet!!)
        }

        //Mostrar los datos
        var nombreEditText = findViewById<EditText>(R.id.box_nombreArticuloActualizar)
        var precioEditText = findViewById<EditText>(R.id.box_precioArticuloActualizar)
        var cantidadEditText = findViewById<EditText>(R.id.box_cantidaArticuloActualizar)
        var marcaEditText = findViewById<EditText>(R.id.box_marcaArticuloActualizar)
        var descripcionEditText = findViewById<EditText>(R.id.box_descripcionArticuloActualizar)

        nombreEditText.setText(articuloIntet!!.nombreArticulo)
        precioEditText.setText(articuloIntet!!.precioArticulo.toString())
        cantidadEditText.setText(articuloIntet!!.cantidadArticulo.toString())
        marcaEditText.setText(articuloIntet!!.marcaArticulo)
        descripcionEditText.setText(articuloIntet!!.descripcionArticulo)

        //Actualizar datos

        val botonActualizarArticulo = findViewById<Button>(R.id.btn_actualizarArticulo)
        botonActualizarArticulo.setOnClickListener {

            //Comprobar si no son nulos
            val nombre = nombreEditText.text.toString()
            val precio = precioEditText.text.toString().toDouble()
            val cantidad = cantidadEditText.text.toString().toInt()
            val marca = marcaEditText.text.toString()
            val descripcion = descripcionEditText.text.toString()

            if (!nombre.isNullOrEmpty() and !marca.isNullOrEmpty() and !descripcion.isNullOrEmpty() and !precio.equals("") and !cantidad.equals("")){

                //Actualizar el artículo
                val respuesta = baseDatos.actualizarArticuloFormulario(nombre,precio,cantidad,marca,descripcion,articuloIntet.idArticulo,
                    papeleriaIntet!!.idPapeleria)

                //Mensaje de retroalimentacion
                if(respuesta){
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Actualización exitosa")
                    builder.setMessage("Se ha actualizado un artículo de manera existosa")
                    builder.setPositiveButton(
                        "Aceptar",
                        DialogInterface.OnClickListener{ dialog, which ->
                            Log.i("Actualizacion", "Se desplego la alerta")
                        }
                    )
                    val dialogo = builder.create()
                    dialogo.show()
                    return@setOnClickListener
                }else{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Actualizacion Fallida")
                    builder.setMessage("Complete todos los campos requeridos para actualizar un artículo")
                    builder.setPositiveButton(
                        "Aceptar",
                        DialogInterface.OnClickListener{ dialog, which ->
                            Log.i("Actualizacion", "Se desplego la alerta")
                        }
                    )
                    val dialogo = builder.create()
                    dialogo.show()
                    return@setOnClickListener
                }
            }
        }
    }

    //Abrir una Actividad mandando una papelería
    fun abrirActividadParametro(
        clase: Class<*>,
        papeleria: Papeleria
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        intentExplicito.putExtra("papeleria",papeleria)
        startActivityForResult(intentExplicito,CODIGO_REPUESTA_INTENT_EXPLICITO)

    }
}