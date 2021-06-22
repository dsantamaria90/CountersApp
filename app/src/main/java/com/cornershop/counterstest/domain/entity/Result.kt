package com.cornershop.counterstest.domain.entity

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success
fun <T> Result<T>.isError(): Boolean = this is Result.Error
fun <T> Result<T>.getData(): T = (this as Result.Success).data
