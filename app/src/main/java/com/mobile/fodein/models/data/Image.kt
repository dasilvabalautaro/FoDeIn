package com.mobile.fodein.models.data

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.models.interfaces.IDataParcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Image(): RealmObject(), IDataParcelable {
    override fun readFromParcel(parcel: Parcel) {
        idForm = parcel.readString()
        date = parcel.readString()
        latitude = parcel.readDouble()
        longitude = parcel.readDouble()
        image = parcel.readString()
    }

    override fun addList(value: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @PrimaryKey
    var id: String = ""
    var idForm: String = ""
    var date: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var image: String = ""
    var upload: Boolean = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        idForm = parcel.readString()
        date = parcel.readString()
        latitude = parcel.readDouble()
        longitude = parcel.readDouble()
        image = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(idForm)
        parcel.writeString(date)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }
}