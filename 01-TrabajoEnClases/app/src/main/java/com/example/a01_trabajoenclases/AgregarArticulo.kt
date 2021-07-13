package com.example.a01_trabajoenclases

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class AgregarArticulo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_actividad)

        var baseDatos = ESqliteHelperArticulo(this)

        val botonAgregar = findViewById<Button>(R.id.btn_agregar)
        botonAgregar
            .setOnClickListener {

                val nombreEdit = findViewById<EditText>(R.id.box_nombre)
                val precioEdit  = findViewById<EditText>(R.id.box_precio)
                val cantidadEdit = findViewById<EditText>(R.id.box_cantidad)

                val nombre = nombreEdit.text.toString()
                val cantidad = cantidadEdit.text.toString().toInt()
                val precio = precioEdit.text.toString().toDouble()

                baseDatos.crearArticuloFormulario(nombre,precio,cantidad)

                abrirNuevaVentana(MainActivity::class.java)
            }
    }

    fun abrirNuevaVentana(
        clase : Class<*>
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        startActivity(intentExplicito)
    }
}