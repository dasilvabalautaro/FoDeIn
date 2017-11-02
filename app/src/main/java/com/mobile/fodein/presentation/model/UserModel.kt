package com.mobile.fodein.presentation.model

import com.mobile.fodein.R
import com.mobile.fodein.presentation.interfaces.IEntity


class UserModel: IEntity {
    var name: String = ""
    var user: String = ""
    var idCard: String = ""
    var email: String = ""
    var password: String = ""
    var phone: String = ""
    var address: String = ""
    var roll: String = ""
    var unit: String = ""
    var token: String = ""
    var image: String = ""
    var id: String = ""
    var list: ArrayList<FormModel> = ArrayList()
    override var graph: Int = R.drawable.ic_person
    override var title: String = name
    override var description: String = ""
    override var imageLink: Int = R.drawable.ic_visibility
}