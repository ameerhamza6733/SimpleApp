package com.example.myapplication.repository

import android.util.Log
import com.example.myapplication.util.Utils
import com.example.myapplication.dateasource.CurrencieRemoteDataSource
import com.example.myapplication.di.RetrofitProviderModule
import com.example.myapplication.model.local.CurrenciesModelLocal
import com.example.myapplication.model.local.ExchangeRateModelLocal
import com.example.myapplication.model.remote.ExchangeRateReponse
import com.example.myapplication.model.request.ExchangeRateRequest
import com.example.myapplication.networkBoundResource
import com.example.myapplication.room.CurrencyDataBase
import javax.inject.Inject


class CurrencyRepository @Inject constructor(
    private val db: CurrencyDataBase,
    private val currencieRemoteDataSource: CurrencieRemoteDataSource
) {
    private val TAG = "CurrencyRepository"

    fun getSupportedCurrency() = networkBoundResource(query = {
        db.currencyDao().getSupportedCurrency()
    }, getFromNetwork = {
        CurrenciesModelLocal(currenciesRespons = currencieRemoteDataSource.refreshCurrencyListCrash())
    }, saveToLocalDb = {
        db.currencyDao().insertSupportedCurrency(it!!)
    }, shouldUpdateCach = {dateFromCache->
        if (dateFromCache == null) {
            true
        } else {
            Utils.getTimeDifferenceInMints(dateFromCache.lastRefreshTime) >= RetrofitProviderModule.THRESHOLD_API_REFRESH_TIME_MINTS
        }

    })

    fun getExchangeRate(exchangeRateRequest: ExchangeRateRequest) = networkBoundResource(query = {
        //try to load exchange rate from local if not found try to get usd
        db.currencyDao().getExchangeRateByCurrencies(exchangeRateRequest.baseCurrencie) ?: convertCurrencyIntoUsdBasedOnHisUsdRate(db.currencyDao().getExchangeRateByCurrencies("USD")?.exchangeRateReponse,exchangeRateRequest)
    }, getFromNetwork = {
        if (exchangeRateRequest.equals("USD")) {
            ExchangeRateModelLocal(
                baseCurrencie = exchangeRateRequest.baseCurrencie,
                exchangeRateReponse = currencieRemoteDataSource.getCurrencyEchangeRate(
                    exchangeRateRequest
                )
            )
        } else {
            //if this is not usd then we get usd rate and converter that rate into currency because in free account only usd rates are provided
            val response = currencieRemoteDataSource.getCurrencyEchangeRate(
                ExchangeRateRequest("USD")
            )
            convertCurrencyIntoUsdBasedOnHisUsdRate(exchangeRateReponse =response,exchangeRateRequest )
        }
    }, saveToLocalDb = {
        db.currencyDao().insertExchangeRatesByCurrency(it!!)
    }, shouldUpdateCach = {dateFromCache->
        if (dateFromCache == null) {
            true
        } else {
            Utils.getTimeDifferenceInMints(
                dateFromCache.lastRefreshTime
            ) >= RetrofitProviderModule.THRESHOLD_API_REFRESH_TIME_MINTS

        }


    })

     private fun convertCurrencyIntoUsdBasedOnHisUsdRate(exchangeRateReponse: ExchangeRateReponse?, exchangeRateRequest: ExchangeRateRequest):ExchangeRateModelLocal?{
        val baseCurrencyInUsdRate = exchangeRateReponse?.rates?.get(exchangeRateRequest.baseCurrencie)
      return if (baseCurrencyInUsdRate==null){
            null
       }else{
           val map: HashMap<String, Double> = HashMap()
           exchangeRateReponse.rates.forEach { (currency, rate) ->
               map.put(currency, rate / baseCurrencyInUsdRate)
           }
           exchangeRateReponse.rates = map
           exchangeRateReponse.base = exchangeRateRequest.baseCurrencie
           ExchangeRateModelLocal(
               baseCurrencie = exchangeRateRequest.baseCurrencie,
               exchangeRateReponse = exchangeRateReponse
           )
       }
    }

}