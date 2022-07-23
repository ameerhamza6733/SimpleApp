package com.example.myapplication.room.converters

import androidx.room.TypeConverter
import com.example.myapplication.model.remote.CurrenciesResponse
import com.example.myapplication.model.remote.ExchangeRateReponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ExchangeRateLocalTypeConverter {
    @TypeConverter
    fun fromString(value: String?): Map<String,Double>? {
        val listType: Type = object : TypeToken<Map<String,Double>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: Map<String,Double>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringExchangeRateReponse(value: String?): ExchangeRateReponse? {
        val listType: Type = object : TypeToken<ExchangeRateReponse>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromExchangeRateReponse(exchangeRateReponse: ExchangeRateReponse?): String? {
        return Gson().toJson(exchangeRateReponse)
    }
}