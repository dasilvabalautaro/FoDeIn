package com.mobile.fodein.models.data

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.models.interfaces.IDataParcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Form() : RealmObject(), IDataParcelable {
    override fun readFromParcel(parcel: Parcel) {
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

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeParcelable(project, flags)
        dest.writeParcelable(user, flags)
        dest.writeString(date)
        dest.writeDouble(latitude)
        dest.writeDouble(longitude)
        dest.writeString(dateUpdate)
        dest.writeString(annotation)
        dest.writeString(annotationOne)
        dest.writeString(annotationTwo)
    }

    override fun describeContents(): Int {
        return 0
    }

    @PrimaryKey
    var id: String = ""
    @Required
    var project: Project? = null
    @Required
    var user: User? = null
    var date: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var dateUpdate: String = ""
    var annotation: String = ""
    var annotationOne: String = ""
    var annotationTwo: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
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

    companion object CREATOR : Parcelable.Creator<Form> {
        override fun createFromParcel(parcel: Parcel): Form {
            return Form(parcel)
        }

        override fun newArray(size: Int): Array<Form?> {
            return arrayOfNulls(size)
        }
    }

}