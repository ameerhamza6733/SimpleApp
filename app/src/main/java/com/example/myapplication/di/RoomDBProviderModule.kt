package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.room.CurrencyDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDBProviderModule {
    @Singleton
    @Provides
    fun getDataBase(@ApplicationContext app: Context): CurrencyDataBase {
        return Room.databaseBuilder(
            app,
            CurrencyDataBase::class.java,
            "database"
        ).fallbackToDestructiveMigration().build()
    }
}