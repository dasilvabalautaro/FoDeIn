package com.mobile.fodein.models.data

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.models.interfaces.IDataParcelable
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class District() : RealmObject(), IDataParcelable {
    override fun addList(value: Any) {
        unities.add(value as Unity)
    }

    override fun readFromParcel(parcel: Parcel) {
        name = parcel.readString()
    }

    @PrimaryKey
    var id: String = ""
    var name: String = ""
    var unities: RealmList<Unity> = RealmList()

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<District> {
        override fun createFromParcel(parcel: Parcel): District {
            return District(parcel)
        }

        override fun newArray(size: Int): Array<District?> {
            return arrayOfNulls(size)
        }
    }
}