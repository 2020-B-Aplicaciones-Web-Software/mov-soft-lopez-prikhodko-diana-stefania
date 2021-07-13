package com.example.a01_trabajoenclases

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class ActualizarArticulo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_articulo)

        var baseDatos = ESqliteHelperArticulo(this)

        val articulo = intent.getParcelableExtra<Articulo>("articulo")

        val id = articulo?.id!!.toInt()
        val nombre = findViewById<EditText>(R.id.box_nombre3)
        val precio = findViewById<EditText>(R.id.box_precio2)
        val cantidad = findViewById<EditText>(R.id.box_cantidad2)
        nombre.setText(articulo?.nombreArticulo.toString())
        precio.setText(articulo?.precio.toString())
        cantidad.setText(articulo?.cantidad.toString())

        val botonActualizar = findViewById<Button>(R.id.btn_actualizar)
        botonActualizar
            .setOnClickListener {

                val nombreEdit = findViewById<EditText>(R.id.box_nombre3)
                val precioEdit  = findViewById<EditText>(R.id.box_precio2)
                val cantidadEdit = findViewById<EditText>(R.id.box_cantidad2)

                val nombre = nombreEdit.text.toString()
                val cantidad = cantidadEdit.text.toString().toInt()
                val precio = precioEdit.text.toString().toDouble()

                baseDatos.actualizarArticuloFormulario(nombre,precio,cantidad,id)

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