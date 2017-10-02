package com.mobile.fodein.domain.data

import android.os.Parcel
import android.os.Parcelable


class MapperUser() : Parcelable {
    var name: String = ""
    var user: String = ""
    var idCard: String = ""
    var email: String = ""
    var password: String = ""
    var phone: String = ""
    var address: String = ""
    var description: String = ""
    var roll: String = ""
    var unit: String = ""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        user = parcel.readString()
        idCard = parcel.readString()
        email = parcel.readString()
        password = parcel.readString()
        phone = parcel.readString()
        address = parcel.readString()
        description = parcel.readString()
        roll = parcel.readString()
        unit = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(user)
        parcel.writeString(idCard)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(description)
        parcel.writeString(roll)
        parcel.writeString(unit)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MapperUser> {
        override fun createFromParcel(parcel: Parcel): MapperUser {
            return MapperUser(parcel)
        }

        override fun newArray(size: Int): Array<MapperUser?> {
            return arrayOfNulls(size)
        }
    }
}