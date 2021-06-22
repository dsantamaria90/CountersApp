package com.cornershop.counterstest.data.counters.service

import com.cornershop.counterstest.data.counters.entity.AddCounter
import com.cornershop.counterstest.data.counters.entity.ModifyCounter
import com.cornershop.counterstest.domain.counters.entity.Counter
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface CountersService {

    @GET("counters")
    suspend fun getCountersList(): List<Counter>

    @POST(COUNTER)
    suspend fun addCounter(@Body addCounter: AddCounter): List<Counter>

    @HTTP(method = "DELETE", path = COUNTER, hasBody = true)
    suspend fun deleteCounter(@Body modifyCounter: ModifyCounter): List<Counter>

    @POST("$COUNTER/inc")
    suspend fun incrementCounter(@Body modifyCounter: ModifyCounter): List<Counter>

    @POST("$COUNTER/dec")
    suspend fun decrementCounter(@Body modifyCounter: ModifyCounter): List<Counter>

    companion object {
        private const val COUNTER = "counter"
    }
}
