package com.example.github_profile.utils.viewmodel

sealed class ResultWrapper<T> {
    class Loading<T> : ResultWrapper<T>()
    class Default<T> : ResultWrapper<T>()
    class Empty<T> : ResultWrapper<T>()
    data class Success<T>(val data: T) : ResultWrapper<T>()
    data class Failure<T>(val throwable: Throwable?, val title: String, val message: String?) : ResultWrapper<T>()

    companion object {
        fun <T> loading(): ResultWrapper<T> = Loading()
        fun <T> default(): ResultWrapper<T> = Default()
        fun <T> success(data: T): ResultWrapper<T> = Success(data)
        fun <T> empty(): ResultWrapper<T> = Empty()
        fun <T> fail(throwable: Throwable, title: String, message: String?): ResultWrapper<T> =
            Failure(throwable, title, message)
    }
}