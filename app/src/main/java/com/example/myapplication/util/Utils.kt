package com.example.myapplication.util

import android.view.View
import com.example.myapplication.isInternetError
import com.example.myapplication.model.ui.CurrenciesListModelUI
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.TimeUnit

class Utils {
    companion object {
        fun getTimeDifferenceInMints(dateLong: Long?): Long {
            if (dateLong == null) {
                return 0
            } else {
                val diffInMilli = System.currentTimeMillis() - dateLong
                val differInSecond = TimeUnit.MILLISECONDS.toMinutes(diffInMilli)
                return differInSecond
            }

        }


        fun convert(
            rates: Map<String, Double>?,
            baseCurrencies: String?,
            amount: Double
        ): List<CurrenciesListModelUI> {
            val listModelUI = ArrayList<CurrenciesListModelUI>()
            rates?.forEach { (currency, rate) ->
                listModelUI.add(
                    CurrenciesListModelUI(
                        baseCurrencies,
                        baseCurrenciesInto = "$currency ",
                        amountConversion = String.format("%.2f", amount * rate)
                    )
                )
            }
            return listModelUI
        }


        fun showError(throwable: Throwable?, view: View,  retryClick: () -> Unit) {
            if (throwable?.isInternetError() == true) {
                Snackbar.make(
                    view,
                    "Internal network error",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("tryAgain") {
                    retryClick()
                }.show()
            } else {
                Snackbar.make(
                    view,
                    "${throwable?.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}