package com.example.examen01_lopezdiana

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

    //Variable para saber la posicion de una papelería
    var posicionItemSelecionado = 0
    //Variable para el uso de la BD
    var baseDatos = ESqliteHelper(this)
    //Envio de datos a otra Actividad
    val CODIGO_REPUESTA_INTENT_EXPLICITO = 100

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Abrir Actividad CrearPapeleria
        val botonAgregarPapeleria = findViewById<Button>(R.id.btn_AgregarPapelería)
        botonAgregarPapeleria.setOnClickListener {
            abrirActividad(CrearPapeleria::class.java)
        }

        //Llenar el ListViewy la manipulación de sus menús contextuales
        llenarListView()
    }

    fun llenarListView(){

        // Obtenemos la lista de papelerías registradas
        val arreglo = baseDatos.consultarPapelerias()

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

    //Abrir una Actividad sin necesidad de mandar parámetros
    fun abrirActividad(
        clase : Class<*>
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        startActivity(intentExplicito)
    }

    //Abrir una Actividad mandando una papelería
    fun abrirActividadConParametros(
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
        // Almacenar cual papelería seleccionamos
        posicionItemSelecionado = id
    }

    //Dar funcionalidad a los ítems de la lista seleccionado
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId){
            //Visualizar articulos de una papelería
            R.id.mi_visualizar -> {

                // Obtener el objeto y mandarlo a la actividad de visualizar los artículos
                val lista = baseDatos.consultarPapelerias()
                val papeleria = lista[posicionItemSelecionado]

                abrirActividadConParametros(VisualizarArticulosPorPapeleria::class.java,papeleria)

                return true
            }
            //Eliminar papelería
            R.id.mi_borrarPapeleria -> {

                //Obtener el id de la papelería a eliminar
                val lista = baseDatos.consultarPapelerias()
                val id = lista[posicionItemSelecionado].idPapeleria
                //Eliminar
                baseDatos.eliminarPapeleriaFormulario(id)
                //Actualizar la vista
                llenarListView()

                return true
            }
            //Actualizar papelería
            R.id.mi_actualizarPapeleria ->{

                //Obtener el objeto papelería a actualizar y mandarlo a la actividad
                val lista = baseDatos.consultarPapelerias()
                val papeleria = lista[posicionItemSelecionado]

                abrirActividadConParametros(ActualizarPapeleria::class.java, papeleria)

                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

}