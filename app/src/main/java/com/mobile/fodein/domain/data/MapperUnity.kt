package com.mobile.fodein.domain.data

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.models.data.District


class MapperUnity() : Parcelable {
    var district: District? = null
    var phone: String = ""
    var address: String = ""
    var name: String = ""

    constructor(parcel: Parcel) : this() {
        district = parcel.readParcelable(District::class.java.classLoader)
        phone = parcel.readString()
        address = parcel.readString()
        name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(district, flags)
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