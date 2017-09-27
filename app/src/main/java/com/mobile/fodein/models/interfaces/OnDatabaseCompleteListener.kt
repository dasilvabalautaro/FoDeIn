package com.mobile.fodein.models.interfaces

interface OnDatabaseCompleteListener {
    fun onSaveFailed(error: String)
    fun onSaveSucceeded()
    fun onDeleteCompleted()
    fun onDeleteFailed(error: String)
    fun onUpdateCompleted()
    fun onUpdateFailed(error: String)
}