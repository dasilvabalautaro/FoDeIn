package com.mobile.fodein.models.executor

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.App
import com.mobile.fodein.dagger.ModelsModule
import com.mobile.fodein.domain.data.MapperDistrict
import com.mobile.fodein.domain.repository.IDistrictRepository
import com.mobile.fodein.models.data.District
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.models.persistent.repository.DatabaseRepository
import com.mobile.fodein.presentation.mapper.DistrictModelDataMapper
import com.mobile.fodein.presentation.model.DistrictModel
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DistrictExecutor @Inject constructor():
        DatabaseRepository(), IDistrictRepository {

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule())}

    @Inject
    lateinit var interactDatabaseListener: DatabaseListenerExecutor
    @Inject
    lateinit var districtModelDataMapper: DistrictModelDataMapper

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

    override fun districtList(): Observable<List<DistrictModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<District> = District::class.java
            val listDistrict: List<District>? = this.getAllData(clazz)
            if (listDistrict != null){
                val districtModelCollection: Collection<DistrictModel> = this
                        .districtModelDataMapper
                        .transform(listDistrict)
                subscriber.onNext(districtModelCollection as List<DistrictModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }

    }

    override fun districtSave(district: MapperDistrict): Observable<DistrictModel> {
        val parcel: Parcel = Parcel.obtain()
        district.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
        parcel.setDataPosition(0)

        return Observable.create { subscriber ->

            val clazz: Class<District> = District::class.java
            val newDistrict = this.save(clazz, parcel, interactDatabaseListener)
            if (newDistrict != null){
                val districtModel = this.districtModelDataMapper
                        .transform(newDistrict)
                subscriber.onNext(districtModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }

        }
    }

    override fun districtGetById(id: String): Observable<DistrictModel> {
        return Observable.create { subscriber ->
            val clazz: Class<District> = District::class.java
            val newDistrict = this.getDataById(clazz, id)
            if (newDistrict != null){
                val districtModel = this.districtModelDataMapper
                        .transform(newDistrict)
                subscriber.onNext(districtModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }

        }
    }

    override fun districtGetByField(value: String,
                                    nameField: String):
            Observable<List<DistrictModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<District> = District::class.java
            val listDistrict: List<District>? = this.getDataByField(clazz,
                    nameField, value)
            if (listDistrict != null){
                val usersModelCollection: Collection<DistrictModel> = this
                        .districtModelDataMapper
                        .transform(listDistrict)
                subscriber.onNext(usersModelCollection as List<DistrictModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }
}