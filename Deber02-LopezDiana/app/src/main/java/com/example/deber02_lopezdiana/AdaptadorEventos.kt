package com.example.deber02_lopezdiana

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorEventos(
    private val contexto: MainActivity,
    private val listaEventos: List<Evento>,
    private val recyclerView: RecyclerView,
) : RecyclerView.Adapter<AdaptadorEventos.MyViewHolder>(){
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imgEventoImageView: ImageView
        val fechaEventoTextView : TextView
        val tituloEventoTextView : TextView
        val autorEventoTextView: TextView
        var meInteresaButton : Button

        init{
            imgEventoImageView = view.findViewById(R.id.img_fotoEvento)
            fechaEventoTextView = view.findViewById(R.id.txt_fecha)
            tituloEventoTextView = view.findViewById(R.id.txt_titulo)
            autorEventoTextView = view.findViewById(R.id.txt_autor)
            meInteresaButton = view.findViewById(R.id.btn_meInteresa)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.recycler_view_eventos,
                parent,
                false
            )
        return  MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val evento = listaEventos[position]
        holder.fechaEventoTextView.text = evento.fechaEvento
        holder.tituloEventoTextView.text = evento.tituloEvento
        holder.autorEventoTextView.text = evento.autorEvento
        val imagen = this.contexto.resources.getIdentifier(
            evento.imagenEvento, "drawable", this.contexto.packageName
        )
        holder.imgEventoImageView.setImageResource(imagen)

    }

    override fun getItemCount(): Int {
        return listaEventos.size
    }

}