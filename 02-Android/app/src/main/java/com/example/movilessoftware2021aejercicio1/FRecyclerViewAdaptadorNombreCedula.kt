package com.example.movilessoftware2021aejercicio1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class FRecyclerViewAdaptadorNombreCedula (
    private val contexto: GRecyclerView,
    private val listaEntrenador: List<BEntrenador>,
    private val recyclerView : RecyclerView,
) : RecyclerView.Adapter<FRecyclerViewAdaptadorNombreCedula.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        // Lógica del visualizador - como si  trabajaramos en la actividad

        val nombreTextView : TextView
        val cedulaTextView : TextView
        val likesTextView : TextView
        val accionButton : Button
        var numeroLikes = 0

        init{
            nombreTextView = view.findViewById(R.id.tv_nombre)
            cedulaTextView = view.findViewById(R.id.tv_cedula)
            likesTextView = view.findViewById(R.id.tv_likes)
            accionButton = view.findViewById(R.id.btn_dar_like)
            accionButton.setOnClickListener {
                this.anadirLike()
            }
        }

        fun anadirLike(){
            this.numeroLikes = this.numeroLikes + 1
            likesTextView.text = this.numeroLikes.toString()
            contexto.aumentarTotalLikes()
        }

    }

    //Ver el tamaño del arreglo
    override fun getItemCount(): Int {
        return listaEntrenador.size
    }

    //Settear los datos dentro del arreglo en la pantalla por cada iteración
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val entrenador = listaEntrenador[position]
        holder.nombreTextView.text = entrenador.nombre
        holder.cedulaTextView.text = entrenador.descripcion
        holder.accionButton.text = "Like ${entrenador.nombre}"
        holder.likesTextView.text = "0"
    }

    //Set el layout xml que se va a utilizar - que vista a usar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.recycler_view_vista,
                parent,
                false
            )
        return MyViewHolder(itemView)
    }
}
