package com.mobile.fodein.models.executor

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.dagger.ModelsModule
import com.mobile.fodein.domain.data.MapperDistrict
import com.mobile.fodein.domain.repository.IDistrictRepository
import com.mobile.fodein.models.data.District
import com.mobile.fodein.models.data.Unity
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.models.persistent.repository.DatabaseRepository
import com.mobile.fodein.presentation.mapper.DistrictModelDataMapper
import com.mobile.fodein.presentation.model.DistrictModel
import com.mobile.fodein.tools.Constants
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DistrictExecutor @Inject constructor():
        DatabaseRepository(), IDistrictRepository {

    private val ID_NET = "idNet"
    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}

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

    override fun districtUpdate(): Observable<Boolean> {

        val list= CachingLruRepository
                .instance
                .getLru()
                .get(Constants.CACHE_LIST_DISTRICT_MODEL)
        return Observable.create { subscriber ->

            if (list != null && list is ArrayList<*>) {
                list.indices.forEach { i ->
                    val district = list[i] as DistrictModel
                    (list[i] as DistrictModel).id = saveDistrict(district)
                }
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }

    private fun saveDistrict(district: DistrictModel): String{
        var result = ""
        val clazz: Class<District> = District::class.java
        val listDistrict: List<District>? = this.getDataByField(clazz,
                ID_NET, district.idNet)
        if (listDistrict == null || listDistrict.isEmpty()) {
            val mapperDistrict = MapperDistrict()
            mapperDistrict.name = district.name
            mapperDistrict.idNet = district.idNet
            val parcel: Parcel = Parcel.obtain()
            mapperDistrict.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
            parcel.setDataPosition(0)
            val newDistrict = this.save(clazz, parcel, interactDatabaseListener)
            if (newDistrict == null) {
                println(context.resources.getString(R.string.error_save_database))
            }else{
                result = newDistrict.id

            }
        }else{
            result = listDistrict[0].id
        }
        return result
    }

    override fun addUnities(): Observable<Boolean> {
        val list= CachingLruRepository
                .instance
                .getLru()
                .get(Constants.CACHE_LIST_DISTRICT_MODEL)
        return Observable.create { subscriber ->


            if (list != null && list is ArrayList<*>) {
                list.indices.forEach { i ->
                    val district = list[i] as DistrictModel
                    executeAdd(district)
                }
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }

    private fun executeAdd(district: DistrictModel){
        val clazz: Class<Unity> = Unity::class.java
        val listUnity = district.list
        if (listUnity.isNotEmpty()){
            val idDistrict = district.id
            listUnity.indices.forEach { j ->
                val unity = listUnity[j]
                val newUnity = this.getDataById(clazz, unity.id)
                if (newUnity != null){
                    saveUnityInList(idDistrict, unity.id, newUnity)
                }

            }

        }
    }

    private fun saveUnityInList(idDistrict:String,
                                idUnity: String,
                                unity: Unity){
        val realm: Realm = Realm.getDefaultInstance()
        try {
            realm.executeTransaction {
                val newDistrict = realm.where(District::class.java).equalTo(
                        "id", idDistrict).findFirst()
                val unities: RealmList<Unity> = newDistrict!!.unities
                val filterUnities: RealmResults<Unity> = unities
                        .where()
                        .contains("id", idUnity)
                        .findAll()
                if (filterUnities.isEmpty()){
                    newDistrict.unities.add(unity)
                }
            }

        }catch (e: Throwable){
            println(e.message!!)
        }

    }
}