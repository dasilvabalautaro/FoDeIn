package com.mobile.fodein.models.data

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.models.interfaces.IDataParcelable
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User() : RealmObject(), IDataParcelable{
    override fun addList(value: Any) {
        forms.add(value as Form)
    }

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

    override fun readFromParcel(parcel: Parcel){
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
    override fun describeContents(): Int {
        return 0
    }

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
    @PrimaryKey var id: String = ""
    var forms: RealmList<Form> = RealmList()

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
        id = parcel.readString()
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