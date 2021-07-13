package com.example.movilessoftware2021aejercicio1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.content.contentValuesOf

class ESqliteHelperUsuario (
    contexto: Context?
): SQLiteOpenHelper (
    contexto,
    "moviles",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptCrearTablaUsuario =
            """
                CREATE TABLE USUARIO(
                    id INTEGER PRIMARY KEY AUTOINCREMENT, 
                    nombre VARACHAR(50),
                    descripcion VARCHAR(50)
                )
            """.trimIndent()
        Log.i("bbd","Creando la tabla de usuario")
        db?.execSQL(scriptCrearTablaUsuario)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun crearUsuarioFormulario(
        nombre: String,
        descripcion: String
    ): Boolean{
        val conexionExcritura = writableDatabase

        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre",nombre)
        valoresAGuardar.put("descripcion",descripcion)

        val resultadoEscritura: Long = conexionExcritura
            .insert(
                "USUARIO",
                null,
                 valoresAGuardar
            )
        conexionExcritura.close()
        return if (resultadoEscritura.toInt() == -1) false else true
    }

    fun consultarUsuarioPorId(id: Int): EUsuarioBDD{
        val scriptConsultarUsuario = "SELECT * FROM USUARIO WHERE ID =${id}"
        //Solo abrimos en modo lectura
        val baseDatosLectura = readableDatabase

        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultarUsuario,
            null
        )

        val existeUsuario = resultadoConsultaLectura.moveToFirst()

        val usuarioEncontrado = EUsuarioBDD(0,"","")
        // val usuarioEncontrado = arrayListof<EUsuarioBDD>()

        if(existeUsuario){
            do {
                var id = resultadoConsultaLectura.getInt(0) //Columna índice 0
                var nombre = resultadoConsultaLectura.getString(1) //Columna índice 1
                var descripcion = resultadoConsultaLectura.getString(3) //Columna índice 0

                if (id != null){
                    //arregloUsuario.add(
                    //    EUsuarioBDD(id,nombre,descripcion)
                   // )
                    usuarioEncontrado.id = id
                    usuarioEncontrado.nombre = nombre
                    usuarioEncontrado.descripcion = descripcion
                }
            }while(resultadoConsultaLectura.moveToNext())
        }

        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return usuarioEncontrado
    }

    fun eliminarUsuarioFormulario(id: Int): Boolean{

        val conexionEscritura = writableDatabase

        val resultadoEliminacion = conexionEscritura
            .delete(
                "USUARIO", //Tabla
                "id=?", //Clausura Where
                arrayOf(
                    id.toString(),
                ) //Arreglo ordenado de parametros
            )
        conexionEscritura.close()
        return if (resultadoEliminacion.toInt() == -1) false else true

    }

    fun actualizarUsuarioFormulario(
        nombre: String,
        descripcion: String,
        idActualizar: Int
    ): Boolean{
        val conexionEscritura = writableDatabase

        val valoresActualizar = ContentValues()
        valoresActualizar.put("nombre", nombre)
        valoresActualizar.put("descripcion", descripcion)

        val resultadoActualizacion = conexionEscritura
            .update(
                "USUARIO", //Nombre de la tabla
                valoresActualizar, //Valores actualizar
                "id=?", //Clausura where
                arrayOf(
                    idActualizar.toString()
                )//Parametros de la clausuara where
            )

        conexionEscritura.close()
        return if (resultadoActualizacion == -1) false else true

    }

}








