package com.example.examen01_lopezdiana

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

class Papeleria(
    var idPapeleria: Int,
    var nombrePapeleria: String?,
    var direccionPapeleria: String?,
    var fechaAperturaPapeleria: String?,
    var mayorista: Boolean?
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun toString(): String {
        return "Nombre: ${nombrePapeleria}\nDireccion: ${direccionPapeleria}\nFecha Apertura: ${fechaAperturaPapeleria}\nMayorista: ${mayorista}"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idPapeleria)
        parcel.writeString(nombrePapeleria)
        parcel.writeString(direccionPapeleria)
        parcel.writeString(fechaAperturaPapeleria)
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