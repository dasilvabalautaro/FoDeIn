package com.mobile.fodein.presentation.model

import com.mobile.fodein.models.data.Project
import com.mobile.fodein.models.data.User


class FormModel {
    var id: String = ""
    var project: Project? = null
    var user: User? = null
    var date: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var dateUpdate: String = ""
    var annotation: String = ""
    var annotationOne: String = ""
    var annotationTwo: String = ""
}