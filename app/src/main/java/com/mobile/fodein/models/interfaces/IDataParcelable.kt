package com.mobile.fodein.models.interfaces

import android.os.Parcel
import android.os.Parcelable

interface IDataParcelable : Parcelable {
    fun readFromParcel(parcel: Parcel)
    fun addList(value: Any)
}