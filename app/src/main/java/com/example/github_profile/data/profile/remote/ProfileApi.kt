package com.example.github_profile.data.profile.remote

import com.example.github_profile.data.profile.model.GetUserProfileResponse
import com.example.github_profile.data.profile.model.GetUsersResponse
import io.reactivex.Single

class ProfileApi(private val apiClient: ProfileApiClient): ProfileApiClient {
    override fun getUsers(since: Int): Single<List<GetUsersResponse>> {
        return apiClient.getUsers(since)
    }

    override fun getUserProfile(username: String): Single<GetUserProfileResponse> {
        return apiClient.getUserProfile(username)
    }
}