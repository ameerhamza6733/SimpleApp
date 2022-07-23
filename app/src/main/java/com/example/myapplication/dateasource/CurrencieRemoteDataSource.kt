package com.example.myapplication.dateasource

import android.util.Log
import com.example.myapplication.exception.GetRatesByCurrencyException
import com.example.myapplication.exception.GetSupportedCurrencyListExcetion
import com.example.myapplication.exception.getGetCustomExceptionFromErrorCode
import com.example.myapplication.model.remote.CurrenciesResponse
import com.example.myapplication.model.remote.ExchangeRateReponse
import com.example.myapplication.model.request.ExchangeRateRequest
import com.example.myapplication.webservices.Api
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import javax.inject.Inject

class CurrencieRemoteDataSource @Inject constructor(private val api: Api) {
    private val TAG="CurrencieRemoteDataSource"
    @Throws(Exception::class)
    suspend fun refreshCurrencyListCrash(): List<CurrenciesResponse> {

        return kotlin.runCatching {
            val reponseBodyString = api.getCurrencyList()
           if (reponseBodyString?.isSuccessful==true){
               val type: Type = object : TypeToken<Map<String, String>>() {}.getType()
               val rawBytes=reponseBodyString.body()?.bytes()?: throw GetSupportedCurrencyListExcetion("Response body null")
               val currencyMap = Gson().fromJson<Map<String,String>>(String(rawBytes), type)
               currencyMap?.map { CurrenciesResponse(it.key, it.value) }?: throw GetSupportedCurrencyListExcetion("Response body null")
           }else{
               throw getGetCustomExceptionFromErrorCode(reponseBodyString?.code()?:-1,reponseBodyString?.errorBody())
           }
        }.onFailure {
            it.printStackTrace()
            throw it
        }.getOrThrow()

    }

    @Throws(Exception::class)
    suspend fun getCurrencyEchangeRate(exchangeRateRequest: ExchangeRateRequest): ExchangeRateReponse? {
        return kotlin.runCatching {
            val reponse= api.getExchangeRateByCurrency(exchangeRateRequest.baseCurrencie)
            if (reponse?.isSuccessful==true){
                val type: Type = object : TypeToken<ExchangeRateReponse>() {}.getType()
                val rawBytes=reponse.body()?.bytes()?: throw GetRatesByCurrencyException("Response body null")
                val currencyMap = Gson().fromJson<ExchangeRateReponse>(String(rawBytes), type)
                currencyMap
            }else{
                throw getGetCustomExceptionFromErrorCode(reponse?.code()?:-1,reponse?.errorBody())
            }
         }.onFailure {
            it.printStackTrace()
            throw it
         }.getOrThrow()
    }


}