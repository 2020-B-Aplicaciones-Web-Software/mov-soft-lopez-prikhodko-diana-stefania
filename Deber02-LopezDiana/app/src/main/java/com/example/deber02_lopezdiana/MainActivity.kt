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

        val recyclerViewEvento = findViewById<RecyclerView>(R.id.rv_listas)

        iniciarRV(
            listaEvento,
            this,
            recyclerViewEvento
        )


    }

    fun iniciarRV(
        lista: List<ListasEventos>,
        actividad: MainActivity,
        recyclerView: RecyclerView
    ){
        val adaptador = AdaptadorDeListasDeEventos(
            actividad,
            lista,
            recyclerView
        )
        recyclerView.adapter = adaptador
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(actividad)
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        adaptador.notifyDataSetChanged()

    }

    fun inicializarEventos() : ArrayList<ListasEventos>{
        var eventos = arrayListOf<Evento>()
        var listaDeEventos = arrayListOf<ListasEventos>()

        eventos.add(Evento(1, "7 DE OCT., 15:00 UTC-05 – 12 DE OCT., 15:00 UTC-05",
            "CURSO: QUEMADURAS, HERIDAS Y OSTOMÍAS", "Facmed Ecuador",
            false,"evento1"
        ))
        eventos.add(Evento(2, "12 DE AGO., 15:00 UTC-05 – 17 DE AGO., 15:00 UTC-05",
            "Curso Taller: Administración de Fármacos ・Dilución y Cálculo de Dosis", "Facmed Ecuador", false,
            "evento2"
        ))
        eventos.add(Evento(3, "HOY A LAS 12:00 UTC-05",
            "Medicina ancestral: La herbolaria, medicina antigua y tradicional.", "Secretaría de Desarrollo Humano",
            false,"evento3"
        ))
        eventos.add(Evento(4, "12 DE AGO., 15:00 UTC-05 – 17 DE AGO., 15:00 UTC-05",
            "Curso: Manejo del Paciente Politraumatizado", "Facmed Ecuador", false,
            "evento4"
        ))
        listaDeEventos.add(ListasEventos("Salud y Bienestar", "Eventos sugeridos", eventos))

        var eventos2 = arrayListOf<Evento>()

        eventos2.add(Evento(5, "MAÑANA DE 17:15 UTC-05 A 19:45 UTC-05",
            "Partido Liga de Quito vs Paranaense (BRA)", "Estadio de Liga Deportiva Universitaria",
            false,"evento6"
        ))
        eventos2.add(Evento(6, "VIERNES, 20 DE AGOSTO DE 20:00 UTC-05 A 22:00 UTC-05",
            "1° Curso De Enseñanza de Jiu Jitsu Y Defensa Personal", "Samurai - Artes Marciales - Cuenca", false,
            "evento5"
        ))
        listaDeEventos.add(ListasEventos("Deportes", "Eventos cerca", eventos2))

        var eventos3 = arrayListOf<Evento>()

        eventos3.add(Evento(7, "JUEVES, 12 DE AGOSTO DE 2021 A LAS 18:55 UTC-05",
            "Taller Gratuito de COMMUNITY MANAGER", "Facebook Live",
            false,"evento7"
        ))
        eventos3.add(Evento(8, "10 DE AGO., 18:00 UTC-05 – 14 DE AGO., 18:00 UTC-05",
            "JS Workshop | Semana de JavaScript", "Evento online", false,
            "evento8"
        ))
        listaDeEventos.add(ListasEventos("Cursos Online", "Eventos sugeridos", eventos3))

        return listaDeEventos
    }
}