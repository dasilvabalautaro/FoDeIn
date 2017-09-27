package com.mobile.fodein.models.interactors

import com.mobile.fodein.App
import com.mobile.fodein.dagger.ModelsModule
import com.mobile.fodein.models.data.User
import com.mobile.fodein.models.persistent.repository.DatabaseRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject


class InteractUser @Inject constructor(private val user: User) :
        DatabaseRepository() {
    private var disposable: CompositeDisposable = CompositeDisposable()

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule())}

    @Inject
    lateinit var interactDatabaseListener: InteractDatabaseListener

    private var message: String = ""
    var observableMessage: Subject<String> = PublishSubject.create()

    init {
        component.inject(this)
        val hearMessage = this.interactDatabaseListener
                .observableMessage.map { s -> s }
        disposable.add(hearMessage.observeOn(Schedulers.newThread())
                .subscribe { s ->
                    this.message = s
                    this.observableMessage.onNext(this.message)
                })

        this.observableMessage
                .subscribe { this.message }
    }

    fun saveUser(){
        val clazz: Class<User> = User::class.java
        clazz.cast(user)
        this.save(clazz, interactDatabaseListener)
    }


}