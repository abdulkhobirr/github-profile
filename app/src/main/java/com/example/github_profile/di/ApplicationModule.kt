package com.example.github_profile.di

import com.example.github_profile.BuildConfig
import com.example.github_profile.data.HeaderInterceptor
import com.example.github_profile.utils.data.OkHttpClientFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Singleton
    @Provides
    @Named("okhttp")
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClientFactory.create(
            showDebugLog = BuildConfig.DEBUG,
            interceptors = HeaderInterceptor(BuildConfig.TOKEN)
        )
    }

    @Singleton
    @Provides
    @Named("baseurl")
    fun provideGithubBaseUrl(): String {
        return BuildConfig.GITHUB_API_BASE_URL
    }
}