package com.mobile.fodein.dagger

import android.support.v7.app.AppCompatActivity
import com.mobile.fodein.domain.interactor.*
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IFormRepository
import com.mobile.fodein.domain.repository.IProjectRepository
import com.mobile.fodein.models.executor.FormExecutor
import com.mobile.fodein.models.executor.ImageExecutor
import com.mobile.fodein.models.executor.JobExecutor
import com.mobile.fodein.models.executor.ProjectExecutor
import com.mobile.fodein.models.persistent.network.ServiceRemotePost
import com.mobile.fodein.presentation.UIThread
import com.mobile.fodein.presentation.navigation.Navigator
import com.mobile.fodein.presentation.presenter.*
import com.mobile.fodein.presentation.view.component.ManageImages
import com.mobile.fodein.presentation.view.component.ManageMaps
import com.mobile.fodein.tools.LocationUser
import com.mobile.fodein.tools.PermissionUtils
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    fun provideNavigator(): Navigator{
        return Navigator(activity)
    }

    @Provides
    fun provideManageImages(): ManageImages{
        return ManageImages(activity)
    }

    @Provides
    fun providePermissionUtils(): PermissionUtils{
        return PermissionUtils()
    }

    @Provides
    fun provideManageMaps(): ManageMaps{
        return ManageMaps(activity)
    }

    @Provides
    fun provideJobExecutor(): JobExecutor {
        return JobExecutor()
    }

    @Provides
    fun provideThreadExecutor(jobExecutor: JobExecutor): IThreadExecutor {
        return jobExecutor
    }

    @Provides
    fun provideUIThread(): UIThread {
        return UIThread()
    }
    @Provides
    fun providePostExecutionThread(uiThread: UIThread): IPostExecutionThread {
        return uiThread
    }

    @Provides
    fun provideFormExecutor(): FormExecutor {
        return FormExecutor()
    }

    @Provides
    fun provideFormRepository(formExecutor: FormExecutor):
            IFormRepository {
        return formExecutor
    }

    @Provides
    fun provideGetFormNewUseCase(uiThread: UIThread,
                                 jobExecutor: JobExecutor,
                                 formExecutor: FormExecutor):
            GetFormNewUseCase{
        return GetFormNewUseCase(jobExecutor, uiThread, formExecutor)
    }

    @Provides
    fun provideFormNewPresenter(getFormNewUseCase:
                                GetFormNewUseCase): FormNewPresenter{
        return FormNewPresenter(getFormNewUseCase)
    }

    @Provides
    fun provideProjectExecutor(): ProjectExecutor {
        return ProjectExecutor()
    }

    @Provides
    fun provideProjectRepository(projectExecutor: ProjectExecutor):
            IProjectRepository {
        return projectExecutor
    }

    @Provides
    fun provideAddFormProjectListUseCase(uiThread: UIThread,
                                         jobExecutor: JobExecutor,
                                         projectExecutor: ProjectExecutor):
            AddFormProjectListUseCase{
        return AddFormProjectListUseCase(jobExecutor, uiThread, projectExecutor)
    }

    @Provides
    fun provideAddFormProjectListPresenter(addFormProjectListUseCase:
                                           AddFormProjectListUseCase):
            AddFormProjectListPresenter{
        return AddFormProjectListPresenter(addFormProjectListUseCase)
    }

    @Provides
    fun provideGetProjectListUseCase(uiThread: UIThread,
                                     jobExecutor: JobExecutor,
                                     projectExecutor: ProjectExecutor):
            GetProjectListUseCase {
        return GetProjectListUseCase(jobExecutor, uiThread, projectExecutor)
    }

    @Provides
    fun provideProjectPresenter(getProjectListUseCase:
                                GetProjectListUseCase): ProjectPresenter {
        return ProjectPresenter(getProjectListUseCase)
    }

    @Provides
    fun provideServiceRemotePost(): ServiceRemotePost {
        return ServiceRemotePost()
    }

    @Provides
    fun provideRequestRegisterFormPostUseCase(serviceRemotePost:
                                              ServiceRemotePost):
            RequestRegisterFormPostUseCase{
        return RequestRegisterFormPostUseCase(serviceRemotePost)
    }

    @Provides
    fun proviceFormRegisterNetworkPresenter(requestRegisterFormPostUseCase:
                                            RequestRegisterFormPostUseCase):
            FormRegisterNetworkPresenter{
        return FormRegisterNetworkPresenter(requestRegisterFormPostUseCase)
    }

    @Provides
    fun provideImageExecutor(): ImageExecutor{
        return ImageExecutor()
    }

    @Provides
    fun provideAddImageListUseCase(uiThread: UIThread,
                                   jobExecutor: JobExecutor,
                                   imageExecutor: ImageExecutor): AddImageListUseCase{
        return AddImageListUseCase(jobExecutor, uiThread, imageExecutor)
    }

    @Provides
    fun provideAddImageListPresenter(addImageListUseCase:
                                     AddImageListUseCase): AddImageListPresenter{
        return AddImageListPresenter(addImageListUseCase)
    }

    @Provides
    fun provideLocationUser():LocationUser{
        return LocationUser(activity)
    }
}