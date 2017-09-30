package com.mobile.fodein.models.data

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*


class User(var name: String = "",
           var user: String = "",
           var idCard: String = "",
           var email: String = "",
           var password: String = "",
           var phone: String = "",
           var address: String = "",
           var description: String = "",
           var roll: String = "",
           var unit: String = "",
           @PrimaryKey var id: String =
                UUID.randomUUID().toString()): RealmObject(), Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())


    override fun writeToParcel(p0: Parcel?, p1: Int) {
        if (p0 != null){
            p0.writeString(name)
            p0.writeString(user)
            p0.writeString(idCard)
            p0.writeString(email)
            p0.writeString(password)
            p0.writeString(phone)
            p0.writeString(address)
            p0.writeString(description)
            p0.writeString(roll)
            p0.writeString(unit)
            p0.writeString(id)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}