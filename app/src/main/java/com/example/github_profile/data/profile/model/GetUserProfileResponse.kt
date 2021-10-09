package com.example.github_profile.data.profile.model

import com.google.gson.annotations.SerializedName

data class GetUserProfileResponse(
        @SerializedName("login")
        val username: String,
        @SerializedName("id")
        val userId: Int,
        @SerializedName("avatar_url")
        val avatarUrl: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("location")
        val location: String,
        @SerializedName("created_at")
        val createdAt: String
)