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

    override fun userList(): Observable<List<User>> {
        return Observable.create { subscriber ->
            val clazz: Class<User> = User::class.java
            val listUsers: List<User>? = this.getAllData(clazz)
            if (listUsers != null){
                subscriber.onNext(listUsers)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }

    override fun userSave(user: MapperUser): Observable<MapperUser>{
        val parcel:Parcel = Parcel.obtain()
        user.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
        parcel.setDataPosition(0)

        return Observable.create { subscriber ->

            val clazz: Class<User> = User::class.java
            val newUser = this.save(clazz, parcel, interactDatabaseListener)
            if (newUser != null){
                user.user = newUser.id
                subscriber.onNext(user)
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

}