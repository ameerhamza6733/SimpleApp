package com.example.myapplication.model.remote

class ExchangeRateReponse(
    val timestamp: Long,
    var base: String,
    var rates:Map<String,Double>
)