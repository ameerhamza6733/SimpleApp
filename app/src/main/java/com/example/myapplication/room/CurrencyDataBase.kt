package com.example.myapplication.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.model.local.CurrenciesModelLocal
import com.example.myapplication.model.local.ExchangeRateModelLocal
import com.example.myapplication.room.converters.CurrenciesModelLocalTypeConverter
import com.example.myapplication.room.converters.ExchangeRateLocalTypeConverter

@Database(entities = [CurrenciesModelLocal::class,ExchangeRateModelLocal::class], version = 3)
@TypeConverters(CurrenciesModelLocalTypeConverter::class, ExchangeRateLocalTypeConverter::class)
abstract class CurrencyDataBase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}