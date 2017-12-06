package com.mobile.fodein.models.executor

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.App
import com.mobile.fodein.dagger.ModelsModule
import com.mobile.fodein.domain.data.MapperImage
import com.mobile.fodein.domain.repository.IImageRepository
import com.mobile.fodein.models.data.Image
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.models.persistent.repository.DatabaseRepository
import com.mobile.fodein.presentation.mapper.ImageModelDataMapper
import com.mobile.fodein.presentation.model.ImageModel
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageExecutor @Inject constructor():
        DatabaseRepository(), IImageRepository{



    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}

    @Inject
    lateinit var interactDatabaseListener: DatabaseListenerExecutor
    @Inject
    lateinit var imageModelDataMapper: ImageModelDataMapper


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

    override fun imageSave(image: MapperImage): Observable<ImageModel> {
        val parcel: Parcel = Parcel.obtain()
        image.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
        parcel.setDataPosition(0)

        return Observable.create { subscriber ->

            val clazz: Class<Image> = Image::class.java
            val newImage = this.save(clazz, parcel, interactDatabaseListener)
            if (newImage != null){
                val imageModel = this.imageModelDataMapper
                        .transform(newImage)
                subscriber.onNext(imageModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }

    override fun imageGetByField(value: String, nameField: String):
            Observable<List<ImageModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<Image> = Image::class.java
            val listImage: List<Image>? = this.getDataByField(clazz,
                    nameField, value)
            if (listImage != null){
                val imageModelCollection: Collection<ImageModel> = this
                        .imageModelDataMapper
                        .transform(listImage)
                subscriber.onNext(imageModelCollection as List<ImageModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }

    override fun imageGetByFieldBoolean(value: Boolean, nameField: String):
            Observable<List<ImageModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<Image> = Image::class.java
            val listImage: List<Image>? = this.getDataByFieldBoolean(clazz,
                    nameField, value)
            if (listImage != null){
                val imageModelCollection: Collection<ImageModel> = this
                        .imageModelDataMapper
                        .transform(listImage)
                subscriber.onNext(imageModelCollection as List<ImageModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }

    override fun addListImage(list: List<MapperImage>): Observable<Boolean> {
        return Observable.create { subscriber ->
            var result = true

            list.indices.forEach { i ->
                val image = list[i]
                if (!saveImageOfList(image)){
                    result = false
                }

            }
            if (result){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }

    private fun saveImageOfList(image: MapperImage): Boolean{
        val parcel: Parcel = Parcel.obtain()
        image.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
        parcel.setDataPosition(0)
        val clazz: Class<Image> = Image::class.java
        val newImage = this.save(clazz, parcel, interactDatabaseListener)
        if (newImage != null){
            return true
        }
        return false
    }

    override fun imageUpdateUpload(value: String): Observable<Boolean> {
        return Observable.create { subscriber ->
            val clazz: Class<Image> = Image::class.java
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