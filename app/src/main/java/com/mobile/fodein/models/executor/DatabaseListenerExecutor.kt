package com.mobile.fodein.models.executor

import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.models.interfaces.OnDatabaseCompleteListener
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject


class DatabaseListenerExecutor :
        OnDatabaseCompleteListener {

    private val context = App.appComponent.context()

    private var message: String = ""
    var observableMessage: Subject<String> = PublishSubject.create()
    private var error: DatabaseOperationException? = null
    var observableException:
            Subject<DatabaseOperationException> = PublishSubject.create()

    init {
        this.observableMessage
                .subscribe { this.message }
        this.observableException
                .subscribe { this.error }
    }

    override fun onSaveFailed(error: String) {
        this.error = DatabaseOperationException(error)
        this.observableException.onNext(this.error!!)
    }

    override fun onSaveSucceeded() {
        this.message = context.getString(R.string.save_data)
        this.observableMessage.onNext(this.message)
    }

    override fun onDeleteCompleted() {
        this.message = context.getString(R.string.delete_data)
        this.observableMessage.onNext(this.message)
    }

    override fun onDeleteFailed(error: String) {
        this.error = DatabaseOperationException(error)
        this.observableException.onNext(this.error!!)
    }

    override fun onUpdateCompleted() {
        this.message = context.getString(R.string.update_data)
        this.observableMessage.onNext(this.message)
    }

    override fun onUpdateFailed(error: String) {
        this.error = DatabaseOperationException(error)
        this.observableException.onNext(this.error!!)
    }

}
