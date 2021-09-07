package com.example.firebaseuno

class ArticuloOrden (
        val restaurante: String,
        val producto: String,
        val cantidad: Int,
        val precio: Double
        ){
        override fun toString(): String {
                return "Restaurante: ${restaurante}\nProducto: ${producto}\nCantidad: ${cantidad}\nPrecio: ${precio}"
        }
}