package com.cornershop.counterstest.data.service

import com.cornershop.counterstest.data.entity.AddCounter
import com.cornershop.counterstest.data.entity.DeleteCounter
import com.cornershop.counterstest.domain.entity.Counter
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface CountersService {

    @GET("counters")
    suspend fun getCountersList(): List<Counter>

    @POST(COUNTER)
    suspend fun addCounter(addCounter: AddCounter): List<Counter>

    @DELETE(COUNTER)
    suspend fun deleteCounter(deleteCounter: DeleteCounter): List<Counter>

    @POST("$COUNTER/inc")
    suspend fun incrementCounter(id: String): List<Counter>

    @POST("$COUNTER/dec")
    suspend fun decrementCounter(id: String): List<Counter>

    companion object {
        private const val COUNTER = "counter"
    }
}
