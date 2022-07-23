package com.example.myapplication.room

import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.myapplication.model.local.CurrenciesModelLocal
import com.example.myapplication.model.remote.CurrenciesResponse
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CurrencyDaoTest {

    private lateinit var database: CurrencyDataBase
    private lateinit var dao: CurrencyDao
    private val TAG="CurrencyDaoTest"

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CurrencyDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = database.currencyDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertCurrencyItem() = runBlocking {

        val supportedCurrenciesList =
            listOf(CurrenciesResponse("PKR", "Pakistan"), CurrenciesResponse("INR", "India"))
        val currenciesModelLocal = CurrenciesModelLocal(
            "testSuppotedCurrency", System.currentTimeMillis(),
            supportedCurrenciesList
        )
        dao.insertSupportedCurrency(currenciesModelLocal)
        val allSupportedCurrencyFromLocal = dao.getSupportedCurrency()

        allSupportedCurrencyFromLocal?.currenciesRespons?.forEach {
            Log.d(TAG,"${it.countryCode}")
        }
        assertThat(supportedCurrenciesList.map { it.countryCode }).isNotEmpty()

    }

    //this should return null if there is no currency
    @Test
    fun getExchangeRateByCurrencyIfNull() = runBlocking {

        val exchangeRates = dao.getExchangeRateByCurrencies("PKR")
        assertThat(exchangeRates).isNull()
    }

    @Test
    fun getSupportedCurrencyListIfNull() = runBlocking {
        val supportedCurrenciesModelLocal = dao.getSupportedCurrency()
        assertThat(supportedCurrenciesModelLocal).isNull()
    }




}