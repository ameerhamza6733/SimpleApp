package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Log
import com.example.myapplication.util.Resource
import com.example.myapplication.util.Utils
import com.example.myapplication.model.local.CurrenciesModelLocal
import com.example.myapplication.model.local.ExchangeRateModelLocal
import com.example.myapplication.model.request.ExchangeRateRequest
import com.example.myapplication.model.ui.CurrenciesListModelUI
import com.example.myapplication.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterFragmentViewModel @Inject constructor(private val currencyRepository: CurrencyRepository) :
    ViewModel() {

    init {
        getSupportedCurrency()
    }

    private val _supportedCurrencyMutableLiveData: MutableLiveData<Resource<out CurrenciesModelLocal?>> =
        MutableLiveData()
    var supportedCurrencyLiveData: LiveData<Resource<out CurrenciesModelLocal?>> =
        _supportedCurrencyMutableLiveData

    private val _userSelectedCurrencyMutableLive: MutableLiveData<String> = MutableLiveData()
    val liveUserSelectedCurrency: LiveData<String> = _userSelectedCurrencyMutableLive

    private val _currenciesExchangeRateMutableLiveData: MutableLiveData<Resource<out List<CurrenciesListModelUI>>> =
        MutableLiveData()
    val exchangeRateLiveData: LiveData<Resource<out List<CurrenciesListModelUI>>> =
        _currenciesExchangeRateMutableLiveData

    private val _amountMutableLiveData: MutableLiveData<Double?> = MutableLiveData()
    val amountLiveData: LiveData<Double?> = _amountMutableLiveData

    val currenciesRecyclerViewPosition=0

    fun getSupportedCurrency() {
        Log("getSupportedCurrency")
        viewModelScope.launch(Dispatchers.IO) {
            currencyRepository.getSupportedCurrency().collect {
                _supportedCurrencyMutableLiveData.postValue(it)
            }
        }
    }

     fun getExchangeRates(){
        viewModelScope.launch(Dispatchers.IO) {
            currencyRepository.getExchangeRate(ExchangeRateRequest(liveUserSelectedCurrency.value.toString())).map {
                return@map   transformExchangedRates(it,amountLiveData.value?:0.0)
            }.collect {
                _currenciesExchangeRateMutableLiveData.postValue(it)
            }
        }
    }

    fun setUserCurrency(currencyCode: String) {
        _userSelectedCurrencyMutableLive.value = currencyCode
       getExchangeRates()
    }

    private fun transformExchangedRates(it: Resource<out ExchangeRateModelLocal?>, amount:Double): Resource<List<CurrenciesListModelUI>> {
        return when (it) {
            is Resource.Success -> {
                Resource.Success( Utils.convert(it.data?.exchangeRateReponse?.rates,it.data?.baseCurrencie,amount))
            }
            is Resource.Error -> {
                Resource.Error(it.error,  Utils.convert(it.data?.exchangeRateReponse?.rates,it.data?.baseCurrencie,amount), it.errorMessage.toString())
            }
            else -> {
                Resource.Loading((it as Resource.Loading).message, null)
            }
        }
    }

    fun setNewAmount(double: Double) {
        _amountMutableLiveData.value=double
    }
}