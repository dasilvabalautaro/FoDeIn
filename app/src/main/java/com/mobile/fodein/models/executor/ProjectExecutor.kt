package com.mobile.fodein.models.executor

import android.os.Parcel
import android.os.Parcelable
import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.dagger.ModelsModule
import com.mobile.fodein.domain.data.MapperProject
import com.mobile.fodein.domain.repository.IProjectRepository
import com.mobile.fodein.models.data.Project
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.models.persistent.repository.DatabaseRepository
import com.mobile.fodein.presentation.mapper.ProjectModelDataMapper
import com.mobile.fodein.presentation.model.ProjectModel
import com.mobile.fodein.presentation.model.UnityModel
import com.mobile.fodein.tools.Constants
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectExecutor @Inject constructor():
        DatabaseRepository(), IProjectRepository {

    private val ID_NET = "idNet"
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
                val projectModel = this.projectModelDataMapper
                        .transform(newProject)
                subscriber.onNext(projectModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }

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

    override fun projectUpdate(): Observable<Boolean> {
        val list= CachingLruRepository
                .instance
                .getLru()
                .get(Constants.CACHE_LIST_UNITY_MODEL)
        return Observable.create { subscriber ->

            if (list != null && list is ArrayList<*>) {
                for (i in list.indices) {
                    val unity = list[i] as UnityModel
                    val listProject = unity.list
                    if (listProject.isNotEmpty()){
                        for (j in listProject.indices){
                            val project = listProject[j]
                            listProject[j].id = saveProject(project)
                        }
                    }
                }
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }
    private fun saveProject(project: ProjectModel): String{
        var result = ""
        val clazz: Class<Project> = Project::class.java
        val listResult: List<Project>? = this.getDataByField(clazz,
                ID_NET, project.idNet)
        if (listResult == null || listResult.isEmpty()) {
            val mapperProject = MapperProject()
            mapperProject.type = project.type
            mapperProject.code = project.code
            mapperProject.name = project.name
            mapperProject.latitude = project.latitude
            mapperProject.longitude = project.longitude
            mapperProject.finance = project.finance
            mapperProject.counterpart = project.counterpart
            mapperProject.notFinance = project.notFinance
            mapperProject.other = project.other
            mapperProject.sum = project.sum
            mapperProject.idNet = project.idNet

            val parcel: Parcel = Parcel.obtain()
            mapperProject.writeToParcel(parcel,
                    Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
            parcel.setDataPosition(0)
            val newProject = this.save(clazz, parcel,
                    interactDatabaseListener)
            if (newProject == null) {
                println(context.resources
                        .getString(R.string.error_save_database))
            }else{
                result = newProject.id
            }
        }else{
            result = listResult[0].id
        }

        return result
    }

    override fun addForm(idForm: String, idProject: String):
            Observable<Boolean> {
        return Observable.create { subscriber ->
            if (this.saveFormInList(idProject, idForm)) {
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }



}