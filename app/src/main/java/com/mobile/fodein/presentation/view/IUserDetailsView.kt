package com.mobile.fodein.presentation.view

import com.mobile.fodein.domain.data.MapperUser

interface IUserDetailsView: ILoadDataView {
    fun renderUser(user: MapperUser?)
}