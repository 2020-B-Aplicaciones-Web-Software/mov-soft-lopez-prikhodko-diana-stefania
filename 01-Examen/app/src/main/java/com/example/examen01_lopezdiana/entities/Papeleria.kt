package com.example.examen01_lopezdiana.entities

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*

class Papeleria(
    var idPapeleria: String?,
    var nombrePapeleria: String?,
    var direccionPapeleria: String?,
    var fechaAperturaPapeleria: Date?,
    var mayorista: Boolean?
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Date::class.java.classLoader) as? Date,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    //Método toString sobreescrito para que en la lista se vea mejor
    override fun toString(): String {
        val format = SimpleDateFormat("dd/MM/yyyy")
        return "Nombre: ${nombrePapeleria}\nDireccion: ${direccionPapeleria}\nFecha Apertura: ${format.format(fechaAperturaPapeleria)}\nMayorista: ${mayorista}"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idPapeleria)
        parcel.writeString(nombrePapeleria)
        parcel.writeString(direccionPapeleria)
        parcel.writeValue(fechaAperturaPapeleria)
        parcel.writeValue(mayorista)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Papeleria> {
        override fun createFromParcel(parcel: Parcel): Papeleria {
            return Papeleria(parcel)
        }

        override fun newArray(size: Int): Array<Papeleria?> {
            return arrayOfNulls(size)
        }
    }

}