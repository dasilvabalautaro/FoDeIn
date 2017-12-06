package com.mobile.fodein.dagger

import android.content.Context
import com.mobile.fodein.domain.interactor.*
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.*
import com.mobile.fodein.models.executor.*
import com.mobile.fodein.models.persistent.network.ServiceRemoteGet
import com.mobile.fodein.models.persistent.network.ServiceRemotePost
import com.mobile.fodein.presentation.UIThread
import com.mobile.fodein.presentation.presenter.*
import com.mobile.fodein.tools.ConnectionNetwork
import dagger.Module
import dagger.Provides


@Module
class PresentationModule(val context: Context) {

    @Provides
    fun provideJobExecutor(): JobExecutor{
        return JobExecutor()
    }

    @Provides
    fun provideThreadExecutor(jobExecutor: JobExecutor): IThreadExecutor {
        return jobExecutor
    }

    @Provides
    fun provideUIThread(): UIThread{
        return UIThread()
    }
    @Provides
    fun providePostExecutionThread(uiThread: UIThread): IPostExecutionThread {
        return uiThread
    }

    @Provides
    fun provideUserExecutor(): UserExecutor{
        return UserExecutor()
    }

    @Provides
    fun provideUserRepository(userExecutor: UserExecutor): IUserRepository {
        return userExecutor
    }

    @Provides
    fun provideGetUserNewUseCase(uiThread: UIThread,
                                 jobExecutor: JobExecutor,
                                 userExecutor: UserExecutor): GetUserNewUseCase {
        return GetUserNewUseCase(jobExecutor, uiThread, userExecutor)
    }

    @Provides
    fun provideGetUserListUseCase(uiThread: UIThread,
                                  jobExecutor: JobExecutor,
                                  userExecutor: UserExecutor): GetUserListUseCase{
        return GetUserListUseCase(jobExecutor, uiThread, userExecutor)
    }

    @Provides
    fun provideGetUserLoginUseCase(uiThread: UIThread,
                                   jobExecutor: JobExecutor,
                                   userExecutor: UserExecutor): GetUserLoginUseCase{
        return GetUserLoginUseCase(jobExecutor, uiThread, userExecutor)
    }

    @Provides
    fun provideUserLoginPresenter(getUserLoginUseCase:
                                  GetUserLoginUseCase): UserLoginPresenter{
        return UserLoginPresenter(getUserLoginUseCase)
    }
    @Provides
    fun provideUserPresenter(getUserNewUseCase:
                             GetUserNewUseCase): UserPresenter {
        return UserPresenter(getUserNewUseCase)
    }

    @Provides
    fun provideUserRegisterPresenter(getUserLoginUseCase:
                                     GetUserLoginUseCase,
                                     getUserNewUseCase:
                                     GetUserNewUseCase):
            UserRegisterPresenter{
        return UserRegisterPresenter(getUserLoginUseCase, getUserNewUseCase)
    }

    @Provides
    fun provideUserListPresenter(getUserListUseCase:
                                 GetUserListUseCase):UserListPresenter{
        return UserListPresenter(getUserListUseCase)
    }

    @Provides
    fun provideDistrictExecutor(): DistrictExecutor{
        return DistrictExecutor()
    }

    @Provides
    fun provideDistrictRepository(districtExecutor: DistrictExecutor):
            IDistrictRepository {
        return districtExecutor
    }

    @Provides
    fun provideGetDistrictListUseCase(uiThread: UIThread,
                                      jobExecutor: JobExecutor,
                                      districtExecutor: DistrictExecutor):
            GetDistrictListUseCase {
        return GetDistrictListUseCase(jobExecutor, uiThread, districtExecutor)
    }

    @Provides
    fun provideUpdateDistrictListUseCase(uiThread: UIThread,
                                         jobExecutor: JobExecutor,
                                         districtExecutor: DistrictExecutor):
            UpdateDistrictListUseCase{
        return UpdateDistrictListUseCase(jobExecutor, uiThread, districtExecutor)
    }

    @Provides
    fun provideAddUnitsDistrictListUseCase(uiThread: UIThread,
                                           jobExecutor: JobExecutor,
                                           districtExecutor: DistrictExecutor):
            AddUnitsDistrictListUseCase{
        return AddUnitsDistrictListUseCase(jobExecutor, uiThread, districtExecutor)
    }

    @Provides
    fun provideUnityExecutor(): UnityExecutor{
        return UnityExecutor()
    }

    @Provides
    fun provideUnityRepository(unityExecutor: UnityExecutor):
            IUnityRepository {
        return unityExecutor
    }

    @Provides
    fun provideGetUnityListUseCase(uiThread: UIThread,
                                   jobExecutor: JobExecutor,
                                   unityExecutor: UnityExecutor):
            GetUnityListUseCase{
        return GetUnityListUseCase(jobExecutor, uiThread, unityExecutor)
    }

    @Provides
    fun provideUpdateUnityListUseCase(uiThread: UIThread,
                                      jobExecutor: JobExecutor,
                                      unityExecutor: UnityExecutor):
            UpdateUnityListUseCase{
        return UpdateUnityListUseCase(jobExecutor, uiThread, unityExecutor)
    }

