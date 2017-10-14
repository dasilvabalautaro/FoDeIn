package com.mobile.fodein.domain.repository

import com.mobile.fodein.domain.data.MapperProject
import com.mobile.fodein.models.interfaces.IMessagePersistent
import com.mobile.fodein.presentation.model.ProjectModel
import io.reactivex.Observable


interface IProjectRepository: IMessagePersistent {
    fun projectList(): Observable<List<ProjectModel>>
    fun projectSave(project: MapperProject): Observable<ProjectModel>
    fun projectGetById(id: String): Observable<ProjectModel>
    fun projectGetByField(value: String, nameField: String): Observable<List<ProjectModel>>

}