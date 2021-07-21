package com.example.examen01_lopezdiana

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class ESqliteHelper (
    contexto: Context?
    ): SQLiteOpenHelper(
    contexto,
    "moviles",
    null,
    1
    ) {
        override fun onCreate(db: SQLiteDatabase?) {

            val scriptCrearTablaPapeleria =
                """
                    CREATE TABLE PAPELERIA(
                        id INTEGER PRIMARY KEY AUTOINCREMENT, 
                        nombre VARCHAR(50),
                        direccion VARCHAR(50),
                        fechaApertura VARCHAR(10),
                        mayorista BOOLEAN
                    )
                """.trimIndent()
            Log.i("bd","Creando la tabla de papeleria")
            db?.execSQL(scriptCrearTablaPapeleria)
            val scriptCrearTablaArticulo =
                """
                CREATE TABLE ARTICULOS(
                    id INTEGER PRIMARY KEY AUTOINCREMENT, 
                    nombre VARACHAR(50),
                    precio DECIMAL(3,2),
                    cantidad int,
                    marca VARCHAR(50),
                    descripcion VARCHAR(100),
                    idPapeleria INTEGER,
                    FOREIGN KEY (idPapeleria) REFERENCES PAPELERIA(id) ON DELETE CASCADE 
                )
            """.trimIndent()
            Log.i("bd","Creando la tabla de articulos")
            db?.execSQL(scriptCrearTablaArticulo)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        }

        fun crearPapeleriaFormulario(
            nombre: String,
            direccion: String,
            fechaApertura: String,
            mayorista: Boolean
        ): Boolean {
            val conexionExcritura = writableDatabase

            val valoresAGuardar = ContentValues()
            valoresAGuardar.put("nombre",nombre)
            valoresAGuardar.put("direccion", direccion)
            valoresAGuardar.put("fechaApertura", fechaApertura)
            if (mayorista == true){
                valoresAGuardar.put("mayorista",1)
            }else{
                valoresAGuardar.put("mayorista",2)
            }

            val resultadoEscritura: Long = conexionExcritura
                .insert(
                    "PAPELERIA",
                    null,
                    valoresAGuardar
                )
            conexionExcritura.close()
            return if (resultadoEscritura.toInt() == -1) false else true
        }

        fun consultarPapelerias(): List<Papeleria> {
            val scriptConsultarUsuario = "SELECT * FROM PAPELERIA"
            //Solo abrimos en modo lectura
            val baseDatosLectura = readableDatabase

            val resultadoConsultaLectura = baseDatosLectura.rawQuery(
                scriptConsultarUsuario,
                null
            )

            val existeArticulo = resultadoConsultaLectura.moveToFirst()

            val papeleriasEncontradas = arrayListOf<Papeleria>()

            if(existeArticulo){
                do {
                    var id = resultadoConsultaLectura.getInt(0) //Columna índice 0
                    var nombre = resultadoConsultaLectura.getString(1) //Columna índice 1
                    var direccion = resultadoConsultaLectura.getString(2) //Columna índice 2
                    var fechaCreacion = resultadoConsultaLectura.getString(3)//Columna índice 3
                    var mayoristaNum = resultadoConsultaLectura.getString(4).toInt()//Columna índice 4
                    var mayorista = if (mayoristaNum == 1) true else false
                    if (id != null){
                        papeleriasEncontradas.add(
                            Papeleria(id,nombre,direccion, fechaCreacion ,mayorista)
                        )
                    }
                }while(resultadoConsultaLectura.moveToNext())
            }

            resultadoConsultaLectura.close()
            baseDatosLectura.close()
            Log.i("bd","${papeleriasEncontradas}")
            return papeleriasEncontradas
        }

        fun eliminarPapeleriaFormulario(id: Int): Boolean{

            val conexionEscritura = writableDatabase

            val resultadoEliminacion = conexionEscritura
                .delete(
                    "PAPELERIA", //Tabla
                    "id=?", //Clausura Where
                    arrayOf(
                        id.toString(),
                    ) //Arreglo ordenado de parametros
                )
            conexionEscritura.close()
            return if (resultadoEliminacion.toInt() == -1) false else true

        }

        fun actualizarPapeleriaFormulario(
            nombre: String,
            direccion: String,
            fechaApertura: String,
            mayorista: Boolean,
            idActualizar: Int
        ): Boolean{
            val conexionEscritura = writableDatabase

            val valoresActualizar = ContentValues()
            valoresActualizar.put("nombre",nombre)
            valoresActualizar.put("direccion", direccion)
            valoresActualizar.put("fechaApertura", fechaApertura)
            if (mayorista == true){
                valoresActualizar.put("mayorista",1)
            }else{
                valoresActualizar.put("mayorista",2)
            }

            val resultadoActualizacion = conexionEscritura
                .update(
                    "PAPELERIA", //Nombre de la tabla
                    valoresActualizar, //Valores actualizar
                    "id=?", //Clausura where
                    arrayOf(
                        idActualizar.toString()
                    )//Parametros de la clausuara where
                )

            conexionEscritura.close()
            return if (resultadoActualizacion == -1) false else true

        }

    fun crearArticuloFormulario(
        nombre: String,
        precio: Double,
        cantidad: Int,
        marca: String,
        descripcion: String,
        idPapeleria: Int
    ): Boolean {
        val conexionExcritura = writableDatabase

        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre",nombre)
        valoresAGuardar.put("precio", precio)
        valoresAGuardar.put("cantidad",cantidad)
        valoresAGuardar.put("marca",marca)
        valoresAGuardar.put("descripcion",descripcion)
        valoresAGuardar.put("idPapeleria", idPapeleria)

        val resultadoEscritura: Long = conexionExcritura
            .insert(
                "ARTICULOS",
                null,
                valoresAGuardar
            )
        conexionExcritura.close()
        return if (resultadoEscritura.toInt() == -1) false else true
    }

    fun consultarArticulos(idPapeleriaConsultar: Int): List<Articulo> {
        val scriptConsultarUsuario = "SELECT * FROM ARTICULOS WHERE idPapeleria = ${idPapeleriaConsultar}"
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
                var marca = resultadoConsultaLectura.getString(4)//Columna índice 4
                var descripcion = resultadoConsultaLectura.getString(5) //Columna índice 5
                var idPapeleria = resultadoConsultaLectura.getInt(6)//Columna índice 6

                if (id != null){
                    articulosEncontrado.add(
                        Articulo(id,nombre,precio,cantidad,marca,descripcion,idPapeleria)
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
        marca: String,
        descripcion: String,
        idActualizar: Int,
        idPapeleria: Int
    ): Boolean{
        val conexionEscritura = writableDatabase

        val valoresActualizar = ContentValues()
        valoresActualizar.put("nombre", nombre)
        valoresActualizar.put("precio", precio)
        valoresActualizar.put("cantidad", cantidad)
        valoresActualizar.put("marca",marca)
        valoresActualizar.put("descripcion", descripcion)
        valoresActualizar.put("idPapeleria",idPapeleria)

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
