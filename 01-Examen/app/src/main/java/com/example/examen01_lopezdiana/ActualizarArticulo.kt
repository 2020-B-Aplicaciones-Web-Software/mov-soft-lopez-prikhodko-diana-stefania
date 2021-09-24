package com.example.examen01_lopezdiana

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.examen01_lopezdiana.entities.Articulo
import com.example.examen01_lopezdiana.entities.Papeleria
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActualizarArticulo : AppCompatActivity() {

    //Referencia a la base de datos
    val db = Firebase.firestore
    //Envio de datos a otra Actividad
    val CODIGO_REPUESTA_INTENT_EXPLICITO = 101

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_articulo)

        //Obtener la papeleria y articulo
        val papeleria = intent.getParcelableExtra<Papeleria>("papeleria")
        val articulo = intent.getParcelableExtra<Articulo>("articulo")

        //Boton regresar
        val botonRegresar = findViewById<Button>(R.id.btn_regresarActualizarArticulo)
        botonRegresar.setOnClickListener {
            abrirActividadParametro(VisualizarArticulosPorPapeleria::class.java, papeleria!!)
        }

        //Mostrar los datos
        var nombreEditText = findViewById<EditText>(R.id.box_nombreArticuloActualizar)
        var precioEditText = findViewById<EditText>(R.id.box_precioArticuloActualizar)
        var cantidadEditText = findViewById<EditText>(R.id.box_cantidaArticuloActualizar)
        var marcaEditText = findViewById<EditText>(R.id.box_marcaArticuloActualizar)
        var descripcionEditText = findViewById<EditText>(R.id.box_descripcionArticuloActualizar)
        var latitudEditText = findViewById<EditText>(R.id.box_latitudArticuloActualizar)
        var longitudEditText = findViewById<EditText>(R.id.box_longitudArticuloActualizar)

        nombreEditText.setText(articulo!!.nombreArticulo)
        precioEditText.setText(articulo!!.precioArticulo.toString())
        cantidadEditText.setText(articulo!!.cantidadArticulo.toString())
        marcaEditText.setText(articulo!!.marcaArticulo)
        descripcionEditText.setText(articulo!!.descripcionArticulo)
        latitudEditText.setText(articulo!!.ubicacionLatArticulo.toString())
        longitudEditText.setText(articulo!!.ubicacionLngArticulo.toString())

        //Actualizar datos

        val botonActualizarArticulo = findViewById<Button>(R.id.btn_actualizarArticulo)
        botonActualizarArticulo.setOnClickListener {

            //Comprobar si no son nulos
            val nombre = nombreEditText.text.toString()
            val precio = precioEditText.text.toString().toDouble()
            val cantidad = cantidadEditText.text.toString().toInt()
            val marca = marcaEditText.text.toString()
            val descripcion = descripcionEditText.text.toString()
            val latitud = latitudEditText.text.toString()
            val longitud = longitudEditText.text.toString()

            if (!nombre.isNullOrEmpty() and !marca.isNullOrEmpty() and !descripcion.isNullOrEmpty()
                and !precio.equals("") and !cantidad.equals("") and !latitud.equals("") and !longitud.equals("")){
                actualizarProducto(articulo.idArticulo.toString(),papeleria?.nombrePapeleria.toString(),nombre,precio,cantidad,marca,descripcion,latitud.toDouble(),longitud.toDouble())
            }else{
                mensaje(false)
            }
        }
    }


    fun actualizarProducto(id : String, papeleriaNombre : String,nombre:String, precio: Double,cantidad: Int, marca : String, descripcion: String, latitud: Double, longitud:Double){

        val referenciaProductos= db.collection("papeleria/${papeleriaNombre}/productos")

        db.runTransaction{actualizacion ->
            actualizacion.update(
                referenciaProductos.document(id),
                mapOf(
                    "nombre" to nombre,
                    "precio" to precio,
                    "cantidad" to cantidad,
                    "marca" to marca,
                    "descripcion" to descripcion,
                    "latitud" to latitud,
                    "longitud" to longitud
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
            builder.setMessage("Se ha actualizado un producto de manera existosa")
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


    //Abrir una Actividad mandando una papelería
    fun abrirActividadParametro(clase: Class<*>, papeleria: Papeleria){
        val intentExplicito = Intent(this,clase)
        intentExplicito.putExtra("papeleria",papeleria)
        startActivityForResult(intentExplicito,CODIGO_REPUESTA_INTENT_EXPLICITO)

    }
}