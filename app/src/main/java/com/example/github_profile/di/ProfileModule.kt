
package com.example.github_profile.di

import com.example.github_profile.BuildConfig.GITHUB_API_BASE_URL
import com.example.github_profile.data.profile.ProfileRepository
import com.example.github_profile.data.profile.ProfileRepositoryImpl
import com.example.github_profile.data.profile.remote.ProfileApi
import com.example.github_profile.data.profile.remote.ProfileApiClient
import com.example.github_profile.utils.data.ApiService
import com.example.github_profile.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val profileModule = module {
    single {
        ApiService.createReactiveService(
                ProfileApiClient::class.java,
                get(),
                get(named(GITHUB_API_BASE_URL))
        )
    }

    single { ProfileApi(get()) }

    single<ProfileRepository> { ProfileRepositoryImpl(get()) }

    viewModel { ProfileViewModel(get(), get()) }
}