package com.mobile.fodein.models.executor

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.dagger.ModelsModule
import com.mobile.fodein.domain.data.MapperUnity
import com.mobile.fodein.domain.repository.IUnityRepository
import com.mobile.fodein.models.data.Unity
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.models.persistent.repository.DatabaseRepository
import com.mobile.fodein.presentation.mapper.UnityModelDataMapper
import com.mobile.fodein.presentation.model.DistrictModel
import com.mobile.fodein.presentation.model.UnityModel
import com.mobile.fodein.tools.Constants
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnityExecutor @Inject constructor():
        DatabaseRepository(), IUnityRepository {
    private val ID_NET = "idNet"
    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}

    @Inject
    lateinit var interactDatabaseListener: DatabaseListenerExecutor
    @Inject
    lateinit var unityModelDataMapper: UnityModelDataMapper

    init {
        component.inject(this)
    }


    override fun userGetMessage(): Observable<String> {
        return this.interactDatabaseListener
                .observableMessage.map { s -> s }
    }

    override fun userGetError(): Observable<DatabaseOperationException> {
        return this.interactDatabaseListener
                .observableException.map { e -> e }
    }

    override fun unityList(): Observable<List<UnityModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<Unity> = Unity::class.java
            val listUnity: List<Unity>? = this.getAllData(clazz)
            if (listUnity != null){
                val unityModelCollection: Collection<UnityModel> = this
                        .unityModelDataMapper
                        .transform(listUnity)
                subscriber.onNext(unityModelCollection as List<UnityModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }

    override fun unitySave(unity: MapperUnity): Observable<UnityModel> {
        val parcel: Parcel = Parcel.obtain()
        unity.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
        parcel.setDataPosition(0)

        return Observable.create { subscriber ->

            val clazz: Class<Unity> = Unity::class.java
            val newUnity = this.save(clazz, parcel, interactDatabaseListener)
            if (newUnity != null){
                val unityModel = this.unityModelDataMapper
                        .transform(newUnity)
                subscriber.onNext(unityModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }

        }
    }

    override fun unityGetById(id: String): Observable<UnityModel> {
        return Observable.create { subscriber ->
            val clazz: Class<Unity> = Unity::class.java
            val newUnity = this.getDataById(clazz, id)
            if (newUnity != null){
                val unityModel = this.unityModelDataMapper
                        .transform(newUnity)
                subscriber.onNext(unityModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }

        }
    }

    override fun unityGetByField(value: String, nameField: String):
            Observable<List<UnityModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<Unity> = Unity::class.java
            val listUnity: List<Unity>? = this.getDataByField(clazz,
                    nameField, value)
            if (listUnity != null){
                val unityModelCollection: Collection<UnityModel> = this
                        .unityModelDataMapper
                        .transform(listUnity)
                subscriber.onNext(unityModelCollection as List<UnityModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }

    override fun unityUpdate(): Observable<Boolean> {
        val list= CachingLruRepository
                .instance
                .getLru()
                .get(Constants.CACHE_LIST_DISTRICT_MODEL)
        return Observable.create { subscriber ->

            if (list != null && list is ArrayList<*>) {
                for (i in list.indices) {
                    val district = list[i] as DistrictModel
                    val listUnity = district.list
                    if (listUnity.isNotEmpty()){
                        for (j in listUnity.indices){
                            val unity = listUnity[j]
                            listUnity[j].id = saveUnity(unity)
                        }
                    }
                }
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }

    }

    private fun saveUnity(unity: UnityModel): String{
        var result = ""
        val clazz: Class<Unity> = Unity::class.java
        val listResult: List<Unity>? = this.getDataByField(clazz,
                ID_NET, unity.idNet)
        if (listResult == null) {
            val mapperUnity = MapperUnity()
            mapperUnity.phone = unity.phone
            mapperUnity.address = unity.address
            mapperUnity.name = unity.name
            mapperUnity.idNet = unity.idNet
            val parcel: Parcel = Parcel.obtain()
            mapperUnity.writeToParcel(parcel,
                    Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
            parcel.setDataPosition(0)
            val newUnity = this.save(clazz, parcel,
                    interactDatabaseListener)
            if (newUnity == null) {
                println(context.resources
                        .getString(R.string.error_save_database))
            }else{
                result = newUnity.id
            }
        }else{
            result = listResult[0].id
        }

        return result
    }
}