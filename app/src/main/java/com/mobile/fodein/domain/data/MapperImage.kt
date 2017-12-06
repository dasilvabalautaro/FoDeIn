package com.mobile.fodein.domain.data

import android.os.Parcel
import android.os.Parcelable

class MapperImage() : Parcelable {
    var idForm: String = ""
    var date: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var image: String = ""

    constructor(parcel: Parcel) : this() {
        idForm = parcel.readString()
        date = parcel.readString()
        latitude = parcel.readDouble()
        longitude = parcel.readDouble()
        image = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idForm)
        parcel.writeString(date)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MapperImage> {
        override fun createFromParcel(parcel: Parcel): MapperImage {
            return MapperImage(parcel)
        }

        override fun newArray(size: Int): Array<MapperImage?> {
            return arrayOfNulls(size)
        }
    }

}