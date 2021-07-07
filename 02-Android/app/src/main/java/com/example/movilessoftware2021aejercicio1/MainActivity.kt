package com.example.movilessoftware2021aejercicio1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    val CODIGO_REPUESTA_INTENT_EXPLICITO = 401
    val CODIGO_RESPUESTA_INTENT_IMPLICITO = 402

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonIrCicloVida = findViewById<Button>(
            //Buscar indetificador
            R.id.btn_id_ciclo_vida
        )
        botonIrCicloVida
            .setOnClickListener{
                abrirCicloVida(
                ACicloVida::class.java
            )
        }
        val botonIrListView = findViewById<Button>(
            //Buscar indetificador
            R.id.btn_ir_list_view
        )
        botonIrListView
            .setOnClickListener{
                abrirCicloVida(
                    BListView::class.java
                )
            }

        val botonIntentExplicito = findViewById<Button>(
            R.id.btn_ir_intent
        )
        botonIntentExplicito
            .setOnClickListener{
                abrirActividadConParametros(CIntentExplicitoParametros::class.java)
            }

        val botonIntentImplicito = findViewById<Button>(
            R.id.btn_intent_implicito
        )
        botonIntentImplicito
            .setOnClickListener {
                val intentConRespuestaImplicito = Intent(
                    //Abrir un contacto
                    Intent.ACTION_PICK,
                    //Solicitamos el URI
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                )
                startActivityForResult(intentConRespuestaImplicito, CODIGO_RESPUESTA_INTENT_IMPLICITO)
            }

    }





    fun abrirActividadConParametros(
        clase : Class<*>
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        intentExplicito.putExtra("nombre","Adrian")
        intentExplicito.putExtra("apellido","Eguez")
        intentExplicito.putExtra("edad","32")
        intentExplicito.putExtra("entrenador",
            BEntrenador("Juan", "Perez")
            )
        startActivityForResult(intentExplicito, CODIGO_REPUESTA_INTENT_EXPLICITO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CODIGO_REPUESTA_INTENT_EXPLICITO ->{
                if(resultCode == RESULT_OK){
                    Log.i("intent-explicito", "SI SE ACTUALIZO LOS DATOS")
                    if (data != null){
                        val nombre = data.getStringExtra("nombreModificado")
                        val edad = data.getIntExtra("edadModificado", 0)
                        Log.i("intent-explicito", "${nombre}")
                        Log.i("intent-explicito", "${edad}")
                   }

                }
            }
            CODIGO_RESPUESTA_INTENT_IMPLICITO ->{
                if(resultCode == RESULT_OK){
                    if(data != null){
                        val uri: Uri = data.data!!
                        val cursor = contentResolver.query(
                            uri,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                        cursor?.moveToFirst()
                        val indiceTelefono = cursor?.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                        val telefono = cursor?.getString(
                            indiceTelefono!!
                        )
                        cursor?.close()
                        Log.i("resultado", "Telefono: ${telefono}")
                    }
                }
            }
        }
    }

    fun abrirCicloVida(
        clase : Class<*>
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        startActivity(intentExplicito)
    }
}