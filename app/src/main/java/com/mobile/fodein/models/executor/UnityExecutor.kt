package com.mobile.fodein.models.executor

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.App
import com.mobile.fodein.dagger.ModelsModule
import com.mobile.fodein.domain.data.MapperUnity
import com.mobile.fodein.domain.repository.IUnityRepository
import com.mobile.fodein.models.data.District
import com.mobile.fodein.models.data.Unity
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.models.persistent.repository.DatabaseRepository
import com.mobile.fodein.presentation.mapper.UnityModelDataMapper
import com.mobile.fodein.presentation.model.UnityModel
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnityExecutor @Inject constructor():
        DatabaseRepository(), IUnityRepository {

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule())}

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
                addListPattern(newUnity)
                val unityModel = this.unityModelDataMapper
                        .transform(newUnity)
                subscriber.onNext(unityModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }

        }
    }

    private fun addListPattern(unity: Unity){
        val clazz: Class<District> = District::class.java
        val idDistrict = unity.district!!.id
        this.addObjectList(clazz, idDistrict, unity, interactDatabaseListener)
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

}