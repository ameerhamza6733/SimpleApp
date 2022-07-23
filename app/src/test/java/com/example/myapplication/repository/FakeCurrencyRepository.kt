package com.example.myapplication.repository

import com.example.myapplication.di.RetrofitProviderModule
import com.example.myapplication.model.local.CurrenciesModelLocal
import com.example.myapplication.networkBoundResource
import com.example.myapplication.util.Utils
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException

class FakeCurrencyRepository {


    fun getSupportedCurrencyWhenDataBaseNullAndNetworkConnectionOffAndThrowEx() = networkBoundResource(query = {
       null
    }, getFromNetwork = {
       throw UnknownHostException()
    }, saveToLocalDb = {

    })



}