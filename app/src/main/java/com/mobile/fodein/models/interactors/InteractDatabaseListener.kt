package com.mobile.fodein.models.interactors

import android.content.Context
import com.mobile.fodein.R
import com.mobile.fodein.models.interfaces.OnDatabaseCompleteListener
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject


class InteractDatabaseListener @Inject constructor(
        private val context: Context):
        OnDatabaseCompleteListener {
    private var message: String = ""
    var observableMessage: Subject<String> = PublishSubject.create()

    init {
        this.observableMessage
                .subscribe { this.message }
    }

    override fun onSaveFailed(error: String) {
        this.message = error
        this.observableMessage.onNext(this.message)
    }

    override fun onSaveSucceeded() {
        this.message = context!!.getString(R.string.save_data)
        this.observableMessage.onNext(this.message)
    }

    override fun onDeleteCompleted() {
        this.message = context!!.getString(R.string.delete_data)
        this.observableMessage.onNext(this.message)
    }

    override fun onDeleteFailed(error: String) {
        this.message = error
        this.observableMessage.onNext(this.message)
    }

    override fun onUpdateCompleted() {
        this.message = context!!.getString(R.string.update_data)
        this.observableMessage.onNext(this.message)
    }

    override fun onUpdateFailed(error: String) {
        this.message = error
        this.observableMessage.onNext(this.message)
    }

}