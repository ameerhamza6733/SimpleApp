package com.example.myapplication.model.remote

class CurrenciesResponse(val countryCode:String, val countryName:String) {
    override fun hashCode(): Int {
        return this.countryCode.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        other as CurrenciesResponse
        return this.countryCode.equals(other.countryCode)

    }
}