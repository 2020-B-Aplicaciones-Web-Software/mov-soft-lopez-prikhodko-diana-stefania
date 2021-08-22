package com.example.deber02_lopezdiana

import android.os.Parcel
import android.os.Parcelable

class Evento(
    val idEvento: Int,
    val fechaEvento: String?,
    val tituloEvento: String?,
    val autorEvento: String?,
    val meInteresaEvento: Boolean,
    val seguidoresEventos: Int,
    val interesadosEventos: Int,
    val compartidosEventos: Int,
    val gratisEventos: Boolean,
    val imagenEvento: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idEvento)
        parcel.writeString(fechaEvento)
        parcel.writeString(tituloEvento)
        parcel.writeString(autorEvento)
        parcel.writeByte(if (meInteresaEvento) 1 else 0)
        parcel.writeInt(seguidoresEventos)
        parcel.writeInt(interesadosEventos)
        parcel.writeInt(compartidosEventos)
        parcel.writeByte(if (gratisEventos) 1 else 0)
        parcel.writeString(imagenEvento)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Evento> {
        override fun createFromParcel(parcel: Parcel): Evento {
            return Evento(parcel)
        }

        override fun newArray(size: Int): Array<Evento?> {
            return arrayOfNulls(size)
        }
    }

}