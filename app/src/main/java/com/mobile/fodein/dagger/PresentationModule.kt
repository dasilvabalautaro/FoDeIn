package com.mobile.fodein.dagger

import android.content.Context
import com.mobile.fodein.domain.interactor.GetUserNewUseCase
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IUserRepository
import com.mobile.fodein.models.executor.JobExecutor
import com.mobile.fodein.models.executor.UserExecutor
import com.mobile.fodein.presentation.UIThread
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
    fun provideUserPresenter(getUserNewUseCase:
                             GetUserNewUseCase): UserPresenter {
        return UserPresenter(getUserNewUseCase)
    }

}