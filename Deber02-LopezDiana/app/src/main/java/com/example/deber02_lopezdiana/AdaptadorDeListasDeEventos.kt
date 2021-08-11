package com.example.deber02_lopezdiana

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdaptadorDeListasDeEventos(
    private val contexto: MainActivity,
    private val lista: List<ListasEventos>,
    private val recyclerView: RecyclerView,
) : RecyclerView.Adapter<AdaptadorDeListasDeEventos.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloDeListaTextView : TextView
        val descripcionListaTextView : TextView
        val listaDeEventosRecyclerView : RecyclerView

        init{
            tituloDeListaTextView = view.findViewById(R.id.txt_tituloLista)
            listaDeEventosRecyclerView = view.findViewById(R.id.rv_eventos)
            descripcionListaTextView = view.findViewById(R.id.txt_descripcion)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorDeListasDeEventos.MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.listas_eventos,
                parent,
                false
            )
        return  MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdaptadorDeListasDeEventos.MyViewHolder, position: Int) {
        val listaE = lista[position]
        holder.tituloDeListaTextView.text = listaE.nombreLista
        holder.descripcionListaTextView.text = listaE.descripcion
        iniciarRecyclerView(listaE.sublistaEvento,contexto, holder.listaDeEventosRecyclerView)

    }

    override fun getItemCount(): Int {
        return lista.size
    }

    fun iniciarRecyclerView(
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
}
