package com.example.myapplication.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.model.remote.CurrenciesResponse

@Entity
 open class CurrenciesModelLocal(
    @PrimaryKey val key: String = KEY_SUPPORTED_CURRENCY,
    val lastRefreshTime: Long = System.currentTimeMillis(),
    var currenciesRespons: List<CurrenciesResponse>?=null
) {
    companion object {
        val KEY_SUPPORTED_CURRENCY = "supported_currency"
    }
}