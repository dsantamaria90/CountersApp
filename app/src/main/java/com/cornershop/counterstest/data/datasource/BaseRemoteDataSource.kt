package com.cornershop.counterstest.data.datasource

import com.cornershop.counterstest.domain.entity.Result

abstract class BaseRemoteDataSource {

    protected suspend fun <T> getResult(call: suspend () -> T): Result<T> = try {
        Result.Success(call())
    } catch (exception: Exception) {
        Result.Error(exception)
    }
}