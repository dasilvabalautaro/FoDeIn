package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.domain.interactor.GetFormListUseCase
import javax.inject.Inject


class FormPresenter @Inject constructor(private val getFormListUseCase:
                                        GetFormListUseCase):
        BasePresenter(){
    init {
        this.iHearMessage = getFormListUseCase
    }
}