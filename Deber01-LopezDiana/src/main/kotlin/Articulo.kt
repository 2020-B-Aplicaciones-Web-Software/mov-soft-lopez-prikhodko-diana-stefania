import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardOpenOption

class Articulo (
        var idArticulo : Int,
        var nombreArticulo : String,
        var precioArticulo : Double,
        var cantidadArticulo : Int,
        var marcaArticulo : String,
        var descripcionArticulo : String?
){

        fun agregarProductos(idPapeleria: Int, articuloNuevo: Articulo){
                //Declaro el path de archivo
                val path = "D:\\GitKrak\\mov-soft-lopez-prikhodko-diana-stefania\\Deber01-LopezDiana\\src\\main\\resources\\archivoInformacion"
                //Obtengo en una lista todas las lineas del archivo
                val inputStream: InputStream = File(path).inputStream()
                val lineas = mutableListOf<String>()
                inputStream.bufferedReader().useLines { lines -> lines.forEach { lineas.add(it) } }
                //Variables para generar el nuevo documento de texto
                var nuevoRegistro = ""
                var nuevosProductos =""
                //Itero por cada linea del archivo
                lineas.forEachIndexed {indice, it ->
                        //Separo cada linea _ (idPapeleria_nombrePapeleria,....ListaProductos)
                        val papeleriaParte: List<String> = it.split("_")
                        //Si es la papeleria que deseo añadir un articulo
                        if(it != null && idPapeleria == papeleriaParte[0].toInt()){
                                //Mantengo los datos de la Papeleria
                                nuevoRegistro += "${papeleriaParte[0]}_${papeleriaParte[1]}_${papeleriaParte[2]}_${papeleriaParte[3]}_${papeleriaParte[4]}_"
                                //Quito corchetes,espacios y comas para obtener cada producto en una lista
                                val productos: List<String> = papeleriaParte[5].trim().replace("]","").replace("[","").replace(" ", "").split(",")
                                productos.forEach {
                                        //Separo los elementos del producto
                                        if (it != "") {
                                                nuevosProductos += "${it}, "
                                        }
                                }
                                //Añadir un producto nuevo
                                nuevosProductos += articuloNuevo.toString()
                                //En el registro añado el nuevo arreglo de productos
                                nuevoRegistro += "[$nuevosProductos]"
                        }else{
                                nuevoRegistro += "${it}" //Si no es la tienda, la agrego
                        }
                        if(indice < lineas.size - 1){
                                nuevoRegistro += "\n" //Asegurar que no se imprinan /n al final
                        }
                }
                //Reescribir el archivo
                val archivo = File(path)
                archivo.printWriter().use { param -> param.println(nuevoRegistro) }

        }

        fun eliminarProductos(idPapeleria: Int, idArticulo: Int){
                //Declaro el path de archivo
                val path = "D:\\GitKrak\\mov-soft-lopez-prikhodko-diana-stefania\\Deber01-LopezDiana\\src\\main\\resources\\archivoInformacion"
                //Obtengo en una lista todas las lineas del archivo
                val inputStream: InputStream = File(path).inputStream()
                val lineas = mutableListOf<String>()
                inputStream.bufferedReader().useLines { lines -> lines.forEach { lineas.add(it) } }
                //Variables para generar el nuevo documento de texto
                var nuevoRegistro = ""
                var nuevosProductos =""
                //Itero por cada linea del archivo
                lineas.forEachIndexed {indice, it ->
                        //Separo cada linea _ (idPapeleria_nombrePapeleria,....ListaProductos)
                        val papeleriaParte: List<String> = it.split("_")
                        //Si es la papeleria que deseo actualizar los articulos
                        if(it != null && idPapeleria == papeleriaParte[0].toInt()){
                                //Mantengo los datos de la Papeleria
                                nuevoRegistro += "${papeleriaParte[0]}_${papeleriaParte[1]}_${papeleriaParte[2]}_${papeleriaParte[3]}_${papeleriaParte[4]}_"
                                //Quito corchetes,espacios y comas para obtener cada producto en una lista
                                val productos: List<String> = papeleriaParte[5].trim().replace("[","").replace("]","").replace(" ", "").split(",")
                                productos.forEach {
                                        //Separo los elementos del producto
                                        val datoProducto: List<String> = it.split("--")
                                        if (nuevosProductos != "" && idArticulo != datoProducto[0].toInt()) {
                                                nuevosProductos += ", "  //Añadir una coma cuando se tenga muchos productos
                                        }
                                        if (idArticulo == datoProducto[0].toInt()) {
                                                println("ELIMINADO EXITOSAMENTE") //No escribir en la variable
                                        } else {
                                                nuevosProductos += "${it}"  //Añado los demas articulos
                                        }
                                }
                                //En el registro añado el nuevo arreglo de productos
                                nuevoRegistro += "[$nuevosProductos]"
                        }else{
                                nuevoRegistro += "${it}" //Si no es la tienda, la añado
                        }
                        if(indice < lineas.size - 1){
                                nuevoRegistro += "\n" //Asegurar que no se imprinan /n al final
                        }
                }
                //Reescribir el archivo
                val archivo = File(path)
                archivo.printWriter().use { param -> param.println(nuevoRegistro) }
        }

        fun actualizarProductos(idPapeleria: Int, articuloNuevo: Articulo){
                //Declaro el path de archivo
                val path = "D:\\GitKrak\\mov-soft-lopez-prikhodko-diana-stefania\\Deber01-LopezDiana\\src\\main\\resources\\archivoInformacion"
                //Obtengo en una lista todas las lineas del archivo
                val inputStream: InputStream = File(path).inputStream()
                val lineas = mutableListOf<String>()
                inputStream.bufferedReader().useLines { lines -> lines.forEach { lineas.add(it) } }
                //Variables para generar el nuevo documento de texto
                var nuevoRegistro = ""
                var nuevosProductos =""
                //Itero por cada linea del archivo
                lineas.forEachIndexed() { indice, it ->
                        //Separo cada linea _ (idPapeleria_nombrePapeleria,....ListaProductos)
                        val papeleriaParte: List<String> = it.split("_")
                        //Si es la papeleria que deseo actualizar los articulos
                        if(it != null && idPapeleria == papeleriaParte[0].toInt()){
                                //Mantengo los datos de la Papeleria
                                nuevoRegistro += "${papeleriaParte[0]}_${papeleriaParte[1]}_${papeleriaParte[2]}_${papeleriaParte[3]}_${papeleriaParte[4]}_"
                                //Quito corchetes,espacios y comas para obtener cada producto en una lista
                                val productos: List<String> = papeleriaParte[5].trim().replace("[","").replace("]","").replace(" ", "").split(",")
                                productos.forEach {
                                        //Separo los elementos del producto
                                        val datoProducto: List<String> = it.split("--")
                                        if (nuevosProductos != ""){
                                                nuevosProductos += ", " //Añadir una coma cuando se tenga muchos productos
                                        }
                                        //Cambio con los datos nuevos del artículo
                                        if (articuloNuevo.idArticulo == datoProducto[0].toInt()) {
                                                nuevosProductos += articuloNuevo.toString()
                                                println("ACTUALIZADO EXITOSAMENTE")
                                        } else {
                                                nuevosProductos += "${it}" //Añado los demas articulos
                                        }
                                }
                                //En el registro añado el nuevo arreglo de productos
                                nuevoRegistro += "[$nuevosProductos]"
                        }else{
                                nuevoRegistro += "${it}" //Si no es la tienda, solo la agrego
                        }
                        if(indice < lineas.size - 1){
                                nuevoRegistro += "\n" //Asegurar que no se imprinan /n al final
                        }
                }
                //Reescribir el archivo
                val archivo = File(path)
                archivo.printWriter().use { param -> param.println(nuevoRegistro) }
        }

        fun visualizarProductos(idPapeleria: Int){
                val path = "D:\\GitKrak\\mov-soft-lopez-prikhodko-diana-stefania\\Deber01-LopezDiana\\src\\main\\resources\\archivoInformacion"

                val inputStream: InputStream = File(path).inputStream()

                val lineas = mutableListOf<String>()
                inputStream.bufferedReader().useLines { lines -> lines.forEach { lineas.add(it) } }
                var n = 0
                lineas.forEach {
                        val dato: List<String> = it.split("_")
                                  if(idPapeleria == dato[0].toInt() && n == 0){
                                        val productos: List<String> = dato[5].replace("[","").replace("]","").split(",")
                                        if(productos[0] != ""){
                                                productos.forEach{
                                                        val caracteristicas: List<String> = it.split("--")
                                                        println("Productos: ")
                                                        println("ID: ${caracteristicas[0]} \nNOMBRE: ${caracteristicas[1]}")
                                                        println("PRECIO: ${caracteristicas[2]}\nCANTIDAD: ${caracteristicas[3]}")
                                                        println("MARCA: ${caracteristicas[4]}\nDESCRIPCIÓN: ${caracteristicas[5]}")
                                                        n = n + 1
                                                }
                                        }else{
                                                println("NO EXISTEN PRODUCTOS EN ESTA PAPELERÍA")
                                        }
                                }
                        }
        }

        override fun toString(): String {
                return "${idArticulo}--${nombreArticulo}--${precioArticulo}--${cantidadArticulo}--${marcaArticulo}--${descripcionArticulo}"
        }
}