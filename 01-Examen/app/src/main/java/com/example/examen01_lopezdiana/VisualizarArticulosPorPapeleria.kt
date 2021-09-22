package com.example.examen01_lopezdiana

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
import com.example.examen01_lopezdiana.entities.Articulo
import com.example.examen01_lopezdiana.entities.Papeleria

class VisualizarArticulosPorPapeleria : AppCompatActivity() {

    //Variable para saber la posicion de una artículo
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
        //llenarListView(papeleriaIntent!!.idPapeleria)

        //Crear artículo en esa papelería
        val botonCrearProductos = findViewById<Button>(R.id.btn_AgregarArticulos)
        botonCrearProductos.setOnClickListener {
         //   abrirActividadConParametros1(CrearArticulos::class.java, papeleriaIntent)
        }

        //Regresar de pantalla de visualizar papelerías
        val botonRegresar = findViewById<Button>(R.id.btn_regresarVisualizarArticulo)
        botonRegresar.setOnClickListener {
            abrirActividad(MainActivity::class.java)
        }
    }

    fun llenarListView(idPapeleria: Int){

        //Buscar artículos de la papelería seleccionada
        val arreglo = baseDatos.consultarArticulos(idPapeleria)
        //Creamos el adaptador
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arreglo
        )

        //Asignamos a la lista el adaptador
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

    //Dar funcionalidad a los ítems de la lista seleccionado
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId){

            //Actualizar Artículo seleccionado
            R.id.mi_actualizarArticulo -> {

                //Papelería actual
                val papeleriaIntent = intent.getParcelableExtra<Papeleria>("papeleria")

                //Articulo seleccionado
                //val lista = baseDatos.consultarArticulos(papeleriaIntent!!.idPapeleria)
              ///  val producto = lista[posicionItemSelecionado]

                //Envío de los 2 objetos
              //  abrirActividadConParametros(ActualizarArticulo::class.java,papeleriaIntent,producto)

                return true
            }
            //Eliminar Artículo seleccionado
            R.id.mi_borrarArticulo -> {

                // Papelería actual
                val papeleriaIntent = intent.getParcelableExtra<Papeleria>("papeleria")

                //Artículo seleccionado
               // val lista = baseDatos.consultarArticulos(papeleriaIntent!!.idPapeleria)
           //     val id = lista[posicionItemSelecionado].idArticulo

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Eliminación")
                builder.setMessage("¿Deseas eliminar el artículo?")
                builder.setPositiveButton(
                    "Si",
                    DialogInterface.OnClickListener{ dialog, which ->
                        //Eliminar Artículo
                    //    baseDatos.eliminarArticuloFormulario(id)
                        //Actualizar la vista
                        //llenarListView(papeleriaIntent.idPapeleria)
                    }
                )
                builder.setNegativeButton(
                    "No",
                    DialogInterface.OnClickListener{ dialog, which ->
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

    //Abrir una Actividad mandando una papelería y un artículo
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

    //Abrir una Actividad mandando una papelería
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

    //Abrir una Actividad sin necesidad de mandar parámetros
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