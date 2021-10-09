package com.example.github_profile.di

import com.example.github_profile.BuildConfig
import com.example.github_profile.BuildConfig.GITHUB_API_BASE_URL
import com.example.github_profile.utils.data.OkHttpClientFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module

val apiModule = module {

    single {
        return@single OkHttpClientFactory.create(
                showDebugLog = BuildConfig.DEBUG
        )
    }

    single(named(GITHUB_API_BASE_URL)) { GITHUB_API_BASE_URL }
}