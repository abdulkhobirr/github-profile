package com.example.github_profile.data.profile

import com.example.github_profile.data.profile.model.GetUserProfileResponse
import com.example.github_profile.data.profile.model.GetUsersResponse
import io.reactivex.Single

interface ProfileRepository {
    fun getUsers(since: Int): Single<List<GetUsersResponse>>

    fun getUserProfile(username: String): Single<GetUserProfileResponse>
}