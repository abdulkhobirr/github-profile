package com.example.github_profile.data.profile.remote

import com.example.github_profile.data.profile.model.GetUserProfileResponse
import com.example.github_profile.data.profile.model.GetUsersResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileApiClient {
    @GET("/users?per_page=10")
    fun getUsers(
            @Query("since") since:Int
    ): Single<List<GetUsersResponse>>

    @GET("/user/{username}")
    fun getUserProfile(
            @Path("username") username: String
    ): Single<GetUserProfileResponse>
}