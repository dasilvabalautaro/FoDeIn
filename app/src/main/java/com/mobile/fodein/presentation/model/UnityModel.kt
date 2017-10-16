package com.mobile.fodein.presentation.model

import com.mobile.fodein.R
import com.mobile.fodein.presentation.interfaces.IEntity


class UnityModel: IEntity {
    var district: DistrictModel? = null
    var phone: String = ""
    var address: String = ""
    var name: String = ""
    var id: String = ""
    var list: List<ProjectModel> = ArrayList()
    override var graph: Int = R.drawable.ic_group_work
    override var title: String = name
    override var description: String = address + " " + phone
    override var imageLink: Int = R.drawable.ic_project

}