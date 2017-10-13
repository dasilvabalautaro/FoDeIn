package com.mobile.fodein.domain.data

import android.os.Parcel
import android.os.Parcelable


class MapperDistrict() : Parcelable {
    var name: String = ""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MapperDistrict> {
        override fun createFromParcel(parcel: Parcel): MapperDistrict {
            return MapperDistrict(parcel)
        }

        override fun newArray(size: Int): Array<MapperDistrict?> {
            return arrayOfNulls(size)
        }
    }
}