package com.mobile.fodein.models.executor

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.App
import com.mobile.fodein.dagger.ModelsModule
import com.mobile.fodein.domain.data.MapperForm
import com.mobile.fodein.domain.repository.IFormRepository
import com.mobile.fodein.models.data.Form
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.models.persistent.repository.DatabaseRepository
import com.mobile.fodein.presentation.mapper.FormModelDataMapper
import com.mobile.fodein.presentation.model.FormModel
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormExecutor @Inject constructor():
        DatabaseRepository(), IFormRepository{

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}

    @Inject
    lateinit var interactDatabaseListener: DatabaseListenerExecutor
    @Inject
    lateinit var formModelDataMapper: FormModelDataMapper

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

    override fun formList(): Observable<List<FormModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<Form> = Form::class.java
            val listForm: List<Form>? = this.getAllData(clazz)
            if (listForm != null){
                val formModelCollection: Collection<FormModel> = this
                        .formModelDataMapper
                        .transform(listForm)
                subscriber.onNext(formModelCollection as List<FormModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }

    }

    override fun formSave(form: MapperForm): Observable<FormModel> {
        val parcel: Parcel = Parcel.obtain()
        form.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
        parcel.setDataPosition(0)

        return Observable.create { subscriber ->

            val clazz: Class<Form> = Form::class.java
            val newForm = this.save(clazz, parcel, interactDatabaseListener)
            if (newForm != null){
                val formModel = this.formModelDataMapper
                        .transform(newForm)
                subscriber.onNext(formModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }

    }


    override fun formGetById(id: String): Observable<FormModel> {
        return Observable.create { subscriber ->
            val clazz: Class<Form> = Form::class.java
            val newForm = this.getDataById(clazz, id)
            if (newForm != null){
                val formModel = this.formModelDataMapper
                        .transform(newForm)
                subscriber.onNext(formModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }

        }

    }

    override fun formGetByField(value: String, nameField: String): 
            Observable<List<FormModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<Form> = Form::class.java
            val listForm: List<Form>? = this.getDataByField(clazz,
                    nameField, value)
            if (listForm != null){
                val formModelCollection: Collection<FormModel> = this
                        .formModelDataMapper
                        .transform(listForm)
                subscriber.onNext(formModelCollection as List<FormModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }

    }
    override fun formGetByFieldBoolean(value: Boolean, nameField: String):
            Observable<List<FormModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<Form> = Form::class.java
            val listForm: List<Form>? = this.getDataByFieldBoolean(clazz,
                    nameField, value)
            if (listForm != null){
                val formModelCollection: Collection<FormModel> = this
                        .formModelDataMapper
                        .transform(listForm)
                subscriber.onNext(formModelCollection as List<FormModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }
    override fun formUpdateUpload(value: String): Observable<Boolean>  {
        return Observable.create { subscriber ->
            val clazz: Class<Form> = Form::class.java
             if (this.updateUpload(value,  clazz,
                     interactDatabaseListener)){
                 subscriber.onNext(true)
                 subscriber.onComplete()
             }else{
                subscriber.onError(Throwable())
            }
        }
    }

}