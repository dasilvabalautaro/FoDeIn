package com.mobile.fodein.presentation.view

import com.mobile.fodein.models.data.User
import com.mobile.fodein.presentation.model.UserModel

interface IUserListView: ILoadDataView {
    fun renderUserList(userModelCollection: Collection<UserModel>)
    fun viewUser(user: User)
}