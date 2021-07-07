import java.time.LocalDate

fun main () {
    var valor1 = 0
    val articuloGenerico = Articulo(0,"",0.0,0,"",null)
    val papeleriaGenerica = Papeleria(0,"","", LocalDate.now(), false, arrayListOf())

    while (valor1 != 9){
        println("============ Deber 01 (Papelería-Artículos) ============")
        println("Seleccione la acción que desea hacer: ")
        println("1. Visualizar Papelerías\n2. Crear Papelería")
        println("3. Actualizar Papelería\n4. Eliminar Papelería")
        println("5. Visualizar Productos de una Papelerías\n6. Añadir un Producto a una Papelería")
        println("7. Actualizar Producto de una Papelería\n8. Eliminar un Producto de una Papelería")
        println("9. Salir del sistema")
        valor1 = readLine()!!.toInt()
        when{
            valor1 == 1 ->{
                papeleriaGenerica.visualizarPapelerías()
                println("\n")
            }
            valor1 == 2 ->{
                println("Ingrese los datos para crear una papelería")
                print("ID: ")
                var idPapeleria = readLine()!!.toInt()
                print("NOMBRE: ")
                val nombrePapeleria = readLine().toString()
                print("DIRECCIÓN : ")
                val direccionPapeleria = readLine().toString()
                print("FECHA DE CREACIÓN (02/05/2012): ")
                var fechaPapeleria = "01/01/2021"
                fechaPapeleria = readLine().toString()
                val fechaDividida: List<String> = fechaPapeleria.split("/")
                print("MAYORISTA (true=Si; false=No): ")
                val mayoristaPapeleria = readLine().toBoolean()

                val papeleriaCreada = Papeleria(idPapeleria,nombrePapeleria,direccionPapeleria,
                        LocalDate.of(fechaDividida[2].toInt(),fechaDividida[1].toInt(),fechaDividida[0].toInt()),
                        mayoristaPapeleria, arrayListOf())
                papeleriaCreada.crearPapelería(papeleriaCreada)
            }
            valor1 == 3 ->{
                println("Ingrese los datos para actualizar una papelería")
                print("ID: ")
                var idPapeleria = readLine()!!.toInt()
                print("NOMBRE: ")
                val nombrePapeleria = readLine().toString()
                print("DIRECCIÓN : ")
                val direccionPapeleria = readLine().toString()
                print("FECHA DE CREACIÓN (02/05/2012): ")
                var fechaPapeleria = "01/01/2021"
                fechaPapeleria = readLine().toString()
                val fechaDividida: List<String> = fechaPapeleria.split("/")
                print("MAYORISTA (true=Si; false=No): ")
                val mayoristaPapeleria = readLine().toBoolean()

                val papeleriaCreada = Papeleria(idPapeleria,nombrePapeleria,direccionPapeleria,
                    LocalDate.of(fechaDividida[2].toInt(),fechaDividida[1].toInt(),fechaDividida[0].toInt()),
                    mayoristaPapeleria, arrayListOf())
                papeleriaCreada.actualizarPapeleria(papeleriaCreada)
            }
            valor1 == 4 ->{
                println("¿Qué papelería quiere eliminar? ")
                print("Ingrese el ID: ")
                val idEliminar = readLine()?.toInt()
                if (idEliminar != null) {
                    papeleriaGenerica.eliminarPapelerias(idEliminar)
                }
            }
            valor1 == 5 ->{
                println("Visualizar Productos de una Papeleria")
                print("ID Papelería: ")
                val idVerPapeleria = readLine()?.toInt()
                if (idVerPapeleria != null) {
                    articuloGenerico.visualizarProductos(idVerPapeleria)
                }
                println("")
            }
            valor1 == 6 ->{
                println("Añadir Productos a una Papeleria")
                print("ID Papelería: ")
                val idVerPapeleria = readLine()!!.toInt()
                print("ID Artículo: ")
                val idArticulo = readLine()!!.toInt()
                print("Nombre Artículo: ")
                val nombreArticulo = readLine().toString()
                print("Precio Artículo: ")
                var precioArticulo = 0.00
                precioArticulo = readLine()!!.toDouble()
                print("Cantidad Artículo: ")
                val cantidadArticulo = readLine()!!.toInt()
                print("Marca Artículo: ")
                val marcaArticulo = readLine().toString()
                print("Descripción Artículos: ")
                val descripcionArticulo = readLine().toString()
                if (idVerPapeleria != null) {
                    val articuloCreado = Articulo(idArticulo,nombreArticulo,precioArticulo,
                    cantidadArticulo,marcaArticulo,descripcionArticulo)
                    articuloCreado.agregarProductos(idVerPapeleria,articuloCreado)
                }
                println("")
            }
            valor1 == 7 ->{
                println("Actualizar un Producto de una Papeleria")
                print("ID Papelería: ")
                val idVerPapeleria = readLine()!!.toInt()
                print("ID Artículo: ")
                val idArticulo = readLine()!!.toInt()
                print("Nombre Artículo: ")
                val nombreArticulo = readLine().toString()
                print("Precio Artículo: ")
                var precioArticulo = 0.00
                precioArticulo = readLine()!!.toDouble()
                print("Cantidad Artículo: ")
                val cantidadArticulo = readLine()!!.toInt()
                print("Marca Artículo: ")
                val marcaArticulo = readLine().toString()
                print("Descripción Artículos: ")
                val descripcionArticulo = readLine().toString()
                if (idVerPapeleria != null) {
                    val articuloCreado = Articulo(idArticulo,nombreArticulo,precioArticulo,
                        cantidadArticulo,marcaArticulo,descripcionArticulo)
                    articuloCreado.actualizarProductos(idVerPapeleria,articuloCreado)
                }
                println("")
            }
            valor1 == 8 ->{
                println("Eliminar un Producto de una Papeleria")
                print("ID Papelería: ")
                val idVerPapeleria = readLine()!!.toInt()
                print("ID Artículo: ")
                val idArticulo = readLine()!!.toInt()
                if (idVerPapeleria != null) {
                    articuloGenerico.eliminarProductos(idVerPapeleria,idArticulo)
                }
                println("")
            }
        }
    }





}