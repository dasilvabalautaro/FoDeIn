package com.mobile.fodein.presentation.view

interface IUserListView: ILoadDataView {
    fun <T> renderUserList(userModelCollection: Collection<T>)
    fun <T> viewUser(obj: T)
}