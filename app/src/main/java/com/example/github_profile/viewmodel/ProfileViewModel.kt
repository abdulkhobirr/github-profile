package com.example.github_profile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github_profile.data.profile.ProfileRepository
import com.example.github_profile.data.profile.model.GetUserProfileResponse
import com.example.github_profile.data.profile.model.GetUsersResponse
import com.example.github_profile.utils.viewmodel.ResultWrapper
import com.example.github_profile.utils.viewmodel.addTo
import com.example.github_profile.utils.viewmodel.genericErrorHandler
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val disposable: CompositeDisposable
): ViewModel() {
    val listUser = MutableLiveData<ResultWrapper<List<GetUserProfileResponse>>>()
    var since: Int = 1

    init {
        listUser.value = ResultWrapper.default()
    }

    fun getUsers(){
        listUser.value = ResultWrapper.loading()
        repository.getUsers(since)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getUserProfile(it)
            }, {
                genericErrorHandler(it, listUser)
            })
            .addTo(disposable)
    }

    fun getUserProfile(list: List<GetUsersResponse>){
        val getProfile: MutableList<Observable<GetUserProfileResponse>> = mutableListOf()
        val tempList = mutableListOf<GetUserProfileResponse>()

        for (item in list){
            getProfile.add(repository.getUserProfile(item.username).toObservable())
        }

        Observable.concat(getProfile)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onNext = {
                    tempList.add(it)
                }, onError = {
                    genericErrorHandler(it, listUser)
                }, onComplete = {
                    incrementSince()
                    listUser.value = ResultWrapper.success(tempList)
                }
            ).addTo(disposable)
    }

    fun getSinceCount(): Int {
        return since
    }

    fun resetSince(){
        since = 1
    }

    private fun incrementSince(){
        since += 10
    }

    override fun onCleared() {
        if (!disposable.isDisposed) disposable.dispose()
        super.onCleared()
    }
}