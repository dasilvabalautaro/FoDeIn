package com.mobile.fodein.domain

import com.mobile.fodein.models.data.AgentCarrier
import com.mobile.fodein.models.interfaces.IServiceGet
import com.mobile.fodein.models.persistent.network.MessageOfService
import com.mobile.fodein.models.persistent.network.ServiceRemoteGet
import com.mobile.fodein.tools.Constants
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
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
        }catch (ex: Exception){
            println(ex.message)
        }catch (se: SocketTimeoutException){
            println(se.message)
        }
        return false
    }

    fun getDataServer(){
        if (setServiceGet()){
            try {
                disposable.add(serviceGet!!.sendGet(agentCarrier!!.license,
                        agentCarrier!!.address + agentCarrier!!.service)
                        .subscribeOn(Schedulers.newThread())
                        .subscribe({
                            response -> getJsonArray(response)
                        })
                )

            }catch (se: SocketTimeoutException){
                println(se.message)
            }catch (ce: ConnectException){
                println(ce.message)
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

}