    @Provides
    fun provideAddProjectsUnitsListUseCase(uiThread: UIThread,
                                           jobExecutor: JobExecutor,
                                           unityExecutor: UnityExecutor):
            AddProjectsUnitsListUseCase{
        return AddProjectsUnitsListUseCase(jobExecutor, uiThread, unityExecutor)
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
    fun provideGetProjectListUseCase(uiThread: UIThread,
                                     jobExecutor: JobExecutor,
                                     projectExecutor: ProjectExecutor):
            GetProjectListUseCase{
        return GetProjectListUseCase(jobExecutor, uiThread, projectExecutor)
    }

    @Provides
    fun provideUpdateProjectListUseCase(uiThread: UIThread,
                                        jobExecutor: JobExecutor,
                                        projectExecutor: ProjectExecutor):
            UpdateProjectListUseCase{
        return UpdateProjectListUseCase(jobExecutor, uiThread, projectExecutor)
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
    fun provideGetFormListUseCase(uiThread: UIThread,
                                  jobExecutor: JobExecutor,
                                  formExecutor: FormExecutor): GetFormListUseCase{
        return GetFormListUseCase(jobExecutor, uiThread, formExecutor)
    }

    @Provides
    fun provideFormPresenter(getFormListUseCase:
                             GetFormListUseCase): FormPresenter{
        return FormPresenter(getFormListUseCase)
    }

    @Provides
    fun provideDistrictPresenter(getDistrictListUseCase:
                                 GetDistrictListUseCase): DistrictPresenter{
        return DistrictPresenter(getDistrictListUseCase)
    }

    @Provides
    fun provideUnityPresenter(getUnityListUseCase:
                              GetUnityListUseCase): UnityPresenter{
        return UnityPresenter(getUnityListUseCase)
    }

    @Provides
    fun provideProjectPresenter(getProjectListUseCase:
                                GetProjectListUseCase): ProjectPresenter{
        return ProjectPresenter(getProjectListUseCase)
    }

    @Provides
    fun provideConnectionNetwork(): ConnectionNetwork {
        return ConnectionNetwork(context)
    }

    @Provides
    fun provideServiceRemotePost(): ServiceRemotePost {
        return ServiceRemotePost()
    }

    @Provides
    fun provideServiceRemoteGet(): ServiceRemoteGet {
        return ServiceRemoteGet()
    }

    @Provides
    fun provideRequestLoginGetUseCase(serviceRemoteGet: ServiceRemoteGet,
                                      connectionNetwork:
                                      ConnectionNetwork):
            RequestLoginGetUseCase {
        return RequestLoginGetUseCase(serviceRemoteGet, connectionNetwork)
    }

    @Provides
    fun provideUserLoginNetworkPresenter(requestLoginGetUseCase:
                                         RequestLoginGetUseCase):
            UserLoginNetworkPresenter{
        return UserLoginNetworkPresenter(requestLoginGetUseCase)
    }

    @Provides
    fun provideRequestRegisterPostUseCase(serviceRemotePost: ServiceRemotePost,
                                          connectionNetwork:
                                          ConnectionNetwork):
            RequestRegisterPostUseCase{
        return RequestRegisterPostUseCase(serviceRemotePost, connectionNetwork)
    }

    @Provides
    fun provideUserRegisterNetworkPresenter(requestRegisterPostUseCase:
                                            RequestRegisterPostUseCase):
            UserRegisterNetworkPresenter{
        return UserRegisterNetworkPresenter(requestRegisterPostUseCase)
    }

    @Provides
    fun provideRequestDistrictGetUseCase(serviceRemoteGet: ServiceRemoteGet,
                                         connectionNetwork:
                                         ConnectionNetwork):
            RequestDistrictGetUseCase{
        return RequestDistrictGetUseCase(serviceRemoteGet, connectionNetwork)
    }

    @Provides
    fun provideRequestUnitsGetUseCase(serviceRemoteGet: ServiceRemoteGet,
                                      connectionNetwork:
                                      ConnectionNetwork):
            RequestUnitsGetUseCase{
        return RequestUnitsGetUseCase(serviceRemoteGet, connectionNetwork)
    }

    @Provides
    fun provideRequestProjectsGetUseCase(serviceRemoteGet:
                                         ServiceRemoteGet,
                                         connectionNetwork:
                                         ConnectionNetwork):
            RequestProjectsGetUseCase{
        return RequestProjectsGetUseCase(serviceRemoteGet, connectionNetwork)
    }

    @Provides
    fun provideProjectNetworkPresenter(requestProjectsGetUseCase:
                                       RequestProjectsGetUseCase,
                                       updateProjectListUseCase:
                                       UpdateProjectListUseCase,
                                       addProjectsUnitsListUseCase:
                                       AddProjectsUnitsListUseCase):
            ProjectNetworkPresenter{
        return ProjectNetworkPresenter(requestProjectsGetUseCase,
                updateProjectListUseCase, addProjectsUnitsListUseCase)
    }

    @Provides
    fun provideDistrictNetworkPresenter(requestDistrictGetUseCase:
                                        RequestDistrictGetUseCase,
                                        updateDistrictListUseCase:
                                        UpdateDistrictListUseCase):
            DistrictNetworkPresenter{
        return DistrictNetworkPresenter(requestDistrictGetUseCase,
                updateDistrictListUseCase)
    }

    @Provides
    fun provideUnityNetworkPresenter(requestUnitsGetUseCase:
                                     RequestUnitsGetUseCase,
                                     updateUnityListUseCase:
                                     UpdateUnityListUseCase,
                                     addUnitsDistrictListUseCase:
                                     AddUnitsDistrictListUseCase):
            UnityNetworkPresenter{
        return UnityNetworkPresenter(requestUnitsGetUseCase,
                updateUnityListUseCase, addUnitsDistrictListUseCase)
    }

}