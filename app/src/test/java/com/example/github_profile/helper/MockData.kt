package com.example.github_profile.helper

import com.example.github_profile.data.profile.model.GetUserProfileResponse
import com.example.github_profile.data.profile.model.GetUsersResponse

object MockData {
    val usersList = listOf(
        GetUsersResponse("user1"),
        GetUsersResponse("user2")
    )

    val userDetail = GetUserProfileResponse(
        username = "username",
        userId = 1,
        avatarUrl = "https://avatars.githubusercontent.com/u/22088019?v=4",
        name = "user",
        email = "user1@mail.com",
        location = "Indonesia",
        createdAt = "2016-09-09T00:29:57Z"
    )

    val userDetail1 = GetUserProfileResponse(
        username = "username1",
        userId = 1,
        avatarUrl = "https://avatars.githubusercontent.com/u/22088019?v=4",
        name = "user1",
        email = "user1@mail.com",
        location = "Indonesia",
        createdAt = "2016-09-09T00:29:57Z"
    )

    val userDetail2 = GetUserProfileResponse(
        username = "username2",
        userId = 2,
        avatarUrl = "https://avatars.githubusercontent.com/u/22088019?v=4",
        name = "user2",
        email = "user1@mail.com",
        location = "Indonesia",
        createdAt = "2016-09-09T00:29:57Z"
    )

    val mapData = mapOf("user1" to userDetail1, "user2" to userDetail2)
}