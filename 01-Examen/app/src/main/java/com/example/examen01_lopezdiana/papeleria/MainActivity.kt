package com.example.examen01_lopezdiana.papeleria

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.example.examen01_lopezdiana.R
import com.example.examen01_lopezdiana.entities.Articulo
import com.example.examen01_lopezdiana.entities.Papeleria
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    //Variable para saber la posicion de una papelería
    var posicionItemSelecionado = 0

    //Referencia a la base de datos
    val db = Firebase.firestore
    val referenciaPapeleria= db.collection("papeleria")

    //Envio de datos a otra Actividad
    val CODIGO_REPUESTA_INTENT_EXPLICITO = 100

    // Arreglo de papelerias
    var arregloPapeleria = arrayListOf<Papeleria>()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Abrir Actividad CrearPapeleria
        val botonAgregarPapeleria = findViewById<Button>(R.id.btn_AgregarPapelería)
        botonAgregarPapeleria.setOnClickListener {
            abrirActividad(CrearPapeleria::class.java)
        }

        //Llenar el ListView y la manipulación de sus menús contextuales
        obtenerPapelerias()
    }

    fun obtenerPapelerias() {
        arregloPapeleria = arrayListOf<Papeleria>()
        // Obtencion del arreglo de papelerias
        referenciaPapeleria
            .get()
            .addOnSuccessListener { documentos ->
                documentos.forEach { documento ->
                    val fecha = documento["fechaCreacion"] as Timestamp
                    arregloPapeleria.add(Papeleria(
                        documento["id"].toString(),
                        documento["nombre"].toString(),
                        documento["direccion"].toString(),
                        fecha?.toDate(),
                        documento["mayorista"] as Boolean?
                    ))

                }
                llenarListView() // Llenar la lista luego de acabar la ejecucion
            }
            .addOnFailureListener {
                Log.i("Error","No se pudo obtener ninguna papeleria")
            }
    }

    fun eliminarPapeleria(idPapeleria : String){

        var arregloArticulos = arrayListOf<Articulo>()
        val referenciaArticulos= db.collection("papeleria/${idPapeleria}/productos")

        // Obtencion del arreglo de papelerias
        referenciaArticulos
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
                arregloArticulos.forEach{producto ->
                    referenciaArticulos
                        .document(producto.idArticulo.toString())
                        .delete()
                        .addOnSuccessListener {
                            Log.i("Eliminacion","Se estan eliminando todos los productos")
                        }
                        .addOnFailureListener {
                            Log.i("Error","No se pudo obtener ninguna papeleria")
                        }
                }
            }
            .addOnFailureListener {
                Log.i("Error","No se pudo obtener ninguna papeleria")
            }


        // Obtencion del arreglo de papelerias
        referenciaPapeleria
            .document(idPapeleria)
            .delete()
            .addOnSuccessListener {
                mensaje()
                obtenerPapelerias() // Llenar la lista luego de acabar la ejecucion
            }
            .addOnFailureListener {
                Log.i("Error","No se pudo obtener ninguna papeleria")
            }
    }

    fun llenarListView(){

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arregloPapeleria
        )
        //Asignamos a la lista el adaptador
        val listView= findViewById<ListView>(R.id.lv_productos)
        listView.adapter = adaptador
        //Entregamos la lista contextual
        registerForContextMenu(listView)
    }

    //Abrir una Actividad
    fun abrirActividad( clase : Class<*> ){
        val intentExplicito = Intent(this, clase )
        startActivity(intentExplicito)
    }

    //Abrir una Actividad mandando una papelería
    fun abrirActividadConParametros(clase : Class<*>, papeleria: Papeleria){
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("papeleria", papeleria)
        startActivityForResult(intentExplicito, CODIGO_REPUESTA_INTENT_EXPLICITO)
    }

    //Crear el menú contextual dentro de la pantalla
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        // Almacenar cual papelería seleccionamos
        posicionItemSelecionado = id
    }

    //Dar funcionalidad a los ítems de la lista seleccionado
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId){
            R.id.mi_visualizar -> {
                // Obtener el objeto y mandarlo a la actividad de visualizar los artículos
                val papeleria = arregloPapeleria[posicionItemSelecionado]
                abrirActividadConParametros(VisualizarArticulosPorPapeleria::class.java,papeleria)
                return true
            }
            R.id.mi_borrarPapeleria -> {
                //Obtener el nombre de la papelería a eliminar
                val idPapeleria = arregloPapeleria[posicionItemSelecionado].idPapeleria
                eliminarPapeleria(idPapeleria!!)
                return true
            }
            R.id.mi_actualizarPapeleria ->{
                //Obtener el objeto papelería a actualizar y mandarlo a la actividad
                val papeleria = arregloPapeleria[posicionItemSelecionado]
                abrirActividadConParametros(ActualizarPapeleria::class.java, papeleria)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun mensaje(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminación existosa")
        builder.setMessage("Se ha eliminado una papelería de manera existosa")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener{ dialog, which ->
                Log.i("Mensajes", "Se desplego la alerta")
            }
        )
        var dialogo = builder.create()
        dialogo.show()
    }

}