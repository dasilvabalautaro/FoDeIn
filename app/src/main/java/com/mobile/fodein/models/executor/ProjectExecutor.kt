package com.mobile.fodein.models.executor

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.App
import com.mobile.fodein.dagger.ModelsModule
import com.mobile.fodein.domain.data.MapperProject
import com.mobile.fodein.domain.repository.IProjectRepository
import com.mobile.fodein.models.data.Project
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.models.persistent.repository.DatabaseRepository
import com.mobile.fodein.presentation.mapper.ProjectModelDataMapper
import com.mobile.fodein.presentation.model.ProjectModel
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectExecutor @Inject constructor():
        DatabaseRepository(), IProjectRepository {

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}

    @Inject
    lateinit var interactDatabaseListener: DatabaseListenerExecutor
    @Inject
    lateinit var projectModelDataMapper: ProjectModelDataMapper

    init {
        component.inject(this)
    }

    override fun userGetMessage(): Observable<String> {
        return this.interactDatabaseListener
                .observableMessage.map { s -> s }

    }

    override fun userGetError(): Observable<DatabaseOperationException> {
        return this.interactDatabaseListener
                .observableException.map { e -> e }

    }

    override fun projectList(): Observable<List<ProjectModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<Project> = Project::class.java
            val listProject: List<Project>? = this.getAllData(clazz)
            if (listProject != null){
                val projectModelCollection: Collection<ProjectModel> = this
                        .projectModelDataMapper
                        .transform(listProject)
                subscriber.onNext(projectModelCollection as List<ProjectModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }

    }

    override fun projectSave(project: MapperProject): Observable<ProjectModel> {
        val parcel: Parcel = Parcel.obtain()
        project.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
        parcel.setDataPosition(0)

        return Observable.create { subscriber ->

            val clazz: Class<Project> = Project::class.java
            val newProject = this.save(clazz, parcel, interactDatabaseListener)
            if (newProject != null){
//                addListPattern(newProject)
                val projectModel = this.projectModelDataMapper
                        .transform(newProject)
                subscriber.onNext(projectModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }

//    private fun addListPattern(project: Project){
//        val clazz: Class<Unity> = Unity::class.java
//        val idUnity = project.unity!!.id
//        this.addObjectList(clazz, idUnity, project, interactDatabaseListener)
//    }

    override fun projectGetById(id: String): Observable<ProjectModel> {
        return Observable.create { subscriber ->
            val clazz: Class<Project> = Project::class.java
            val newProject = this.getDataById(clazz, id)
            if (newProject != null){
                val projectModel = this.projectModelDataMapper
                        .transform(newProject)
                subscriber.onNext(projectModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }

        }
    }

    override fun projectGetByField(value: String, nameField: String):
            Observable<List<ProjectModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<Project> = Project::class.java
            val listProject: List<Project>? = this.getDataByField(clazz,
                    nameField, value)
            if (listProject != null){
                val districtModelCollection: Collection<ProjectModel> = this
                        .projectModelDataMapper
                        .transform(listProject)
                subscriber.onNext(districtModelCollection as List<ProjectModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }


}