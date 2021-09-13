package com.example.firebaseuno

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CCrearProducto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cproducto)

        val botonCrear = findViewById<Button>(R.id.btn_crear)
        botonCrear.setOnClickListener{
            crearProducto()
        }

    }

    fun crearProducto(){

        //Obtener datos
        val editTextNombre = findViewById<EditText>(R.id.et_nombre_producto)
        val editTextPrecio = findViewById<EditText>(R.id.et_precio_producto)

        //Referencia a la base de datos
        val db = Firebase.firestore
        val referencia = db.collection("producto")
        val generatedID = referencia.document()

        //Obtener todos los datos requeridos
        val nuevoProducto = hashMapOf<String, Any>(
            "uid" to generatedID.id,
            "nombre" to editTextNombre.text.toString(),
            "precio" to editTextPrecio.text.toString().toDouble()
        )

        //Almacenar productos
        referencia
            .document(editTextNombre.text.toString())
            .set(nuevoProducto)
            .addOnSuccessListener {
                //Cuadro de dialogo
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Creación exitosa del Producto")
                builder.setMessage("El producto ${editTextNombre.text} ha sido creado exitosamente!")
                builder.setPositiveButton(
                    "Continuar",
                    DialogInterface.OnClickListener{ dialog, which ->
                        Log.i("Dialogo","Creación exitosa del producto")
                        editTextPrecio.text.clear()
                        editTextNombre.text.clear()
                    }
                )
                val dialogo = builder.create()
                dialogo.show()
            }
            .addOnFailureListener {  }

    }
}