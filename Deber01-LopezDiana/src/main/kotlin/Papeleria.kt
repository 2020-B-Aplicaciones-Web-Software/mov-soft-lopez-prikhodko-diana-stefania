import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import javax.print.attribute.standard.PrinterInfo
import kotlin.collections.ArrayList

class Papeleria (
     var idPapeleria : Int,
     var nombrePapeleria : String,
     var direccionPapeleria : String,
     var fechaAperturaPapeleria : LocalDate,
     var mayorista : Boolean,
     var articulos : ArrayList <Articulo>
){
     fun crearPapelería(nuevaPapeleria: Papeleria){
          val path = "D:\\GitKrak\\mov-soft-lopez-prikhodko-diana-stefania\\Deber01-LopezDiana\\src\\main\\resources\\archivoInformacion"
          val archivo = File(path)
          Files.write(archivo.toPath(), nuevaPapeleria.toString().toByteArray(), StandardOpenOption.APPEND)
          Files.write(archivo.toPath(), "\n".toByteArray(), StandardOpenOption.APPEND)
          println("\nCREACIÓN EXITOSA\n")
     }

     fun visualizarPapelerías() {
          ////Declaro el path de archivo
          val path =
               "D:\\GitKrak\\mov-soft-lopez-prikhodko-diana-stefania\\Deber01-LopezDiana\\src\\main\\resources\\archivoInformacion"
          //Obtengo en una lista todas las lineas del archivo
          val inputStream: InputStream = File(path).inputStream()
          val lineas = mutableListOf<String>()
          inputStream.bufferedReader().useLines { lines -> lines.forEach { lineas.add(it) } }
          lineas.forEachIndexed { indice, it ->
               //Separo cada linea _ (idPapeleria_nombrePapeleria,....ListaProductos)
               if(it != null){
                    val papeleriaParte: List<String> = it.trim().split("_")
                    if (papeleriaParte != null){
                         println("--- DATOS PAPELERÍA ---")
                         println("ID: ${papeleriaParte[0]}\tNOMBRE: ${papeleriaParte[1]}\nDIRECCION: ${papeleriaParte[2]} ")
                         println("FECHA APERTURA: ${papeleriaParte[3]}\tMAYORISTA: ${papeleriaParte[4]}")
                    }
               }
          }
     }

     fun eliminarPapelerias(idPapeleria: Int){
          //Declaro el path de archivo
          val path = "D:\\GitKrak\\mov-soft-lopez-prikhodko-diana-stefania\\Deber01-LopezDiana\\src\\main\\resources\\archivoInformacion"
          //Obtengo en una lista todas las lineas del archivo
          val inputStream: InputStream = File(path).inputStream()
          val lineas = mutableListOf<String>()
          inputStream.bufferedReader().useLines { lines -> lines.forEach { lineas.add(it) } }
          //Variables para generar el nuevo documento de texto
          var nuevoRegistro = ""
          var bandera = false
          //Itero por cada linea del archivo
          lineas.forEachIndexed {indice, it ->
               //Separo cada linea _ (idPapeleria_nombrePapeleria,....ListaProductos)
               val papeleriaParte: List<String> = it.trim().split("_")
               //Si es la papeleria que deseo actualizar los articulos
               if(it != null && idPapeleria == papeleriaParte[0].toInt()){
                    //Cambio los datos de la papeleria
                    println("ELIMINACIÓN EXISTOSA")
                    bandera = true
                    val productos: List<String> = papeleriaParte[5].trim().replace("[","").replace("]","").replace(" ", "").split(",")
               }else{
                    nuevoRegistro += "${it.trim()}" //Si no es la tienda, la añado
                    if(indice < lineas.size - 1){
                         nuevoRegistro += "\n" //Asegurar que no se imprinan /n al final
                    }
               }

          }
          if(bandera == false){
               println("NO EXISTE EL ELEMENTO A ELIMINAR")
          }
          //Reescribir el archivo
          val archivo = File(path)
          archivo.printWriter().use { param -> param.println(nuevoRegistro.trim()) }
     }

     fun actualizarPapeleria(nuevaPapeleria: Papeleria){
          //Declaro el path de archivo
          val path = "D:\\GitKrak\\mov-soft-lopez-prikhodko-diana-stefania\\Deber01-LopezDiana\\src\\main\\resources\\archivoInformacion"
          //Obtengo en una lista todas las lineas del archivo
          val inputStream: InputStream = File(path).inputStream()
          val lineas = mutableListOf<String>()
          inputStream.bufferedReader().useLines { lines -> lines.forEach { lineas.add(it) } }
          //Variables para generar el nuevo documento de texto
          var nuevoRegistro = ""
          var bandera = false
          //Itero por cada linea del archivo
          lineas.forEachIndexed { indice, it ->
               //Separo cada linea _ (idPapeleria_nombrePapeleria,....ListaProductos)
               val papeleriaParte: List<String> = it.split("_")

                    //En el registro añado las existentes
                    if (nuevaPapeleria.idPapeleria == papeleriaParte[0].toInt()) {
                         nuevoRegistro += nuevaPapeleria.toString()
                         bandera = true
                         println("ACTUALIZADO CON EXITO")
                    } else {
                         nuevoRegistro += "${it}" //Añado los demas articulos
                    }

                    if (indice < lineas.size - 1) {
                         nuevoRegistro += "\n" //Asegurar que no se imprinan /n al final
                    }
               }

          if (bandera){
               //Reescribir el archivo
               val archivo = File(path)
               archivo.printWriter().use { param -> param.println(nuevoRegistro) }
          }else{
               print("NO SE PUDO ACTUALIZAR")
          }

     }

     override fun toString(): String {
          return "${ idPapeleria }_${nombrePapeleria}_${direccionPapeleria}_${fechaAperturaPapeleria}_${mayorista}_${articulos}"
     }
}