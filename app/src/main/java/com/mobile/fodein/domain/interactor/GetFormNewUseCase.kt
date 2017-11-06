package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.data.MapperForm
import com.mobile.fodein.domain.interfaces.IHearMessage
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IFormRepository
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.presentation.model.FormModel
import com.mobile.fodein.tools.Constants
import io.reactivex.Observable
import javax.inject.Inject


class GetFormNewUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                            postExecutionThread: IPostExecutionThread,
                                            private var formRepository:
                                            IFormRepository):
        UseCase<FormModel>(threadExecutor, postExecutionThread),
        IHearMessage {
    var form: MapperForm = MapperForm()

    fun setForm(data: MutableMap<String, Any>){
        form.date = data[Constants.FORM_DATE].toString()
        form.latitude = data[Constants.FORM_LATITUDE] as Double
        form.longitude = data[Constants.FORM_LONGITUDE] as Double
        form.dateUpdate = data[Constants.FORM_DATE_UPDATE].toString()
        form.annotation = data[Constants.FORM_ANNOTATION].toString()
        form.annotationOne = data[Constants.FORM_ANNOTATION_ONE].toString()
        form.annotationTwo = data[Constants.FORM_ANNOTATION_TWO].toString()
        form.userid = data[Constants.FORM_USER].toString()
        form.project_id = data[Constants.FORM_PROJECT_NET].toString()
    }

    override fun hearMessage(): Observable<String> {
        return this.formRepository.userGetMessage()
    }

    override fun hearError(): Observable<DatabaseOperationException> {
        return this.formRepository.userGetError()
    }

    override fun buildUseCaseObservable(): Observable<FormModel> {
        return this.formRepository.formSave(form)
    }
}