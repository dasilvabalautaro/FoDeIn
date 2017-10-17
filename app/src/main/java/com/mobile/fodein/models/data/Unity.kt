package com.mobile.fodein.models.data

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.models.interfaces.IDataParcelable
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Unity() : RealmObject(), IDataParcelable {
    override fun addList(value: Any) {
        projects.add(value as Project)
    }

    override fun readFromParcel(parcel: Parcel) {
        phone = parcel.readString()
        address = parcel.readString()
        name = parcel.readString()
    }

    var phone: String = ""
    var address: String = ""
    var name: String = ""
    @PrimaryKey
    var id: String = ""
    var projects: RealmList<Project> = RealmList()

    constructor(parcel: Parcel) : this() {
        phone = parcel.readString()
        address = parcel.readString()
        name = parcel.readString()
        id = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(name)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Unity> {
        override fun createFromParcel(parcel: Parcel): Unity {
            return Unity(parcel)
        }

        override fun newArray(size: Int): Array<Unity?> {
            return arrayOfNulls(size)
        }
    }

}