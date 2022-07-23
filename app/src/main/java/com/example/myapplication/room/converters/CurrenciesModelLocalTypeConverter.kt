package com.example.myapplication.room.converters

import androidx.room.TypeConverter
import com.example.myapplication.model.remote.CurrenciesResponse
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class CurrenciesModelLocalTypeConverter {
    @TypeConverter
    fun fromString(value: String?): List<CurrenciesResponse>? {
        val listType: Type = object : TypeToken<List<CurrenciesResponse>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<CurrenciesResponse>?): String? {
        return Gson().toJson(list)
    }

}