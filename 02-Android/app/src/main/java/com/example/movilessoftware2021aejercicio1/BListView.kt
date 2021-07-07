package com.example.movilessoftware2021aejercicio1

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

class BListView : AppCompatActivity() {

    var posicionItemSelecionado = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blist_view)

        //Creamos el arreglo
        val arregloNumero = BBaseDatosMemoria.arregloBEntrenador


        //creamos el adaptador
        val adaptador = ArrayAdapter(
            this, //contexto
            android.R.layout.simple_list_item_1, //Layout (visual)
            arregloNumero //arreglo por default
        )

        //Asignamos a la lista el adaptador
        val listViewEjemplo = findViewById<ListView>(R.id.txv_ejemplo)
        listViewEjemplo.adapter = adaptador

        //Indicamos la acción del botón
        val botonListView = findViewById<Button>(R.id.btn_list_view_anadir)
        botonListView
            .setOnClickListener{
                anadirItemsAlListView(BEntrenador("Ana","asd@a.com"),
                    arregloNumero, adaptador)
            }

        //Indica si un ítem fue presionado por un tiempo
 //       listViewEjemplo
 //           .setOnItemLongClickListener{ adapterView, view, posicion, id ->
 //               Log.i("list-view", "Dio click en la posición ${posicion}")
  //              return@setOnItemLongClickListener true
  //          }

        //Registar el menú que se creo
        registerForContextMenu(listViewEjemplo)

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
        Log.i("List-View","Entrenador: ${BBaseDatosMemoria.arregloBEntrenador[id]}")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId){ // el ? es para que no se caiga el aplicativo por no haber puesto el id
            //Editar
            R.id.mi_editar -> {
                Log.i("List-View", "Editar: ${BBaseDatosMemoria.arregloBEntrenador[posicionItemSelecionado]}")
                return true
            }
            //Eliminar
            R.id.mi_eliminar -> {
                Log.i("List-View", "Eliminar: ${BBaseDatosMemoria.arregloBEntrenador[posicionItemSelecionado]}")
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun anadirItemsAlListView(
        valor : BEntrenador,
        arreglo: ArrayList<BEntrenador>,
        adaptador: ArrayAdapter<BEntrenador>
    ){
        arreglo.add(valor)
        //Escucha los cambios y los muestra en pantalla
        adaptador.notifyDataSetChanged()
    }

}