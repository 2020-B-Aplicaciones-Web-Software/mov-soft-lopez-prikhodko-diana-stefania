package com.example.firebaseuno

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DCrearRestaurante : AppCompatActivity() {

    var query: Query? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drestaurante)

        val botonCrearRestaurante = findViewById<Button>(R.id.btn_crear_restaurante)
        botonCrearRestaurante.setOnClickListener{
            crearResturante()
        }
/*
        val botonDatosPrueba = findViewById<Button>(R.id.btn_datos_prueba)
        botonDatosPrueba.setOnClickListener{
            transaccion()
        }

        val botonConsultar = findViewById<Button>(R.id.btn_consultar)
        botonConsultar.setOnClickListener{
            consultar()
        }*/
    }

    fun transaccion(){
        val db = Firebase.firestore
        val referenciaCities = db.collection("cities").document("SF")

        db.runTransaction{ transaccion ->
            val documentoActual = transaccion.get(referenciaCities)
            val poblacion = documentoActual.getDouble("population")
            if(poblacion != null){
                val nuevaPoblacion = poblacion + 1
                transaccion.update(referenciaCities,"population",nuevaPoblacion)
            }
        }
            .addOnSuccessListener { Log.i("transaccion","Transaccion completada") }
            .addOnFailureListener { Log.i("transaccion","ERROR transaccion") }
    }

    fun consultar(){

        val db = Firebase.firestore
        val citiesRef = db.collection("cities")
            .orderBy("population")
            .limit(2)

        var tarea: Task<QuerySnapshot>? = null

        if (query == null){
            tarea = citiesRef.get()
        } else {
            tarea = query!!.get()
        }
        if(tarea != null){
            tarea
                .addOnSuccessListener { documentSnapshots ->
                    guardarQuery(documentSnapshots, citiesRef)
                    for(ciudad in documentSnapshots) {
                        Log.i("consultas", "${ciudad.data}")
                    }
                }
                .addOnFailureListener {
                    Log.i("consultas", "${it}")
                }
        }
/*
        //Buscar por dos o mas elementos de campo "==" ">="
        citiesRef
            .whereEqualTo("capital",true)
            .whereGreaterThanOrEqualTo("population",1000000)
            .get()
            .addOnSuccessListener {
                for (ciudad in it){
                    Log.i("ConsultasFB","${ciudad.data}")
                }
            }


        // Busqueda sabiendo en ID del documento
        citiesRef
            .document("BJ")
            .get()
            .addOnSuccessListener {
                    Log.i( "ConsultasFB", "${it.data}")
                }
            .addOnFailureListener {  }
        // Busqueda de todos los documentos
        citiesRef
            .whereEqualTo("country","China")
            .get()
            .addOnSuccessListener {
                Log.i("ConsultasFB","${it.documents}")
                for (ciudad in it){
                    Log.i("ConsultasFB","${ciudad.data}")
                    Log.i("ConsultasFB", "${ciudad.id}")
            }
            }
            .addOnFailureListener{ }

        //Buscar por dos o mas elementos campo "==" "array-contains"

        citiesRef
            .whereEqualTo("capital",false)
            .whereArrayContainsAny("regions", arrayListOf("socal","norcal"))
            .get()
            .addOnSuccessListener {
                for (ciudad in it){
                    Log.i("ConsultasFB", "==array-contains ${ciudad.data}")
                }
            }

        citiesRef
            .whereEqualTo("capital", false)
            .whereLessThanOrEqualTo("population", 4000000)
            .orderBy("population", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for(ciudad in it){
                    Log.i("consultas","${ciudad.data}")
                }
            }

        */
    }

    fun guardarQuery(documentSnapshot: QuerySnapshot, citiesRef : Query){
        if(documentSnapshot.size() > 0){
            val ultimoDocumento = documentSnapshot.documents[documentSnapshot.size() -1]
            query = citiesRef
                .startAfter(ultimoDocumento)
        }else{

        }
    }

    fun crearResturante(){
        //Obtener datos
        val editTextNombre = findViewById<EditText>(R.id.et_nombre_restaurante)

        //Referencia a la base de datos
        val db = Firebase.firestore
        val referencia = db.collection("restaurante")

        //Guardar todos los datos necesarios
        val generatedID = referencia.document()

        val inicializador = 0
        val nuevoRestaurante = hashMapOf<String, Any>(
            "uid" to generatedID.id,
            "nombre" to editTextNombre.text.toString(),
            "calificacionMedia" to inicializador,
            "sumatoria" to inicializador,
            "cantidadUsuarios" to inicializador
        )

        //Almacenar el restaurante
        referencia
            .document(editTextNombre.text.toString())
            .set(nuevoRestaurante)
            .addOnSuccessListener {
                //Cuadro de dialogo
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Creación exitosa del Restaurante")
                builder.setMessage("El restaurante ${editTextNombre.text} ha sido creado exitosamente!")
                builder.setPositiveButton(
                    "Continuar",
                    DialogInterface.OnClickListener{ dialog, which ->
                        Log.i("Dialogo","Creación exitosa del restaurante")
                        editTextNombre.text.clear()
                    }
                )
                val dialogo = builder.create()
                dialogo.show()
            }
            .addOnFailureListener {  }

    }

    fun crearDatos(){
        val db = Firebase.firestore
        val cities = db.collection("cities")

        val data1 = hashMapOf(
            "name" to "San Francisco",
            "state" to "CA",
            "country" to "USA",
            "capital" to false,
            "population" to 860000,
            "regions" to listOf("west_coast", "norcal")
        )
        cities.document("SF").set(data1)

        val data2 = hashMapOf(
            "name" to "Los Angeles",
            "state" to "CA",
            "country" to "USA",
            "capital" to false,
            "population" to 3900000,
            "regions" to listOf("west_coast", "socal")
        )
        cities.document("LA").set(data2)

        val data3 = hashMapOf(
            "name" to "Washington D.C.",
            "state" to null,
            "country" to "USA",
            "capital" to true,
            "population" to 680000,
            "regions" to listOf("east_coast")
        )
        cities.document("DC").set(data3)

        val data4 = hashMapOf(
            "name" to "Tokyo",
            "state" to null,
            "country" to "Japan",
            "capital" to true,
            "population" to 9000000,
            "regions" to listOf("kanto", "honshu")
        )
        cities.document("TOK").set(data4)

        val data5 = hashMapOf(
            "name" to "Beijing",
            "state" to null,
            "country" to "China",
            "capital" to true,
            "population" to 21500000,
            "regions" to listOf("jingjinji", "hebei")
        )
        cities.document("BJ").set(data5)
    }

}