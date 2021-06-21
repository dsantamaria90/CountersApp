package com.cornershop.counterstest.data.service

import com.cornershop.counterstest.data.entity.AddCounter
import com.cornershop.counterstest.data.entity.ModifyCounter
import com.cornershop.counterstest.domain.entity.Counter
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface CountersService {

    @GET("counters")
    suspend fun getCountersList(): List<Counter>

    @POST(COUNTER)
    suspend fun addCounter(@Body addCounter: AddCounter): List<Counter>

    @DELETE(COUNTER)
    suspend fun deleteCounter(@Body modifyCounter: ModifyCounter): List<Counter>

    @POST("$COUNTER/inc")
    suspend fun incrementCounter(@Body modifyCounter: ModifyCounter): List<Counter>

    @POST("$COUNTER/dec")
    suspend fun decrementCounter(@Body modifyCounter: ModifyCounter): List<Counter>

    companion object {
        private const val COUNTER = "counter"
    }
}
