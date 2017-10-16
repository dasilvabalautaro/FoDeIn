package com.mobile.fodein.presentation.model

import com.mobile.fodein.R
import com.mobile.fodein.presentation.interfaces.IEntity


class FormModel: IEntity {
    var id: String = ""
    var project: ProjectModel? = null
    var user: UserModel? = null
    var date: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var dateUpdate: String = ""
    var annotation: String = ""
    var annotationOne: String = ""
    var annotationTwo: String = ""

    override var graph: Int = R.drawable.ic_form
    override var title: String = date
    override var description: String = "Latitud: " + latitude.toString() +
            " Longitud: " + longitude.toString()
    override var imageLink: Int = R.drawable.ic_visibility
}