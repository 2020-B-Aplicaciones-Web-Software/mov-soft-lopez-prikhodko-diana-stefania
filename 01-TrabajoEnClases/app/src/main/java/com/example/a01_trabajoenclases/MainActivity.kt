package com.example.a01_trabajoenclases

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

class MainActivity : AppCompatActivity() {

    var posicionItemSelecionado = 0
    var baseDatos = ESqliteHelperArticulo(this)
    val CODIGO_REPUESTA_INTENT_EXPLICITO = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Visualizar
        llenarView()

        //Agregar nuevo artículo

        val botonAgregarProducto = findViewById<Button>(R.id.btn_anadir)
        botonAgregarProducto
            .setOnClickListener {
                abrirNuevaVentana(AgregarArticulo::class.java)
            }

    }


    fun llenarView(){
        val arreglo = baseDatos.consultarArticulos()
        //Creamos el adaptador
        val adaptador = ArrayAdapter(
            this, //contexto
            android.R.layout.simple_list_item_1, //Layout (visual)
            arreglo //arreglo por default
        )

        //Asignamos a la lista el adaptador
        val listView= findViewById<ListView>(R.id.listview_articulos)
        listView.adapter = adaptador

        //Entregamos la lista contextual
        registerForContextMenu(listView)
    }

    fun abrirNuevaVentana(
        clase : Class<*>
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        startActivity(intentExplicito)
    }

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
        Log.i("List-View","List view ${posicionItemSelecionado}")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId){ // el ? es para que no se caiga el aplicativo por no haber puesto el id
            //Editar
            R.id.mi_editar -> {
                val lista = baseDatos.consultarArticulos()
                val articulo = lista[posicionItemSelecionado]
                abrirActividadConParametros(ActualizarArticulo::class.java,articulo)
                return true
            }
            //Eliminar
            R.id.mi_borrar -> {
                val lista = baseDatos.consultarArticulos()
                val id = lista[posicionItemSelecionado].id
                baseDatos.eliminarArticuloFormulario(id)
                llenarView()
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun abrirActividadConParametros(
        clase : Class<*>,
        articulo: Articulo
    ){
        val intentExplicito = Intent(
            this,
            clase
        )

        intentExplicito.putExtra("articulo",articulo)
        startActivityForResult(intentExplicito, CODIGO_REPUESTA_INTENT_EXPLICITO)
    }


}