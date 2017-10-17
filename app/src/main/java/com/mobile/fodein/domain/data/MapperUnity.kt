package com.mobile.fodein.domain.data

import android.os.Parcel
import android.os.Parcelable


class MapperUnity() : Parcelable {
    var phone: String = ""
    var address: String = ""
    var name: String = ""

    constructor(parcel: Parcel) : this() {
        phone = parcel.readString()
        address = parcel.readString()
        name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MapperUnity> {
        override fun createFromParcel(parcel: Parcel): MapperUnity {
            return MapperUnity(parcel)
        }

        override fun newArray(size: Int): Array<MapperUnity?> {
            return arrayOfNulls(size)
        }
    }
}