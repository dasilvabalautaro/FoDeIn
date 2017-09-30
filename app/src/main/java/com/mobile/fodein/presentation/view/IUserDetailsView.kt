package com.mobile.fodein.presentation.view

import com.mobile.fodein.models.data.User

interface IUserDetailsView: ILoadDataView {
    fun renderUser(user: User?)
}