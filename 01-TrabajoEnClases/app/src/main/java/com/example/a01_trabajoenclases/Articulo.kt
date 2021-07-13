package com.example.a01_trabajoenclases

import android.os.Parcel
import android.os.Parcelable

class Articulo (
    var id: Int,
    var nombreArticulo: String?,
    var precio: Double?,
    var cantidad: Int?
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun toString(): String {
        return "Nombre: ${nombreArticulo} - Precio: ${precio} - Cantidad: ${cantidad}"
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombreArticulo)
        parcel.writeValue(precio)
        parcel.writeValue(cantidad)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Articulo> {
        override fun createFromParcel(parcel: Parcel): Articulo {
            return Articulo(parcel)
        }

        override fun newArray(size: Int): Array<Articulo?> {
            return arrayOfNulls(size)
        }
    }

}

