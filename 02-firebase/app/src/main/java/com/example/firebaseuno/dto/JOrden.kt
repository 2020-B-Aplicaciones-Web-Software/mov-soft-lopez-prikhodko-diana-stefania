package com.example.firebaseuno.dto

class JOrden(
    val fechaPedido: String,
    val usuario: String,
    val total: Double,
    val calificacion: Int,
    val productos: ArrayList<HProducto>,
    val restaurante: String?
) {
    override fun toString(): String {
        var lista = ""
        productos.forEach{
            lista += "${it.cantidad} - ${it.producto} \n"
        }
        return "Fecha: ${fechaPedido}\nUsuario: ${usuario}\nProductos: \n${lista}Total: ${total}"
    }

}
