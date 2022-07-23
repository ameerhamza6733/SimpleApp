package com.example.myapplication.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.model.local.CurrenciesModelLocal
import com.example.myapplication.model.local.ExchangeRateModelLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSupportedCurrency(currenciesModelLocal: CurrenciesModelLocal)

    //this table have only one row, so it will will retrun only one recode even we query to whole table
    @Query("SELECT * FROM currenciesmodellocal")
    fun getSupportedCurrency(): CurrenciesModelLocal?

    @Query("SELECT * FROM exchangeratemodellocal WHERE baseCurrencie == :curreny")
    fun getExchangeRateByCurrencies(curreny: String):ExchangeRateModelLocal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExchangeRatesByCurrency(exchangeRateModelLocal:ExchangeRateModelLocal)


}