package com.mobile.fodein.domain.data

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.models.data.Unity


class MapperProject() : Parcelable {

    var unity: Unity? = null
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

    constructor(parcel: Parcel) : this() {
        unity = parcel.readParcelable(Unity::class.java.classLoader)
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
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(unity, flags)
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
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MapperProject> {
        override fun createFromParcel(parcel: Parcel): MapperProject {
            return MapperProject(parcel)
        }

        override fun newArray(size: Int): Array<MapperProject?> {
            return arrayOfNulls(size)
        }
    }
}