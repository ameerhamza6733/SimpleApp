package com.example.myapplication.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.model.remote.CurrenciesResponse
import com.example.myapplication.model.remote.ExchangeRateReponse

@Entity
class ExchangeRateModelLocal(
    @PrimaryKey val baseCurrencie: String ,
    val lastRefreshTime: Long = System.currentTimeMillis(),
    var exchangeRateReponse: ExchangeRateReponse? = null
)