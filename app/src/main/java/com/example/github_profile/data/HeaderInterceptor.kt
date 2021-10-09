package com.example.github_profile.data

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val token: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val request = original.newBuilder()
            .addHeader("Authorization", "token " + token)
            .addHeader("User-Agent", "request")
            .addHeader("Accept", "application/vnd.github.v3+json")
            .build()

        return chain.proceed(request)
    }
}