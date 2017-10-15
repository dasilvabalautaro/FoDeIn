package com.mobile.fodein.dagger

import android.content.Context
import com.mobile.fodein.domain.interactor.*
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.*
import com.mobile.fodein.models.executor.*
import com.mobile.fodein.presentation.UIThread
import com.mobile.fodein.presentation.presenter.DistrictPresenter
import com.mobile.fodein.presentation.presenter.UserListPresenter
import com.mobile.fodein.presentation.presenter.UserLoginPresenter
import com.mobile.fodein.presentation.presenter.UserPresenter
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
    fun provideDistrictPresenter(getDistrictListUseCase:
                                 GetDistrictListUseCase): DistrictPresenter{
        return DistrictPresenter(getDistrictListUseCase)
    }
}