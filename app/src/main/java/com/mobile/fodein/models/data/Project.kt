package com.mobile.fodein.models.data

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.models.interfaces.IDataParcelable
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Project() : RealmObject(), IDataParcelable {
    override fun addList(value: Any) {
        forms.add(value as Form)
    }

    override fun readFromParcel(parcel: Parcel) {
        type = parcel.readInt()
        code = parcel.readString()
        name = parcel.readString()
        latitude = parcel.readDouble()
        longitude = parcel.readDouble()
        finance = parcel.readDouble()
        counterpart = parcel.readDouble()
        notFinance = parcel.readDouble()
        other = parcel.readDouble()
        sum = parcel.readDouble()
        idNet = parcel.readString()
    }

    @PrimaryKey
    var id: String = ""
    var type: Int = 0
    var code: String = ""
    var name: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var finance: Double = 0.00
    var counterpart: Double = 0.00
    var notFinance: Double = 0.00
    var other: Double = 0.00
    var sum: Double = 0.00
    var idNet: String = ""
    var forms: RealmList<Form> = RealmList()

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        type = parcel.readInt()
        code = parcel.readString()
        name = parcel.readString()
        latitude = parcel.readDouble()
        longitude = parcel.readDouble()
        finance = parcel.readDouble()
        counterpart = parcel.readDouble()
        notFinance = parcel.readDouble()
        other = parcel.readDouble()
        sum = parcel.readDouble()
        idNet = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(type)
        parcel.writeString(code)
        parcel.writeString(name)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeDouble(finance)
        parcel.writeDouble(counterpart)
        parcel.writeDouble(notFinance)
        parcel.writeDouble(other)
        parcel.writeDouble(sum)
        parcel.writeString(idNet)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Project> {
        override fun createFromParcel(parcel: Parcel): Project {
            return Project(parcel)
        }

        override fun newArray(size: Int): Array<Project?> {
            return arrayOfNulls(size)
        }
    }

}