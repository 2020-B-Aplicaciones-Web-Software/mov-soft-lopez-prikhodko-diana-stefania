package com.example.firebaseuno.dto

class FireStoreUsuarioDto(
    var uid: String = "",
    var email: String = "",
    val roles : ArrayList<String> = arrayListOf()
) {
}