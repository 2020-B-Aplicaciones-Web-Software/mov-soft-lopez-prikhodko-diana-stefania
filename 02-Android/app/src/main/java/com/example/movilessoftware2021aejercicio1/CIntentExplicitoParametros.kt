package com.example.movilessoftware2021aejercicio1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class CIntentExplicitoParametros : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cintent_explicito_parametros)
        //Obtener datos
        val nombre = intent.getStringExtra("nombre")
        val apellido = intent.getStringExtra("apellido")
        val edad = intent.getStringExtra("edad")
        val entrenador = intent.getParcelableExtra<BEntrenador>("entrenador")
        //Imprimir en el LogCat
        Log.i("intent-explicito", "Nombre: ${nombre}")
        Log.i("intent-explicito", "Apellido: ${apellido}")
        Log.i("intent-explicito", "Edad: ${edad}")
        Log.i("intent-explicito", "Entrenador: ${entrenador}")

        val BotonDevolverRespuesta = findViewById<Button>(
            R.id.btn_devolver_respuesta
        )
        BotonDevolverRespuesta
            .setOnClickListener{
                val intentDevolverParametros = Intent()
                intentDevolverParametros.putExtra("nombreModificado", "Juan")
                intentDevolverParametros.putExtra("edadModificado", "52")
                //retornar esultado satisfactoro y los valores
                setResult(RESULT_OK, intentDevolverParametros)
                //cerrar actividad
                finish()
            }
    }
}