package com.example.examen01_lopezdiana.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.type.LatLng

class Articulo(
    var idArticulo: Int,
    var nombreArticulo: String?,
    var precioArticulo: Double,
    var cantidadArticulo: Int,
    var marcaArticulo: String?,
    var descripcionArticulo: String?,
    var ubicacionArticulo: LatLng?
) : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(ubicacionArticulo) as LatLng?
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idArticulo)
        parcel.writeString(nombreArticulo)
        parcel.writeDouble(precioArticulo)
        parcel.writeInt(cantidadArticulo)
        parcel.writeString(marcaArticulo)
        parcel.writeString(descripcionArticulo)
        parcel.writeValue(ubicacionArticulo)
    }

    //Método toString sobreescrito para que en la lista se vea mejor
    override fun toString(): String {
        return "Nombre: ${nombreArticulo}\nPrecio: ${precioArticulo}\nCantidad: ${cantidadArticulo}\nMarca: ${marcaArticulo}\nDescripcion: ${descripcionArticulo}"
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