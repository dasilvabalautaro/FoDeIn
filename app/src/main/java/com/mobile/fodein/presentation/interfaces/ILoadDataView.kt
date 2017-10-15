package com.mobile.fodein.presentation.interfaces

import android.content.Context


interface ILoadDataView {
    fun showLoading()
    fun showMessage(message: String)
    fun hideLoading()
    fun showRetry()
    fun hideRetry()
    fun showError(message: String)
    fun context(): Context
    fun <T> renderList(objectModelCollection: Collection<T>)
    fun <T> renderObject(obj: T)
}