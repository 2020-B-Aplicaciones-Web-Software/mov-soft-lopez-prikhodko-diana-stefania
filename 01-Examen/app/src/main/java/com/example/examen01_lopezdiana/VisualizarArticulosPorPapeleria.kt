package com.example.examen01_lopezdiana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*

class VisualizarArticulosPorPapeleria : AppCompatActivity() {

    //Variable para saber la posicion de una papelería
    var posicionItemSelecionado = 0
    //Variable para el uso de la BD
    var baseDatos = ESqliteHelper(this)
    //Envio de datos a otra Actividad
    val CODIGO_REPUESTA_INTENT_EXPLICITO = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_articulos_por_papeleria)

        // Papelería actual
        val papeleriaIntent = intent.getParcelableExtra<Papeleria>("papeleria")

        //Ver el título con el nombre de la papelería

        val tituloTextView = findViewById<TextView>(R.id.tv_tituloMenuPapeleriaProducto)
        tituloTextView.setText(papeleriaIntent?.nombrePapeleria.toString())

        //Llenar List View
        llenarListView(papeleriaIntent!!.idPapeleria)

        //Crear Productos

        val botonCrearProductos = findViewById<Button>(R.id.btn_AgregarArticulos)
        botonCrearProductos.setOnClickListener {
            abrirActividadConParametros1(CrearArticulos::class.java, papeleriaIntent)
        }

        //Regresar de pantalla
        val botonRegresar = findViewById<Button>(R.id.btn_regresarVisualizarArticulo)
        botonRegresar.setOnClickListener {
            abrirActividad(MainActivity::class.java)
        }

    }

    fun llenarListView(idPapeleria: Int){
        val arreglo = baseDatos.consultarArticulos(idPapeleria)
        //Creamos el adaptador
        val adaptador = ArrayAdapter(
            this, //contexto
            android.R.layout.simple_list_item_1, //Layout (visual)
            arreglo //arreglo por default
        )

        //Asignamos a la lista el adaptador
        val listView= findViewById<ListView>(R.id.lv_productos)
        listView.adapter = adaptador

        //Entregamos la lista contextual
        registerForContextMenu(listView)
    }
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

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId){ // el ? es para que no se caiga el aplicativo por no haber puesto el id
            //Actualizar
            R.id.mi_actualizarArticulo -> {

                // Papelería actual
                val papeleriaIntent = intent.getParcelableExtra<Papeleria>("papeleria")

                val lista = baseDatos.consultarArticulos(papeleriaIntent!!.idPapeleria)
                val producto = lista[posicionItemSelecionado]
                abrirActividadConParametros(ActualizarArticulo::class.java,papeleriaIntent,producto)

                return true
            }
            //Eliminar
            R.id.mi_borrarArticulo -> {

                // Papelería actual
                val papeleriaIntent = intent.getParcelableExtra<Papeleria>("papeleria")

                val lista = baseDatos.consultarArticulos(papeleriaIntent!!.idPapeleria)
                val id = lista[posicionItemSelecionado].idArticulo
                baseDatos.eliminarArticuloFormulario(id)
                llenarListView(papeleriaIntent.idPapeleria)

                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    fun abrirActividadConParametros(
        clase : Class<*>,
        papeleria: Papeleria,
        articulo: Articulo
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        intentExplicito.putExtra("papeleria", papeleria)
        intentExplicito.putExtra("articulo", articulo)
        startActivityForResult(intentExplicito, CODIGO_REPUESTA_INTENT_EXPLICITO)
    }
    fun abrirActividadConParametros1(
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
    fun abrirActividad(
        clase: Class<*>
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        startActivity(intentExplicito)
    }
}