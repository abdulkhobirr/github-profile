package com.example.github_profile.data

import com.example.github_profile.helper.MockData
import com.example.github_profile.data.profile.ProfileRepository
import com.example.github_profile.data.profile.ProfileRepositoryImpl
import com.example.github_profile.data.profile.remote.ProfileApi
import com.example.github_profile.data.profile.remote.ProfileApiClient
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class ProfileRepositoryImplTest  {

    @MockK
    private lateinit var profileApiClient: ProfileApiClient

    private lateinit var profileApi: ProfileApi

    private lateinit var profileRepository: ProfileRepository

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        profileApi = ProfileApi(profileApiClient)
        profileRepository = ProfileRepositoryImpl(profileApi)
    }

    @Test
    fun `Given since parameter, When get users data, Returns users data`(){
        val since = 1
        //Mock
        coEvery { profileRepository.getUsers(since) } returns Single.just(MockData.usersList)

        //When
        val response = profileRepository.getUsers(since).map { it }.blockingGet()

        //Then
        assert(response.isNotEmpty())
    }

    @Test
    fun `Given username parameter, When get user detail, Returns user detail data`(){
        //Given
        val username = "username"

        //Mock
        coEvery { profileRepository.getUserProfile(username) } returns Single.just(MockData.userDetail)

        //When
        val response = profileRepository.getUserProfile(username).map { it }.blockingGet()

        //Then
        assert(response == MockData.userDetail)
    }
}