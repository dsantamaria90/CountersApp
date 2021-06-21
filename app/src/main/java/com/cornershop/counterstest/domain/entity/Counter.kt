package com.cornershop.counterstest.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Counter(
    @PrimaryKey
    val id: String,
    val title: String,
    val count: Int
) {

    fun isDecrementable(): Boolean = count > EMPTY_COUNT

    companion object {
        private const val EMPTY_COUNT = 0
    }
}
