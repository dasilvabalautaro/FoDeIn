package com.mobile.fodein.domain

import com.mobile.fodein.models.data.AgentCarrier
import com.mobile.fodein.models.interfaces.IServiceGet
import com.mobile.fodein.models.persistent.network.MessageOfService
import com.mobile.fodein.models.persistent.network.ServiceRemoteGet
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException



abstract class RequestGetUseCase constructor(private val serviceRemoteGet:
                                             ServiceRemoteGet) {
    private var serviceGet: IServiceGet? = null
    protected var disposable: CompositeDisposable = CompositeDisposable()
    var agentCarrier: AgentCarrier? = null
    var service: String = ""
    var nameUser: String = ""
    var password: String = ""
    var token: String = ""
    var messageError: String = ""
    var observableMessage: Subject<String> = PublishSubject.create()

    private fun setServiceGet(): Boolean{
        try {
            serviceGet = serviceRemoteGet.getService()

            return true
        }catch (ie: IllegalArgumentException){
            println(ie.message)
        }catch (ex: NullPointerException){
            println(ex.message)
        }catch (se: SocketTimeoutException){
            println(se.message)
        }catch (ce: ConnectException){
            println(ce.message)
        }
        return false
    }

    fun getDataServer(){
        //&& connectionNetwork.checkConnect()
        if (setServiceGet()){
            try {
                disposable.add(serviceGet!!.sendGet(agentCarrier!!.license,
                        agentCarrier!!.address + agentCarrier!!.service)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getMessageService())
                )

            }catch (se: SocketTimeoutException){
                println(se.message)
            }catch (ce: ConnectException){
                println(ce.message)
            }catch (ex: NullPointerException){
                println(ex.message)
            }catch (ei: IOException){
                println(ei.message)
            }

        }
    }

    private fun getMessageService(): DisposableSingleObserver<MessageOfService> {
        return object : DisposableSingleObserver<MessageOfService>() {
            override fun onSuccess(value: MessageOfService) {
                getJsonArray(value)
            }

            override fun onError(e: Throwable) {
                if (!e.message.isNullOrEmpty()){
                    sendMessageError(e.message!!)
                }
                println(e.message)
            }
        }
    }

    fun createAgent(): Boolean{
        var license = ""

        if (!nameUser.isEmpty() && !password.isEmpty()) {
            license = "user=$nameUser; password=$password"
        }else if (!token.isEmpty()){
            license = "token=$token"
        }
        if (license.isEmpty()) return false

        agentCarrier =  AgentCarrier(service,
                Constants.HTTPBase, service,
                license, null)
        return true
    }

    protected abstract  fun getJsonArray(messageOfService: MessageOfService)
    protected abstract fun sendMessageError(message: String)
}