package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.App
import com.mobile.fodein.domain.interfaces.IHearMessage
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.interfaces.ILoadDataView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter: IPresenter {

    protected var disposable: CompositeDisposable = CompositeDisposable()
    var iHearMessage: IHearMessage? = null
    var view: ILoadDataView? = null

    val context = App.appComponent.context()

    override fun destroy() {
        this.view = null
        if (!this.disposable.isDisposed ) this.disposable.dispose()
    }

    override fun hearError() {
        val hear = iHearMessage!!.hearError()
        disposable.add(hear.observeOn(AndroidSchedulers.mainThread())
                .subscribe { e ->
                    showError(e.message)
                })
    }

    override fun hearMessage() {
        val hear = iHearMessage!!.hearMessage()
        disposable.add(hear.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    showMessage(s)
                })
    }

    override fun showMessage(message: String) {
        this.view!!.showMessage(message)
    }

    override fun showError(error: String) {
        this.view!!.showError(error)
    }

    protected inline fun <reified E> existInCache(keyCache:String, render:Boolean = true): Boolean{
        val dataCache = CachingLruRepository.instance.getLru()
                .get(keyCache)
        if (dataCache != null && dataCache is List<*>){
            val list: List<E> = dataCache
                    .filterIsInstance<E>()
            if (render){
                this.view!!.renderList(list)
            }

            return true
        }
        return false
    }
}