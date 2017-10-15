package com.mobile.fodein.presentation.view

import com.mobile.fodein.presentation.interfaces.ILoadDataView

interface IUserListView: ILoadDataView {
    fun <T> renderUserList(userModelCollection: Collection<T>)
    fun <T> viewUser(obj: T)
}