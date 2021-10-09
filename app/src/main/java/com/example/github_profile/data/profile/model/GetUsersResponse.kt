package com.example.github_profile.data.profile.model

import com.google.gson.annotations.SerializedName

data class GetUsersResponse (
        @SerializedName("login") val username: String
        )