package com.example.a01_trabajoenclases


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.content.contentValuesOf

class ESqliteHelperArticulo (
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
                CREATE TABLE ARTICULOS(
                    id INTEGER PRIMARY KEY AUTOINCREMENT, 
                    nombre VARACHAR(50),
                    precio DECIMAL(3,2),
                    cantidad int
                )
            """.trimIndent()
        Log.i("bbd","Creando la tabla de articulos")
        db?.execSQL(scriptCrearTablaUsuario)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun crearArticuloFormulario(
        nombre: String,
        precio: Double,
        cantidad: Int
    ): Boolean {
        val conexionExcritura = writableDatabase

        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre",nombre)
        valoresAGuardar.put("precio", precio)
        valoresAGuardar.put("cantidad",cantidad)

        val resultadoEscritura: Long = conexionExcritura
            .insert(
                "ARTICULOS",
                null,
                valoresAGuardar
            )
        conexionExcritura.close()
        return if (resultadoEscritura.toInt() == -1) false else true
    }

    fun consultarArticuloPorId(id: Int): Articulo {
        val scriptConsultarUsuario = "SELECT * FROM ARTICULOS WHERE ID =${id}"
        //Solo abrimos en modo lectura
        val baseDatosLectura = readableDatabase

        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultarUsuario,
            null
        )

        val existeUsuario = resultadoConsultaLectura.moveToFirst()

        val articuloEncontrado = Articulo (0, "",0.00,0)

        if(existeUsuario){
            do {
                var id = resultadoConsultaLectura.getInt(0) //Columna índice 0
                var nombre = resultadoConsultaLectura.getString(1) //Columna índice 1
                var precio = resultadoConsultaLectura.getDouble(2) //Columna índice 2
                var cantidad = resultadoConsultaLectura.getInt(3) //Columna índice 3

                if (id != null){
                    articuloEncontrado.id = id
                    articuloEncontrado.nombreArticulo = nombre
                    articuloEncontrado.precio = precio
                    articuloEncontrado.cantidad = cantidad
                }
            }while(resultadoConsultaLectura.moveToNext())
        }

        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return articuloEncontrado
    }

    fun consultarArticulos(): List<Articulo> {
        val scriptConsultarUsuario = "SELECT * FROM ARTICULOS"
        //Solo abrimos en modo lectura
        val baseDatosLectura = readableDatabase

        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultarUsuario,
            null
        )

        val existeArticulo = resultadoConsultaLectura.moveToFirst()

        val articulosEncontrado = arrayListOf<Articulo>()

        if(existeArticulo){
            do {
                var id = resultadoConsultaLectura.getInt(0) //Columna índice 0
                var nombre = resultadoConsultaLectura.getString(1) //Columna índice 1
                var precio = resultadoConsultaLectura.getDouble(2) //Columna índice 2
                var cantidad = resultadoConsultaLectura.getInt(3) //Columna índice 3

                if (id != null){
                    articulosEncontrado.add(
                        Articulo(id,nombre,precio,cantidad)
                     )
                }
            }while(resultadoConsultaLectura.moveToNext())
        }

        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return articulosEncontrado
    }

    fun eliminarArticuloFormulario(id: Int): Boolean{

        val conexionEscritura = writableDatabase

        val resultadoEliminacion = conexionEscritura
            .delete(
                "ARTICULOS", //Tabla
                "id=?", //Clausura Where
                arrayOf(
                    id.toString(),
                ) //Arreglo ordenado de parametros
            )
        conexionEscritura.close()
        return if (resultadoEliminacion.toInt() == -1) false else true

    }

    fun actualizarArticuloFormulario(
        nombre: String,
        precio: Double,
        cantidad: Int,
        idActualizar: Int
    ): Boolean{
        val conexionEscritura = writableDatabase

        val valoresActualizar = ContentValues()
        valoresActualizar.put("nombre", nombre)
        valoresActualizar.put("precio", precio)
        valoresActualizar.put("cantidad", cantidad)

        val resultadoActualizacion = conexionEscritura
            .update(
                "ARTICULOS", //Nombre de la tabla
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








