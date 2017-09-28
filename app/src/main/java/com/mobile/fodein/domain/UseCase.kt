package com.mobile.fodein.domain

import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


abstract class UseCase<T> protected constructor(private val threadExecutor:
                                                IThreadExecutor,
                                                private val postExecutionThread:
                                                IPostExecutionThread) {

    private var disposable: CompositeDisposable = CompositeDisposable()

    protected abstract fun buildUseCaseObservable(): Observable<T>


    fun execute(observer: DisposableObserver<T>) {
        val observable: Observable<T> = this.buildUseCaseObservable()
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.scheduler)
        disposable.add(observable.subscribeWith(observer))
    }


    fun dispose() {
        if (!disposable.isDisposed) disposable.dispose()
    }
}