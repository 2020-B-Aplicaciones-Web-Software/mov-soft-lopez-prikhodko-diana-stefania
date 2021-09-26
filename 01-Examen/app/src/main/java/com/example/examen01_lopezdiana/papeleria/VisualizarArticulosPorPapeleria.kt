package com.example.examen01_lopezdiana.papeleria

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.examen01_lopezdiana.R
import com.example.examen01_lopezdiana.articulos.ActualizarArticulo
import com.example.examen01_lopezdiana.entities.Articulo
import com.example.examen01_lopezdiana.entities.Papeleria
import com.example.examen01_lopezdiana.articulos.CrearArticulos
import com.example.examen01_lopezdiana.articulos.UbicacionArticulo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VisualizarArticulosPorPapeleria : AppCompatActivity() {

    //Variable para saber la posicion de una artículo
    var posicionItemSelecionado = 0
    //Referencia a la base de datos
    val db = Firebase.firestore
    //Envio de datos a otra Actividad
    val CODIGO_REPUESTA_INTENT_EXPLICITO = 101
    // Arreglo de articulos
    var arregloArticulos = arrayListOf<Articulo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_articulos_por_papeleria)

        // Papelería actual
        val papeleria = intent.getParcelableExtra<Papeleria>("papeleria")

        //Ver el título con el nombre de la papelería
        val tituloTextView = findViewById<TextView>(R.id.tv_tituloMenuPapeleriaProducto)
        tituloTextView.setText(papeleria?.nombrePapeleria.toString())

        //Llenar List View
        obtenerProductos(papeleria?.nombrePapeleria!!)

        //Crear artículo en esa papelería
        val botonCrearProductos = findViewById<Button>(R.id.btn_AgregarArticulos)
        botonCrearProductos.setOnClickListener {
            abrirActividadConParametros1(CrearArticulos::class.java, papeleria)
        }

        //Regresar de pantalla de visualizar papelerías
        val botonRegresar = findViewById<Button>(R.id.btn_regresarVisualizarArticulo)
        botonRegresar.setOnClickListener {
            abrirActividad(MainActivity::class.java)
        }
    }

    fun obtenerProductos(nombrePapeleria: String){
        val referenciaProductos= db.collection("papeleria/${nombrePapeleria}/productos")
        arregloArticulos = arrayListOf<Articulo>()
        // Obtencion del arreglo de papelerias
        referenciaProductos
            .get()
            .addOnSuccessListener { documentos ->
                documentos.forEach { documento ->
                    arregloArticulos.add(Articulo(
                        documento["id"].toString(),
                        documento["nombre"].toString(),
                        documento["precio"].toString().toDouble(),
                        documento["cantidad"].toString().toInt(),
                        documento["marca"].toString(),
                        documento["descripcion"].toString(),
                        documento["latitud"].toString().toDouble(),
                        documento["longitud"].toString().toDouble()
                    ))
                }
                llenarListView() // Llenar la lista luego de acabar la ejecucion
            }
            .addOnFailureListener {
                Log.i("Error","No se pudo obtener ningun restaurante")
            }

    }

    fun eliminarArticulo(idProducto : String,nombrePapeleria: String){
        // Obtencion del arreglo de papelerias
        val referenciaProductos= db.collection("papeleria/${nombrePapeleria}/productos")
        referenciaProductos
            .document(idProducto)
            .delete()
            .addOnSuccessListener {
                mensaje(true)
                obtenerProductos(nombrePapeleria)
            }
            .addOnFailureListener {
                Log.i("Error","No se pudo obtener ningun restaurante")
            }
    }

    //Dar funcionalidad a los ítems de la lista seleccionado
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId){
            R.id.mi_actualizarArticulo -> {
                val papeleria = intent.getParcelableExtra<Papeleria>("papeleria")
                abrirActividadConParametros(ActualizarArticulo::class.java, papeleria!!,arregloArticulos[posicionItemSelecionado])
                return true
            }
            R.id.mi_borrarArticulo -> {
                val nombrePapeleria = intent.getParcelableExtra<Papeleria>("papeleria")!!.nombrePapeleria
                eliminarArticulo(arregloArticulos[posicionItemSelecionado].idArticulo.toString(),nombrePapeleria!!)
                return true
            }
            R.id.mi_visualizarUbicacion -> {
                val papeleria = intent.getParcelableExtra<Papeleria>("papeleria")
                val articulo = arregloArticulos[posicionItemSelecionado]
                abrirActividadConParametros(UbicacionArticulo::class.java, papeleria!!,articulo)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun mensaje(valor :Boolean){
        val builder = AlertDialog.Builder(this)
        if(valor == true){
            builder.setTitle("Eliminación existosa")
            builder.setMessage("Se ha eliminado el artículo")
        }else{
            builder.setTitle("Problema de eliminación")
            builder.setMessage("Existe un problema para eliminar el artículo")
        }
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener{ dialog, which ->
                Log.i("Mensajes", "Se desplego la alerta")
            }
        )
        val dialogo = builder.create()
        dialogo.show()

    }

    fun llenarListView(){
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arregloArticulos
        )
        val listView= findViewById<ListView>(R.id.lv_productos)
        listView.adapter = adaptador
        //Entregamos la lista contextual
        registerForContextMenu(listView)
    }

    //Crear el menú contextual dentro de la pantalla
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu1, menu) //inclusión
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        posicionItemSelecionado = id
    }

    //Abrir una Actividad mandando una papelería y un artículo
    fun abrirActividadConParametros(clase : Class<*>, papeleria: Papeleria, articulo: Articulo){
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("papeleria", papeleria)
        intentExplicito.putExtra("articulo", articulo)
        startActivityForResult(intentExplicito, CODIGO_REPUESTA_INTENT_EXPLICITO)
    }

    //Abrir una Actividad mandando una papelería
    fun abrirActividadConParametros1(clase : Class<*>, papeleria: Papeleria){
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("papeleria", papeleria)
        startActivityForResult(intentExplicito, CODIGO_REPUESTA_INTENT_EXPLICITO)
    }

    //Abrir una Actividad sin necesidad de mandar parámetros
    fun abrirActividad(clase: Class<*>){
        val intentExplicito = Intent(this, clase)
        startActivity(intentExplicito)
    }
}