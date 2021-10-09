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
    val listUserDetail = MutableLiveData<ResultWrapper<List<GetUserProfileResponse>>>()
    val listUsers = MutableLiveData<ResultWrapper<List<GetUsersResponse>>>()
    var since: Int = 1

    init {
        listUsers.value = ResultWrapper.default()
        listUserDetail.value = ResultWrapper.default()
    }

    fun getUsers(){
        listUsers.value = ResultWrapper.loading()
        repository.getUsers(since)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                listUsers.value = ResultWrapper.success(it)
            }, {
                genericErrorHandler(it, listUsers)
            })
            .addTo(disposable)
    }

    fun getUserProfile(list: List<GetUsersResponse>){
        listUserDetail.value = ResultWrapper.loading()
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
                    genericErrorHandler(it, listUserDetail)
                }, onComplete = {
                    listUserDetail.value = ResultWrapper.success(tempList)
                }
            ).addTo(disposable)
    }

    fun getSinceCount(): Int {
        return since
    }

    fun resetSince(){
        since = 1
    }


    fun updateSince(update: Int){
        since = update
    }

    override fun onCleared() {
        if (!disposable.isDisposed) disposable.dispose()
        super.onCleared()
    }
}