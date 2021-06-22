package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.entity.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class SuspendUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(parameters: P): Result<R> = try {
        withContext(coroutineDispatcher) {
            execute(parameters)
        }
    } catch (exception: Exception) {
        Result.Error(exception)
    }

    protected abstract suspend fun execute(parameters: P): Result<R>
}