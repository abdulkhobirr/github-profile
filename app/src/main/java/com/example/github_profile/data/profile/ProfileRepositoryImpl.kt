package com.example.github_profile.data.profile

import com.example.github_profile.data.profile.model.GetUserProfileResponse
import com.example.github_profile.data.profile.model.GetUsersResponse
import com.example.github_profile.data.profile.remote.ProfileApi
import io.reactivex.Single

class ProfileRepositoryImpl (api: ProfileApi): ProfileRepository {
    private val webService = api

    override fun getUsers(since: Int): Single<List<GetUsersResponse>> {
        return webService.getUsers(since).map {
            it
        }
    }

    override fun getUserProfile(username: String): Single<GetUserProfileResponse> {
        return webService.getUserProfile(username).map {
            it
        }
    }
}