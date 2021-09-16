package com.example.firebaseuno

import android.content.DialogInterface
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.example.firebaseuno.dto.HProducto
import com.example.firebaseuno.dto.IRestaurante
import com.example.firebaseuno.dto.JOrden
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GVisualizarOrdenes : AppCompatActivity() {

    //Variable para el manejo del guardado de query
    var query: Query? = null
    var arregloOrden = arrayListOf<JOrden>()
    var arregloDocumentos = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gvisualizar_ordenes)

        //Ejecutar por primera vez la visualización
        vermas()

        //Boton Ver mas
        val botonCargarMasOrdenes= findViewById<Button>(R.id.btn_cargarMasOrdenes)
        botonCargarMasOrdenes.setOnClickListener {
            vermas()
        }
    }

    fun actualizarVista(posicion: Int){
        arregloDocumentos.removeAt(posicion)
        arregloOrden.removeAt(posicion)
        cargarListView()
    }

    fun actualizarRestaurante(posicion: Int, puntaje: Int){
        val db = Firebase.firestore
        val referenciaRestaurante = db.collection("restaurante").document(arregloOrden[posicion].restaurante.toString())

        db.runTransaction{ transaccion ->
            val documentoActual = transaccion.get(referenciaRestaurante)
            var calificacionMedia = documentoActual.get("calificacionMedia").toString().toDouble()
            var cantidadUsuarios = documentoActual.get("cantidadUsuarios").toString().toInt()
            var sumatoria = documentoActual.get("sumatoria").toString().toDouble()
            if(puntaje != null && sumatoria != null && cantidadUsuarios != null && calificacionMedia != null){
                if (cantidadUsuarios < 1000){
                    sumatoria += puntaje
                    cantidadUsuarios += 1
                    calificacionMedia = (sumatoria / cantidadUsuarios)
                } else{
                    sumatoria = (calificacionMedia + puntaje)
                    cantidadUsuarios = 2
                    calificacionMedia = (sumatoria / cantidadUsuarios)
                }
                transaccion.update(referenciaRestaurante,"calificacionMedia",calificacionMedia)
                transaccion.update(referenciaRestaurante,"cantidadUsuarios",cantidadUsuarios)
                transaccion.update(referenciaRestaurante,"sumatoria",sumatoria)

            }
        }
            .addOnSuccessListener { Log.i("transaccion","Transaccion completada") }
            .addOnFailureListener { Log.i("transaccion","ERROR transaccion") }
    }

    fun actualizarOrden(posicion :Int, puntaje: Int){
        val db = Firebase.firestore
        val referenciaOrden = db.collection("orden").document(arregloDocumentos[posicion])
        db.runTransaction{ transaccion ->
            val documentoActual = transaccion.get(referenciaOrden)
            val calificacion = documentoActual.get("calificacion")
            if(puntaje != null && calificacion != null){
                transaccion.update(referenciaOrden,"calificacion",puntaje)
                transaccion.update(referenciaOrden,"estado","Entregado")
            }
        }
            .addOnSuccessListener { Log.i("transaccion","Transaccion completada") }
            .addOnFailureListener { Log.i("transaccion","ERROR transaccion") }
    }

    fun vermas(){
        val db = Firebase.firestore
        val referenciaOrden = db.collection("orden")
            .whereEqualTo("usuario",BAuthUsuario.usuario?.email)
            .whereEqualTo("estado","Enviado")
            .limit(3)

        var tarea: Task<QuerySnapshot>? = null
        if (query == null){
            tarea = referenciaOrden.get()
        } else {
            tarea = query!!.get()
        }
        if(tarea != null){
            tarea
                .addOnSuccessListener { result ->
                    guardarQuery(result, referenciaOrden)
                        result.forEach{documento ->
                            arregloDocumentos.add(documento.id)
                            val fechaPedido = documento.data["fechaPedido"].toString()
                            val total = documento.data["total"].toString().toDouble()
                            val calificacion = documento.data["calificacion"].toString().toInt()
                            val usuario = documento.data["usuario"].toString()
                            val restaurante = documento.data["restaurante"] as HashMap<String,Any>
                            val productos = documento.data["productos"] as ArrayList<HashMap<String, Any>>
                            val listaProductos = productos.map { it ->
                                HProducto(
                                    it["uid"].toString(),
                                    it["producto"].toString(),
                                    it["cantidad"].toString().toInt(),
                                    it["precio"].toString().toDouble()
                                )
                            }
                            arregloOrden.add(JOrden(fechaPedido,usuario, total,calificacion, ArrayList(listaProductos), restaurante.getValue("nombre").toString()))
                        }
                        cargarListView()
                    }
                .addOnFailureListener {
                    Log.i("consultas", "${it}")
                }
        }
    }

    fun guardarQuery(documentSnapshot: QuerySnapshot, citiesRef : Query){
        if(documentSnapshot.size() > 0){
            val ultimoDocumento = documentSnapshot.documents[documentSnapshot.size() -1]
            query = citiesRef
                .startAfter(ultimoDocumento)
        }
    }

    fun cargarListView() {
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arregloOrden
        )
        val listView = findViewById<ListView>(R.id.lv_visualizarOrdenes)
        listView.adapter = adaptador

        listView.setOnItemLongClickListener{ adapterView, view, posicion, id ->
            val opciones = resources.getStringArray(R.array.opcionesCalificacion)
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Califique su compra")
            builder.setSingleChoiceItems(opciones, -1) { dialogInterface, i ->
                val calificacion = i + 1
                actualizarOrden(posicion, calificacion)
                actualizarRestaurante(posicion,calificacion)
                Thread.sleep(1_000)
                actualizarVista(posicion)
                dialogInterface.dismiss()
            }
            builder.setNeutralButton("Cerrar") { dialog, which -> dialog.cancel() }
            val dialogo = builder.create()
            dialogo.show()
            return@setOnItemLongClickListener true
        }

    }


}