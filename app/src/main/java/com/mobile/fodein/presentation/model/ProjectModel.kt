package com.mobile.fodein.presentation.model

import com.mobile.fodein.models.data.Form
import com.mobile.fodein.models.data.Unity


class ProjectModel {
    var id: String = ""
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
    var list: List<Form> = ArrayList()
}