package com.example.github_profile.di

import com.example.github_profile.data.profile.ProfileRepository
import com.example.github_profile.data.profile.ProfileRepositoryImpl
import com.example.github_profile.data.profile.remote.ProfileApi
import com.example.github_profile.data.profile.remote.ProfileApiClient
import com.example.github_profile.utils.data.ApiService
import com.example.github_profile.viewmodel.ProfileViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object GithubProfileModule {

    @ViewModelScoped
    @Provides
    @Named("profileApiClient")
    fun provideRetrofitService(
        @Named("baseurl") baseUrl: String,
        @Named("okhttp") okHttpClient: OkHttpClient
    ): ProfileApiClient {
        return ApiService.createReactiveService(
            ProfileApiClient::class.java,
            okHttpClient,
            baseUrl
        )
    }

    @ViewModelScoped
    @Provides
    @Named("profileApi")
    fun provideProfileApi(
        @Named("profileApiClient") apiClient: ProfileApiClient
    ): ProfileApi {
        return ProfileApi(apiClient)
    }

    @ViewModelScoped
    @Provides
    @Named("profileRepository")
    fun provideProfileRepository(
        @Named("profileApi") api: ProfileApi
    ): ProfileRepository {
        return ProfileRepositoryImpl(api)
    }

    @ViewModelScoped
    @Provides
    @Named("compositeDisposable")
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @ViewModelScoped
    @Provides
    fun provideProfileViewModel(
        @Named("profileRepository") repository: ProfileRepository,
        @Named("compositeDisposable") disposable: CompositeDisposable
    ): ProfileViewModel {
        return ProfileViewModel(repository, disposable)
    }
}