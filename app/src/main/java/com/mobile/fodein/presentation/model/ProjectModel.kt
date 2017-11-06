package com.mobile.fodein.presentation.model

import com.mobile.fodein.R
import com.mobile.fodein.presentation.interfaces.IEntity


class ProjectModel: IEntity {
    override var id: String = ""
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
    var list: ArrayList<FormModel> = ArrayList()

    override var graph: Int = R.drawable.ic_project
    override var title: String = name
    override var description: String = "Latitud: " + latitude.toString() +
            " Longitud: " + longitude.toString()
    override var imageLink: Int = R.drawable.ic_form
}