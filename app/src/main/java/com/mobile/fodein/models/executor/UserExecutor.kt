package com.mobile.fodein.models.executor

import com.mobile.fodein.App
import com.mobile.fodein.dagger.ModelsModule
import com.mobile.fodein.domain.repository.IUserRepository
import com.mobile.fodein.models.data.User
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.models.persistent.repository.DatabaseRepository
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserExecutor @Inject constructor():
        DatabaseRepository(), IUserRepository {

    private var disposable: CompositeDisposable = CompositeDisposable()

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule())}

    @Inject
    lateinit var interactDatabaseListener: DatabaseListenerExecutor

    private var message: String = ""
    var observableMessage: Subject<String> = PublishSubject.create()
    private var error: DatabaseOperationException? = null
    var observableException:
            Subject<DatabaseOperationException> = PublishSubject.create()

    init {
        component.inject(this)
        val hearMessage = this.interactDatabaseListener
                .observableMessage.map { s -> s }
        disposable.add(hearMessage.observeOn(Schedulers.newThread())
                .subscribe { s ->
                    this.message = s
                    this.observableMessage.onNext(this.message)
                })
        val hearError = this.interactDatabaseListener
                .observableException.map { e -> e }
        disposable.add(hearError.observeOn(Schedulers.newThread())
                .subscribe { e ->
                    this.error = e
                    this.observableException.onNext(this.error!!)
                })
        this.observableMessage
                .subscribe { this.message }
        this.observableException
                .subscribe { this.error }
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

    override fun userSave(user: User): Observable<User>{
        return Observable.create { subscriber ->
            val clazz: Class<User> = User::class.java
            clazz.cast(user)
            this.save(clazz, interactDatabaseListener)
            subscriber.onNext(user)
            subscriber.onComplete()
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

    fun dispose(){
        if(!disposable.isDisposed) disposable.dispose()
    }
}