package com.mobile.fodein.presentation.view

import com.mobile.fodein.presentation.model.UserModel

interface IUserDetailsView: ILoadDataView {
    fun renderUser(user: UserModel?)
}