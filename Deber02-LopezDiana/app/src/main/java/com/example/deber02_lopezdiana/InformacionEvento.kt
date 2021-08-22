package com.example.deber02_lopezdiana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import org.w3c.dom.Text
import java.util.concurrent.TimeoutException
import kotlin.math.abs

class InformacionEvento : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_evento)

        //Obtener la información
        val evento = intent.getParcelableExtra<Evento>("Evento")

        //Obtener los EditText e ImageView

        val imgEvento = findViewById<ImageView>(R.id.img_FotoEventoInfo)
        val fechaEvento = findViewById<TextView>(R.id.txt_FechaEventoInfo)
        val tituloEvento = findViewById<TextView>(R.id.txt_TituloEventoInfo)
        val segIntComEvento = findViewById<TextView>(R.id.txt_SegIntCompInfo)
        val autorEvento = findViewById<TextView>(R.id.txt_AutorEventoInfo)

        //Colocar datos

        val imagen = this.resources.getIdentifier(
            evento?.imagenEvento, "drawable", this.packageName
        )
        imgEvento.setImageResource(imagen)
        fechaEvento.setText(evento?.fechaEvento.toString())
        tituloEvento.setText(evento?.tituloEvento.toString())
        segIntComEvento.setText("${evento?.seguidoresEventos.toString()} seguidores  ·" +
                " ${evento?.interesadosEventos.toString()} interesados  · " +
                "${evento?.compartidosEventos.toString()} comparticiones ")
        autorEvento.setText("Evento de ${evento?.autorEvento.toString()}")

        //Regresar

        val imgRegresar = findViewById<ImageView>(R.id.img_regresar)
        imgRegresar.setOnClickListener {
            abrirActividadDeEvento()
        }


    }

    fun abrirActividadDeEvento(){
        val intentExplicito = Intent(
            this,
            MainActivity::class.java
        )
        startActivity(intentExplicito)
    }
}