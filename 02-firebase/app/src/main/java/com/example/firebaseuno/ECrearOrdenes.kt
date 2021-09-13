package com.example.firebaseuno

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseuno.BAuthUsuario.Companion.usuario
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ECrearOrdenes : AppCompatActivity() {

    //Variable para saber la posicion de una articulo de la orden
    var posicionItemSelecionado = 0

    // Lista de los productos de la orden
    var arregloOrden = arrayListOf<HProducto>()

    // Total de compra
    var totalCompra = 0.00

    // Lista restaurantes
    var arregloRestaurantes = arrayListOf<IRestaurante>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eordenes)

        // Llenar los Spinners
        obtenerRestaurantes()
        obtenerProductos()

        // Inicializar la suma en 0
        val totalEditText = findViewById<TextView>(R.id.tv_totalOrden)
        totalEditText.text = " $ ${totalCompra}"

        // Crear la lista vacía
        llenarListView()

        // Añadir al hacer clic en el boton
        val botonAniadir = findViewById<Button>(R.id.btn_anadir_lista_producto)
        botonAniadir.setOnClickListener {
            aniadirProducto()
        }

        //Crear orden
        val botonCrearOrden = findViewById<Button>(R.id.btn_completar_pedido)
        botonCrearOrden.setOnClickListener {
            crearOrden()
        }
    }

    fun crearOrden() {
        //Obtener Datos
        var restauranteSeleccionado = IRestaurante("", "", 0.0)
        val restauranteSpinnerText =
            findViewById<Spinner>(R.id.sp_restaurantes).selectedItem.toString()
        for (restaurante in arregloRestaurantes) {
            if (restaurante.nombre == restauranteSpinnerText) {
                restauranteSeleccionado = restaurante
            }
        }
        val formatoFecha = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val fechaActual = formatoFecha.format(Date())


        //Datos a enviar a almacenar
        val restauranteAgregar = hashMapOf<String, Any>(
            "uid" to restauranteSeleccionado.uid,
            "nombre" to restauranteSeleccionado.nombre,
            "calificacionPromedio" to restauranteSeleccionado.calificacionPromedio
        )
        val nuevaOrden = hashMapOf<String, Any>(
            "fechaPedido" to fechaActual,
            "total" to totalCompra,
            "usuario" to BAuthUsuario.usuario!!.email,
            "estado" to "Por Recibir",
            "restaurante" to restauranteAgregar,
            "productos" to arregloOrden,
            "calificacion" to 0
        )
        //Referencia a la base de datos
        val db = Firebase.firestore
        val referencia = db.collection("orden")

        referencia
            .add(nuevaOrden)
            .addOnSuccessListener {
                //Cuadro de dialogo
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Creación exitosa de la Orden")
                builder.setMessage("En el restaurante \"${restauranteSeleccionado.nombre}\" ha realizado su orden!")
                builder.setPositiveButton(
                    "Continuar",
                    DialogInterface.OnClickListener{ dialog, which ->
                        Log.i("Dialogo","Creación exitosa de la orden")
                        arregloOrden = arrayListOf<HProducto>()
                        val cantidadEditText = findViewById<EditText>(R.id.et_cantidad_producto).text.clear()
                        actualizarTotal()
                        llenarListView()
                    }
                )
                val dialogo = builder.create()
                dialogo.show()
            }
            .addOnFailureListener {  }
    }

    fun aniadirProducto() {

        // Crear el adaptador
        val adaptador = ArrayAdapter(
            this, //contexto
            android.R.layout.simple_list_item_1, //Layout (visual)
            arregloOrden //arreglo por default
        )

        //Obtener Datos
        val cantidad = findViewById<EditText>(R.id.et_cantidad_producto).text.toString().toInt()
        val productoSpinner = findViewById<Spinner>(R.id.sp_producto)
        val productoText = productoSpinner.selectedItem.toString()

        //Comprobar que no este vacio el campo
        if (cantidad > 0) {

            //Realizar la consulta
            val db = Firebase.firestore
            db.collection("producto").whereEqualTo("nombre", productoText)
                .get()
                .addOnSuccessListener { result ->
                    var precio = 0.0
                    var uid = ""
                    for (document in result) {
                        //Obtener el costo
                        precio = document.data.getValue("precio").toString().toDouble()
                        uid = document.data.getValue("uid").toString()
                    }
                    Log.i("consultas", "Fila: ${uid} - ${productoText} - ${cantidad} - ${precio}")
                    aniadirProductoEnListView(
                        HProducto(uid, productoText, cantidad, precio),
                        arregloOrden,
                        adaptador
                    )
                    actualizarTotal()
                }
                .addOnFailureListener { exception ->
                    Log.d("firebasedata", "Error getting documents: ", exception)
                }
        } else {
            //Cuadro de dialogo
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Problema con los datos")
            builder.setMessage("Debe ingresar una cantidad mayor a 0")
            builder.setPositiveButton(
                "Si",
                DialogInterface.OnClickListener { dialog, which ->
                    Log.i("List-view", "0k")
                }
            )
            val dialogo = builder.create()
            dialogo.show()
        }
    }

    fun actualizarTotal() {
        totalCompra = 0.00
        for (producto: HProducto in arregloOrden) {
            totalCompra += (producto.cantidad * producto.precio)
        }
        val totalEditText = findViewById<TextView>(R.id.tv_totalOrden)
        totalEditText.text = " $ ${totalCompra}"
    }

    fun llenarListView() {

        //Creamos el adaptador
        val adaptador = ArrayAdapter(
            this, //contexto
            android.R.layout.simple_list_item_1, //Layout (visual)
            arregloOrden //arreglo por default
        )

        //Asignamos a la lista el adaptador
        val listView = findViewById<ListView>(R.id.lv_productos)
        listView.adapter = adaptador

        //Entregamos la lista contextual
        registerForContextMenu(listView)
    }

    fun aniadirProductoEnListView(
        valor: HProducto,
        arreglo: ArrayList<HProducto>,
        adaptador: ArrayAdapter<HProducto>
    ) {
        // Agregaos y actualizamos
        arreglo.add(valor)
        adaptador.notifyDataSetChanged()

        //Asignamos a la lista el adaptador
        val listView = findViewById<ListView>(R.id.lv_productos)
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
        inflater.inflate(R.menu.menu, menu) //inclusión

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        posicionItemSelecionado = id

    }

    //Dar funcionalidad a los ítems de la lista seleccionado
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            //Eliminar Artículo seleccionado
            R.id.mi_borrar -> {
                //Preguntar si esta seguro de eliminar
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Eliminación")
                builder.setMessage("¿Deseas eliminar el artículo?")
                builder.setPositiveButton(
                    "Si",
                    DialogInterface.OnClickListener { dialog, which ->
                        //Eliminar Artículo
                        arregloOrden.removeAt(posicionItemSelecionado)
                        llenarListView()
                        actualizarTotal()
                    }
                )
                builder.setNegativeButton(
                    "No",
                    DialogInterface.OnClickListener { dialog, which ->
                        Log.i("Creacion", "No se eliminó")
                    }
                )
                val dialogo = builder.create()
                dialogo.show()
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun obtenerRestaurantes() {

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
                val sItems = findViewById<Spinner>(R.id.sp_restaurantes)
                sItems.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.d("firebasedata", "Error getting documents: ", exception)
            }
    }

    fun obtenerProductos() {

        val spinnerArray: MutableList<String> = ArrayList()

        val db = Firebase.firestore
        val dates = db.collection("producto").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    spinnerArray.add(document.data.get("nombre").toString())
                }
                val adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, spinnerArray
                )

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                val sItems = findViewById<Spinner>(R.id.sp_producto)
                sItems.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.d("firebasedata", "Error getting documents: ", exception)
            }
    }
}


