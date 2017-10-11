package com.mobile.fodein.models.executor

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.App
import com.mobile.fodein.dagger.ModelsModule
import com.mobile.fodein.domain.data.MapperUser
import com.mobile.fodein.domain.repository.IUserRepository
import com.mobile.fodein.models.data.User
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.models.persistent.repository.DatabaseRepository
import com.mobile.fodein.presentation.mapper.UserModelDataMapper
import com.mobile.fodein.presentation.model.UserModel
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserExecutor @Inject constructor():
        DatabaseRepository(), IUserRepository {

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule())}

    @Inject
    lateinit var interactDatabaseListener: DatabaseListenerExecutor
    @Inject
    lateinit var userModelDataMapper: UserModelDataMapper

    init {
        component.inject(this)
    }

    override fun userGetError(): Observable<DatabaseOperationException> {
        return this.interactDatabaseListener
                .observableException.map { e -> e }

    }

    override fun userGetMessage(): Observable<String>{
       return this.interactDatabaseListener
                .observableMessage.map { s -> s }
    }

    override fun userList(): Observable<List<UserModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<User> = User::class.java
            val listUsers: List<User>? = this.getAllData(clazz)
            if (listUsers != null){
                val usersModelCollection: Collection<UserModel> = this
                    .userModelDataMapper
                    .transform(listUsers)
                subscriber.onNext(usersModelCollection as List<UserModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }

    override fun userSave(user: MapperUser): Observable<UserModel>{
        val parcel:Parcel = Parcel.obtain()
        user.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
        parcel.setDataPosition(0)

        return Observable.create { subscriber ->

            val clazz: Class<User> = User::class.java
            val newUser = this.save(clazz, parcel, interactDatabaseListener)
            if (newUser != null){
                val userModel = this.userModelDataMapper.transform(newUser)

                subscriber.onNext(userModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }

        }

    }

    override fun userGetById(id: String): Observable<User> {
        return Observable.create { subscriber ->
            val clazz: Class<User> = User::class.java
            val newUser = this.getDataById(clazz, id)
            if (newUser != null){
                subscriber.onNext(newUser)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }

        }
    }

    override fun userGetByField(value: String, nameField: String):
            Observable<List<UserModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<User> = User::class.java
            val listUsers: List<User>? = this.getDataByField(clazz,
                    nameField, value)
            if (listUsers != null){
                val usersModelCollection: Collection<UserModel> = this
                        .userModelDataMapper
                        .transform(listUsers)
                subscriber.onNext(usersModelCollection as List<UserModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }
}