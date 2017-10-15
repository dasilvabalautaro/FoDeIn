package com.mobile.fodein.presentation.presenter

interface IPresenter {
    fun destroy()
    fun showMessage(message: String)
    fun showError(error: String)
    fun hearMessage()
    fun hearError()
}