package com.zhd.dompet.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

/**
 * Format integer to currency
 * (Double) 5000.75 -> (String) Rp 5.000,75
 */
fun Number.toCurrency(showCurrency: Boolean = true, currency: String = "Rp "): String {
    val symbolConfig = if (currency.uppercase().startsWith("RP")) {
        val decFormat = DecimalFormatSymbols.getInstance()
        decFormat.groupingSeparator = '.'
        decFormat.decimalSeparator = ','
        decFormat
    } else {
        DecimalFormatSymbols.getInstance(Locale.US)
    }

    symbolConfig.currencySymbol = ""


    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
    formatter.decimalFormatSymbols = symbolConfig
    formatter.maximumFractionDigits = 3

    return if (showCurrency) {
        "$currency ${formatter.format(this)}"
    } else {
        formatter.format(this)
    }
}

/**
 * Parse formatter currency to Int
 * (String) Rp 5.000,52 -> (Double) 5000.52
 */
fun String.fromCurrency(currencySymbol: String = "Rp "): Double {
    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
    val clean = if (currencySymbol.lowercase().startsWith("rp")) {
        this.replace(Regex("[^0-9,]"), "")
            .replace(",", ".")
    } else {
        this.replace(Regex("[^0-9.]"), "")
    }
    return if (clean.isBlank())
        0.0
    else
        formatter.parse(clean)?.toDouble() ?: 0.0
}