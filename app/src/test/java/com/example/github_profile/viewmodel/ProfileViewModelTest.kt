package com.example.github_profile.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.github_profile.data.profile.ProfileRepository
import com.example.github_profile.data.profile.ProfileRepositoryImpl
import com.example.github_profile.data.profile.model.GetUserProfileResponse
import com.example.github_profile.data.profile.model.GetUsersResponse
import com.example.github_profile.data.profile.remote.ProfileApi
import com.example.github_profile.helper.MockData
import com.example.github_profile.helper.RxImmediateSchedulerRule
import com.example.github_profile.utils.viewmodel.ResultWrapper
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.rules.TestRule

class ProfileViewModelTest {
    companion object
    {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()

        @ClassRule
        @JvmField
        var rule: TestRule = InstantTaskExecutorRule()
    }

    @MockK
    private lateinit var profileApi: ProfileApi

    private lateinit var profileRepository: ProfileRepository

    private lateinit var profileViewModel: ProfileViewModel

    @Before
    fun setup(){
        MockKAnnotations.init(this)

        profileRepository = ProfileRepositoryImpl(profileApi)

        profileViewModel = ProfileViewModel(profileRepository, CompositeDisposable())
    }

    @Test
    fun `Given since parameter, When call get users, Return response success`() {
        //Given
        val since = 1
        val slots = mutableListOf<ResultWrapper<List<GetUsersResponse>>>()
        val observer = spyk<Observer<ResultWrapper<List<GetUsersResponse>>>>()
        val expected = MockData.usersList

        //Mock
        profileViewModel.listUsers.observeForever(observer)
        coEvery { profileRepository.getUsers(since) } returns Single.just(MockData.usersList)

        profileViewModel.getUsers()

        //Then
        verify {
            observer.onChanged(capture(slots))
        }
        assert(slots[0] is ResultWrapper.Default)
        assert(slots[1] is ResultWrapper.Loading)
        assert(slots[2] is ResultWrapper.Success)
        assertEquals(expected, (slots[2] as ResultWrapper.Success).data)
    }

    @Test
    fun `Given Throwable, When call get users, Return error`() {
        //Given
        val throwable = Throwable()
        val slots = mutableListOf<ResultWrapper<List<GetUsersResponse>>>()
        val observer = spyk<Observer<ResultWrapper<List<GetUsersResponse>>>>()

        //Mock
        profileViewModel.listUsers.observeForever(observer)
        coEvery { profileRepository.getUsers(1) } returns Single.error(throwable)

        profileViewModel.getUsers()

        //Then
        verify {
            observer.onChanged(capture(slots))
        }
        assert(slots[0] is ResultWrapper.Default)
        assert(slots[1] is ResultWrapper.Loading)
        assert(slots[2] is ResultWrapper.Failure)
        assertEquals(throwable, (slots[2] as ResultWrapper.Failure).throwable)
    }

    @Test
    fun `Given list of usernames, When call get user detail, Return response success`() {
        //Given
        val usernames = MockData.usersList
        val slots = mutableListOf<ResultWrapper<List<GetUserProfileResponse>>>()
        val observer = spyk<Observer<ResultWrapper<List<GetUserProfileResponse>>>>()
        val expected = listOf(MockData.userDetail1, MockData.userDetail2)

        //Mock
        profileViewModel.listUserDetail.observeForever(observer)
        usernames.forEach {
            coEvery { profileRepository.getUserProfile(it.username) } returns Single.just(MockData.mapData[it.username])
        }

        profileViewModel.getUserProfile(usernames)

        //Then
        verify {
            observer.onChanged(capture(slots))
        }
        assert(slots[0] is ResultWrapper.Default)
        assert(slots[1] is ResultWrapper.Loading)
        assert(slots[2] is ResultWrapper.Success)
        assertEquals(expected, (slots[2] as ResultWrapper.Success).data)
    }

    @Test
    fun `Given Throwable, When call get user detail, Return error`() {
        //Given
        val throwable = Throwable()
        val usernames = MockData.usersList
        val slots = mutableListOf<ResultWrapper<List<GetUserProfileResponse>>>()
        val observer = spyk<Observer<ResultWrapper<List<GetUserProfileResponse>>>>()

        //Mock
        profileViewModel.listUserDetail.observeForever(observer)
        usernames.forEach {
            coEvery { profileRepository.getUserProfile(it.username) } returns Single.error(throwable)
        }

        profileViewModel.getUserProfile(usernames)

        //Then
        verify {
            observer.onChanged(capture(slots))
        }
        assert(slots[0] is ResultWrapper.Default)
        assert(slots[1] is ResultWrapper.Loading)
        assert(slots[2] is ResultWrapper.Failure)
        assertEquals(throwable, (slots[2] as ResultWrapper.Failure).throwable)
    }
}