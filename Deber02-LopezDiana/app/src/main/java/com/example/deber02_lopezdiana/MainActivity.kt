package com.example.deber02_lopezdiana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Lista de eventos
        val listaEvento = inicializarEventos()

        //Crear lista

        val recyclerViewEvento = findViewById<RecyclerView>(R.id.rv_eventos)

        iniciarRV(
            listaEvento,
            this,
            recyclerViewEvento
        )


    }

    fun iniciarRV(
        lista: List<Evento>,
        actividad: MainActivity,
        recyclerView: RecyclerView
    ){
        val adaptador = AdaptadorEventos(
            actividad,
            lista,
            recyclerView
        )
        recyclerView.adapter = adaptador
        val layoutManager =
            LinearLayoutManager(actividad, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        adaptador.notifyDataSetChanged()

    }

    fun inicializarEventos() : ArrayList<Evento>{
        var listaEventos = arrayListOf<Evento>()

        listaEventos.add(Evento(1, "LUN., 9 AGO. A LAS 11:00 UTC-05",
            "Qué estudiar para el ICFES y lograr un alto puntaje", "Preuniversitario Ingrese a la Universidad",
            false,"evento2"
        ))
        listaEventos.add(Evento(2, "LUN., 9 AGO. A LAS 11:00 UTC-05",
            "Maraton 4k por mi sueño :V", "Juan Perez", false,
            "evento1"
        ))

        return listaEventos
    }
}