package com.example.github_profile.data.remote

import com.example.github_profile.helper.MockData
import com.example.github_profile.data.profile.remote.ProfileApi
import com.example.github_profile.data.profile.remote.ProfileApiClient
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class ProfileApiTest {

    @MockK
    private lateinit var profileApiClient: ProfileApiClient

    private lateinit var profileApi: ProfileApi

    @Before
    fun setup(){
        MockKAnnotations.init(this)

        profileApi = ProfileApi(profileApiClient)
    }

    @Test
    fun `Given since parameter, When get users data, Returns users data`(){
        val since = 1
        //Mock
        coEvery { profileApi.getUsers(since) } returns Single.just(MockData.usersList)

        //When
        val response = profileApi.getUsers(since).blockingGet()

        //Then
        assert(response.isNotEmpty())
    }

    @Test
    fun `Given username parameter, When get user detail, Returns user detail data`(){
        //Given
        val username = "username"

        //Mock
        coEvery { profileApi.getUserProfile(username) } returns Single.just(MockData.userDetail)

        //When
        val response = profileApi.getUserProfile(username).blockingGet()

        //Then
        assert(response == MockData.userDetail)
    }
}