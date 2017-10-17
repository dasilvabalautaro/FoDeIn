package com.mobile.fodein.domain.data

import android.os.Parcel
import android.os.Parcelable

class MapperForm() : Parcelable {
    var date: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var dateUpdate: String = ""
    var annotation: String = ""
    var annotationOne: String = ""
    var annotationTwo: String = ""

    constructor(parcel: Parcel) : this() {
        date = parcel.readString()
        latitude = parcel.readDouble()
        longitude = parcel.readDouble()
        dateUpdate = parcel.readString()
        annotation = parcel.readString()
        annotationOne = parcel.readString()
        annotationTwo = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(dateUpdate)
        parcel.writeString(annotation)
        parcel.writeString(annotationOne)
        parcel.writeString(annotationTwo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MapperForm> {
        override fun createFromParcel(parcel: Parcel): MapperForm {
            return MapperForm(parcel)
        }

        override fun newArray(size: Int): Array<MapperForm?> {
            return arrayOfNulls(size)
        }
    }
}