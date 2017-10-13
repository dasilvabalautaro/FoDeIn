package com.mobile.fodein.domain.data

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.models.data.Project
import com.mobile.fodein.models.data.User

class MapperForm() : Parcelable {
    var project: Project? = null
    var user: User? = null
    var date: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var dateUpdate: String = ""
    var annotation: String = ""
    var annotationOne: String = ""
    var annotationTwo: String = ""

    constructor(parcel: Parcel) : this() {
        project = parcel.readParcelable(Project::class.java.classLoader)
        user = parcel.readParcelable(User::class.java.classLoader)
        date = parcel.readString()
        latitude = parcel.readDouble()
        longitude = parcel.readDouble()
        dateUpdate = parcel.readString()
        annotation = parcel.readString()
        annotationOne = parcel.readString()
        annotationTwo = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(project, flags)
        parcel.writeParcelable(user, flags)
        parcel.writeString(date)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(dateUpdate)
        parcel.writeString(annotation)
        parcel.writeString(annotationOne)
        parcel.writeString(annotationTwo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MapperForm> {
        override fun createFromParcel(parcel: Parcel): MapperForm {
            return MapperForm(parcel)
        }

        override fun newArray(size: Int): Array<MapperForm?> {
            return arrayOfNulls(size)
        }
    }
}