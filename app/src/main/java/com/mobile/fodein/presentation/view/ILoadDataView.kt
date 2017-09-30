package com.mobile.fodein.presentation.view

import android.content.Context


interface ILoadDataView {
    fun showLoading()
    fun hideLoading()
    fun showRetry()
    fun hideRetry()
    fun showError(message: String)
    fun context(): Context
}