package com.cornershop.counterstest.data.counters.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cornershop.counterstest.domain.counters.entity.Counter
import kotlinx.coroutines.flow.Flow

@Dao
interface CountersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCountersList(countersList: List<Counter>)

    @Query("SELECT * FROM Counter")
    fun getCountersList(): Flow<List<Counter>>

    @Query("DELETE FROM Counter WHERE id NOT IN (:idsList)")
    suspend fun deleteCountersNotIn(idsList: List<String>)
}
