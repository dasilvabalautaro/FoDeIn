package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.GetImageListUseCase
import com.mobile.fodein.domain.interactor.RequestRegisterFormPostUseCase
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.model.ImageModel
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import java.util.HashMap
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.set

class AddImagesNetworkPresenter @Inject constructor(private val getImageListUseCase:
                                                    GetImageListUseCase,
                                                    private val requestRegisterFormPostUseCase:
                                                    RequestRegisterFormPostUseCase):
        BasePresenter(){
    private var list: List<ImageModel> = ArrayList()
    private var token: String = ""

    init {
        this.iHearMessage = getImageListUseCase
        val message = this.requestRegisterFormPostUseCase.observableMessage.map { s -> s }
        disposable.add(message.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    kotlin.run {
                        showError(s)
                    }
                })
        val user = this.requestRegisterFormPostUseCase.observableForm.map { u -> u }
        disposable.add(user.observeOn(AndroidSchedulers.mainThread())
                .subscribe { u ->
                    kotlin.run {
                        showMessage(u)
                    }
                })

    }

    fun setVariables(valueField: String, token: String){
        this.token = token
        getImageListUseCase.nameField = "idForm"
        getImageListUseCase.valueField = valueField
        requestRegisterFormPostUseCase.service = Constants.SERVICE_REGISTER_IMAGE
        requestRegisterFormPostUseCase.token = this.token
    }

    fun getListImages(){

        getImageListUseCase.execute(ListObserver())

    }

    override fun destroy() {
        super.destroy()
        this.getImageListUseCase.dispose()
    }

    private fun showCollectionInView(objectsList:
                                     List<ImageModel>){
        if (objectsList.isNotEmpty()){
            this.list = objectsList
            registerImages()
        }

    }

    private fun registerImages(){
        for (i in this.list.indices){
            val pack: MutableMap<String, Any> = HashMap()
            pack[Constants.FIELD_IMAGE_ID] = this.list[i].id
            pack[Constants.FIELD_IMAGE_ID_FORM] = this.list[i].idForm
            pack[Constants.FIELD_IMAGE_IMAGE] = this.list[i].image
            pack[Constants.FIELD_IMAGE_LATITUDE] = this.list[i].latitude
            pack[Constants.FIELD_IMAGE_LONGITUDE] = this.list[i].longitude
            pack[Constants.FIELD_IMAGE_DATE] = this.list[i].date
            requestRegisterFormPostUseCase.backPack = pack
            registerForm()
            Thread.sleep(2000)
        }

    }

    private fun registerForm(){
        if (requestRegisterFormPostUseCase.createAgent() &&
                requestRegisterFormPostUseCase.createBody()){
            requestRegisterFormPostUseCase.getDataServer()
        }
    }


    inner class ListObserver: DisposableObserver<List<ImageModel>>(){
        override fun onNext(t: List<ImageModel>) {
            if (t.isNotEmpty()){
                val idForm =  t[0].idForm
                CachingLruRepository.instance.getLru()
                        .put(idForm, t)
            }
            showCollectionInView(t)
        }

        override fun onComplete() {
            showMessage(context.resources.getString(R.string.task_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }
}