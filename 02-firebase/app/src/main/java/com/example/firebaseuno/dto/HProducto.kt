package com.example.firebaseuno.dto

class HProducto (
        val uid: String,
        val producto: String,
        val cantidad: Int,
        val precio: Double
        ){
        override fun toString(): String {
                return "Producto: ${producto}\nCantidad: ${cantidad}\nPrecio: ${precio}"
        }
}