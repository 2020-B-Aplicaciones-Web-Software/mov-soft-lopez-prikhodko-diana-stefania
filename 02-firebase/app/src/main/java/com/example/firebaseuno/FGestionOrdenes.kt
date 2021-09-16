package com.example.firebaseuno

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.firebaseuno.dto.HProducto
import com.example.firebaseuno.dto.IRestaurante
import com.example.firebaseuno.dto.JOrden
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FGestionOrdenes : AppCompatActivity() {

    //Arreglo restaurantes y orden
    var arregloRestaurantes = arrayListOf<IRestaurante>()
    var arregloOrden = arrayListOf<JOrden>()
    //Variable para saber la posicion de una articulo de la orden
    var posicionItemSelecionado = 0
    // Referencia a los documentos
    var arregloDocumentos = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fgestion_ordenes)

        //Llenar Spinner restaurante, list view y filtro
        cargarRestaurantes()
        cargarFiltro()

        val botonActualizarLVGestionar = findViewById<Button>(R.id.btn_actualizarLVGestionar)
        botonActualizarLVGestionar.setOnClickListener {
            val restauranteSpinner = findViewById<Spinner>(R.id.sp_restauranteGestionar).selectedItem.toString()
            val estadoSpinner = findViewById<Spinner>(R.id.sp_filtro).selectedItem.toString()
            cargarOrdenes(estadoSpinner,restauranteSpinner)
        }
    }

    fun cargarOrdenes(estado : String, restaurante : String){
        arregloOrden = arrayListOf()
        arregloDocumentos = arrayListOf()
        val db = Firebase.firestore
        val dates = db.collection("orden")
            .whereEqualTo("estado", estado)
            .whereEqualTo("restaurante.nombre",restaurante)
            .get()
            .addOnSuccessListener { result ->
                result.forEach{documento ->
                    arregloDocumentos.add(documento.id)
                    val fechaPedido = documento.data["fechaPedido"].toString()
                    val total = documento.data["total"].toString().toDouble()
                    val calificacion = documento.data["calificacion"].toString().toInt()
                    val usuario = documento.data["usuario"].toString()
                    val productos = documento.data["productos"] as ArrayList<HashMap<String,Any>>
                    val listaProductos = productos.map { it ->
                        HProducto(it["uid"].toString(),it["producto"].toString(),it["cantidad"].toString().toInt(), it["precio"].toString().toDouble())
                    }
                    arregloOrden.add(JOrden(fechaPedido,usuario, total,calificacion, ArrayList(listaProductos), null))
                }
                cargarListView()
            }
            .addOnFailureListener { exception ->
                Log.d("firebasedata", "Error getting documents: ", exception)
            }
    }

    fun cargarListView() {
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arregloOrden
        )
        val listView = findViewById<ListView>(R.id.lv_ordenesGestionar)
        listView.adapter = adaptador
        registerForContextMenu(listView)
    }

    fun cargarRestaurantes(){

        val spinnerArray: MutableList<String> = ArrayList()

        val db = Firebase.firestore
        val dates = db.collection("restaurante").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val uid = document.data.get("uid").toString()
                    val nombre = document.data.get("nombre").toString()
                    val calificacion = document.data.get("calificacionMedia").toString().toDouble()
                    spinnerArray.add(nombre)
                    arregloRestaurantes.add(IRestaurante(uid, nombre, calificacion))
                }
                val adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, spinnerArray
                )

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                val sItems = findViewById<Spinner>(R.id.sp_restauranteGestionar)
                sItems.adapter = adapter
            }
            .addOnFailureListener {  }

    }

    //Crear el menú contextual dentro de la pantalla

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        val estadoSpinner = findViewById<Spinner>(R.id.sp_filtro).selectedItem.toString()
        if(estadoSpinner == "Por Recibir"){
            inflater.inflate(R.menu.menuporrecibir, menu)
        }else if (estadoSpinner == "Preparando"){
            inflater.inflate(R.menu.menupreparando, menu)
        }else{
            inflater.inflate(R.menu.menuvacio, menu)
        }
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        posicionItemSelecionado = id

    }

    //Dar funcionalidad a los ítems de la lista seleccionado
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            //Cambiar a estado Preparando
            R.id.Preparando -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("La compra va a cambiar a Preparando")
                builder.setMessage("¿Esta seguro de su cambio?")
                builder.setPositiveButton(
                    "Si",
                    DialogInterface.OnClickListener { dialog, which ->
                        transaccion("Preparando")
                        Thread.sleep(1_000)
                        val restauranteSpinner = findViewById<Spinner>(R.id.sp_restauranteGestionar).selectedItem.toString()
                        val estadoSpinner = findViewById<Spinner>(R.id.sp_filtro).selectedItem.toString()
                        cargarOrdenes(estadoSpinner,restauranteSpinner)
                        cargarListView()
                    }
                )
                builder.setNegativeButton(
                    "No",
                    DialogInterface.OnClickListener { dialog, which ->
                        Log.i("Estado", "No se cambio de estado")
                    }
                )
                val dialogo = builder.create()
                dialogo.show()
                return true
            }
            //Cambiar a estado Enviado
            R.id.Enviado -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("La compra va a cambiar a Enviado")
                builder.setMessage("¿Esta seguro de su cambio?")
                builder.setPositiveButton(
                    "Si",
                    DialogInterface.OnClickListener { dialog, which ->
                        transaccion("Enviado")
                        Thread.sleep(1_000)
                        val restauranteSpinner = findViewById<Spinner>(R.id.sp_restauranteGestionar).selectedItem.toString()
                        val estadoSpinner = findViewById<Spinner>(R.id.sp_filtro).selectedItem.toString()
                        cargarOrdenes(estadoSpinner,restauranteSpinner)
                        cargarListView()
                    }
                )
                builder.setNegativeButton(
                    "No",
                    DialogInterface.OnClickListener { dialog, which ->
                        Log.i("Estado", "No se cambio de estado")
                    }
                )
                val dialogo = builder.create()
                dialogo.show()
                return true
            }
            //Cambiar a estado Cancelado
            R.id.Cancelado -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("La compra va a cambiar a Cancelado")
                builder.setMessage("¿Esta seguro de su cambio?")
                builder.setPositiveButton(
                    "Si",
                    DialogInterface.OnClickListener { dialog, which ->
                        transaccion("Cancelado")
                        Thread.sleep(1_000)
                        val restauranteSpinner = findViewById<Spinner>(R.id.sp_restauranteGestionar).selectedItem.toString()
                        val estadoSpinner = findViewById<Spinner>(R.id.sp_filtro).selectedItem.toString()
                        cargarOrdenes(estadoSpinner,restauranteSpinner)
                        cargarListView()
                    }
                )
                builder.setNegativeButton(
                    "No",
                    DialogInterface.OnClickListener { dialog, which ->
                        Log.i("Estado", "No se cambio de estado")
                    }
                )
                val dialogo = builder.create()
                dialogo.show()
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun transaccion(estado: String){
        val db = Firebase.firestore
        val referenciaOrden = db.collection("orden").document(arregloDocumentos[posicionItemSelecionado])
        db.runTransaction{ transaccion ->
            val documentoActual = transaccion.get(referenciaOrden)
            val estadovalor = documentoActual.get("estado")
            if(estadovalor != null && estado == "Preparando"){
                val nuevoEstado = estado
                transaccion.update(referenciaOrden,"estado",nuevoEstado)
            }
            if (estadovalor != null && estado == "Cancelado"){
                val nuevoEstado = estado
                transaccion.update(referenciaOrden,"estado",nuevoEstado)
            }
            if (estadovalor != null && estado == "Enviado"){
                val nuevoEstado = estado
                transaccion.update(referenciaOrden,"estado",nuevoEstado)
            }
        }
            .addOnSuccessListener { Log.i("transaccion","Transaccion completada") }
            .addOnFailureListener { Log.i("transaccion","ERROR transaccion") }
    }

    fun cargarFiltro(){
        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add("Por Recibir")
        spinnerArray.add("Preparando")
        spinnerArray.add("Enviado")
        spinnerArray.add("Entregado")
        spinnerArray.add("Cancelado")
        val adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, spinnerArray
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val sItems = findViewById<Spinner>(R.id.sp_filtro)
        sItems.adapter = adapter
    }
}