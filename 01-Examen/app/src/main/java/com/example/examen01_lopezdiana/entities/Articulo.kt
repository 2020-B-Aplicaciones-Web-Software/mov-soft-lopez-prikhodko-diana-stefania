package com.example.examen01_lopezdiana.entities

import android.opengl.GLException
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import com.google.type.LatLng

class Articulo(
    var idArticulo: String?,
    var nombreArticulo: String?,
    var precioArticulo: Double,
    var cantidadArticulo: Int,
    var marcaArticulo: String?,
    var descripcionArticulo: String?,
    var ubicacionLatArticulo: Double?,
    var ubicacionLngArticulo: Double?
) : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idArticulo)
        parcel.writeString(nombreArticulo)
        parcel.writeDouble(precioArticulo)
        parcel.writeInt(cantidadArticulo)
        parcel.writeString(marcaArticulo)
        parcel.writeString(descripcionArticulo)
        parcel.writeDouble(ubicacionLatArticulo!!)
        parcel.writeDouble(ubicacionLngArticulo!!)
    }

    //Método toString sobreescrito para que en la lista se vea mejor
    override fun toString(): String {
        return "Nombre: ${nombreArticulo}\nPrecio: ${precioArticulo}\nCantidad: ${cantidadArticulo}\n" +
                "Marca: ${marcaArticulo}\nDescripcion: ${descripcionArticulo}\nUbicación: (${ubicacionLatArticulo}" +
                ",${ubicacionLngArticulo})"
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