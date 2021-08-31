package com.example.firebaseuno

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class EOrdenes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eordenes)
        obtenerRestaurantes()


    }

    fun obtenerRestaurantes(){

        val spinnerArray: MutableList<String> = ArrayList()

        val db = Firebase.firestore
        val datos = db.collection("restaurante").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val dato = document.data.values.toString().replace("[","").replace("]","")
                    spinnerArray.add(dato)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("firebasedata", "Error getting documents: ", exception)
            }

        val adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, spinnerArray
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val sItems = findViewById<Spinner>(R.id.sp_restaurantes)
        sItems.adapter = adapter
       }
}
