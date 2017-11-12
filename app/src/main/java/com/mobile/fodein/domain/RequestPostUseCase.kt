package com.mobile.fodein.domain

import com.mobile.fodein.models.data.AgentCarrier
import com.mobile.fodein.models.interfaces.IServicePost
import com.mobile.fodein.models.persistent.network.MessageOfService
import com.mobile.fodein.models.persistent.network.ServiceRemotePost
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.net.SocketTimeoutException


abstract class RequestPostUseCase constructor(private val serviceRemotePost:
                                     ServiceRemotePost){
    private val TAG = RequestPostUseCase::class.java.name!!
    private var servicePost: IServicePost? = null
    protected var disposable: CompositeDisposable = CompositeDisposable()
    var agentCarrier: AgentCarrier? = null
    var service: String = ""
    var nameUser: String = ""
    var password: String = ""
    var token: String = ""
    var messageError: String = ""
    var backPack: Map<String, Any>? = null
    private var body: RequestBody? = null
    var observableMessage: Subject<String> = PublishSubject.create()

    private fun setServicePost(): Boolean{
        try {
            servicePost = serviceRemotePost.getService()
            return true
        }catch (ie: IllegalArgumentException){
            println(ie.message)
        }catch (se: SocketTimeoutException){
            println(se.message)
        }
        return false
    }

    fun createAgent(): Boolean{
        var license = ""
        if (service.isEmpty() || backPack == null) return false
        if (!nameUser.isEmpty() && !password.isEmpty()){
            license = "user=$nameUser; password=$password"
        }else if (!token.isEmpty()){
            license = "token=$token"
        }

        agentCarrier =  AgentCarrier(service,
                Constants.HTTPBase, service,
                license, backPack)

        return true
    }

    fun createBody(): Boolean{
        this.body = createRequestBody(agentCarrier!!)
        return this.body != null
    }

    fun getDataServer(){
        if (setServicePost()){
            try {
                disposable.add(servicePost!!.sendPost(agentCarrier!!.license,
                        agentCarrier!!.address + agentCarrier!!.service,
                        body!!)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            response -> getJsonArray(response)
                        })
                )

            }catch (se: SocketTimeoutException){
                println(se.message)
            }

        }
    }

    private fun createRequestBody(agentCarrier: AgentCarrier): RequestBody?{
        try {
            return RequestBody.create(
                    MediaType.parse(Constants.CONTENT_TYPE),
                    (JSONObject(agentCarrier.backPack).toString())
            )

        }catch (je: JSONException){
            println(TAG + ": " + je.message)
        }
        return null
    }

    protected abstract  fun getJsonArray(messageOfService: MessageOfService)
}