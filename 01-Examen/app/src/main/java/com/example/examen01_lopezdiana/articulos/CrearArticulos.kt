package com.example.examen01_lopezdiana.articulos

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.examen01_lopezdiana.R
import com.example.examen01_lopezdiana.papeleria.VisualizarArticulosPorPapeleria
import com.example.examen01_lopezdiana.entities.Papeleria
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CrearArticulos : AppCompatActivity() {

    //Envio de datos a otra Actividad
    val CODIGO_REPUESTA_INTENT_EXPLICITO = 101
    //Referencia a la base de datos
    val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_articulos)

        //Datos de la papeleria
        val papeleriaIntent = intent.getParcelableExtra<Papeleria>("papeleria")

        //Regresar al menú principal
        var botonRegresarArticulo = findViewById<Button>(R.id.btn_regresarCreacionArticulo)
        botonRegresarArticulo.setOnClickListener {
            abrirActividadConParametros(VisualizarArticulosPorPapeleria::class.java, papeleriaIntent!!)
        }

        //Crear articulo
        var botonCrearArticulo = findViewById<Button>(R.id.btn_crearArticulo)
        botonCrearArticulo.setOnClickListener {

            //Obtener los datos
            val nombre = findViewById<EditText>(R.id.box_nombreArticuloCrear).text
            val precio = findViewById<EditText>(R.id.box_precioArticuloCrear).text
            val cantidad = findViewById<EditText>(R.id.box_cantidadArticuloCrear).text
            val marca = findViewById<EditText>(R.id.box_marcaArticuloCrear).text
            val descripcion = findViewById<EditText>(R.id.box_descripcionArticuloCrear).text
            val latitud = findViewById<EditText>(R.id.box_latitudArticuloCrear).text
            var longitud = findViewById<EditText>(R.id.box_longitudArticuloCrear).text

            //Comprobar si no son nulos
            if(!nombre.isNullOrEmpty() and !precio.isNullOrEmpty() and !cantidad.isNullOrEmpty()
                and !marca.isNullOrEmpty() and !descripcion.isNullOrEmpty() and !latitud.isNullOrEmpty()
                and !longitud.isNullOrEmpty()){
                // Llamar al método
                crearArticulo(papeleriaIntent?.idPapeleria.toString(), nombre.toString(),precio.toString().toDouble(),cantidad.toString().toInt(),marca.toString(),descripcion.toString(),latitud.toString().toDouble(), longitud.toString().toDouble())
                //Limpiar la vista
                nombre.clear()
                precio.clear()
                cantidad.clear()
                marca.clear()
                descripcion.clear()
                latitud.clear()
                longitud.clear()
            }else{
                mensaje(false)
            }
        }
    }

    fun crearArticulo(idPapeleria : String, nombre: String, precio: Double, cantidad: Int, marca: String, descripcion: String, latitud: Double, longitud: Double) {

        val referenciaProductos= db.collection("papeleria/${idPapeleria}/productos")

        val idGenerado = referenciaProductos.document().id

        val nuevoProducto = hashMapOf<String,Any>(
            "id" to idGenerado,
            "nombre" to nombre,
            "precio" to precio,
            "cantidad" to cantidad,
            "marca" to marca,
            "descripcion" to descripcion,
            "latitud" to latitud,
            "longitud" to longitud
        )

        referenciaProductos
            .document(idGenerado)
            .set(nuevoProducto)
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
            builder.setMessage("Se ha creado un producto de manera existosa")
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

    //Abrir una Actividad mandando una papelería
    fun abrirActividadConParametros(clase : Class<*>, papeleria: Papeleria){
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("papeleria", papeleria)
        startActivityForResult(intentExplicito, CODIGO_REPUESTA_INTENT_EXPLICITO)
    }
}