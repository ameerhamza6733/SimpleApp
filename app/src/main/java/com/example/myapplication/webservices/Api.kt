package com.example.myapplication.webservices

import com.example.myapplication.model.remote.ExchangeRateReponse
import com.example.myapplication.model.request.ExchangeRateRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {

    @GET("api/currencies.json")
    suspend fun getCurrencyList():Response<ResponseBody>?

    @GET("api/latest.json")
    suspend fun getExchangeRateByCurrency(@Query("base") base:String):Response<ResponseBody>?


}