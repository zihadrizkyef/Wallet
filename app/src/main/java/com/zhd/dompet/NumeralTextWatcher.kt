package com.zhd.dompet

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.zhd.dompet.toCurrency

class NumeralTextWatcher(editText: EditText, currency: String = "Rp", isDecimal: Boolean = true, decimalDigit: Int = 5, valueChangeListener: ((String) -> Unit)? = null) : TextWatcher {
    private val editText: EditText
    private val RUPIAH_CURRENCY = "RP"
    private var isDecimal = true
    private var decimalDigit = 0
    private var currency = ""
    private var valueChangeListener: ((value: String) -> Unit)? = null

    init {
        this.editText = editText
        this.isDecimal = isDecimal
        this.decimalDigit = decimalDigit
        this.currency = currency
        this.valueChangeListener = valueChangeListener
    }
    fun setCurrency(currency: String){
        this.currency = currency
    }
    override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun afterTextChanged(s: Editable) {
        if (editText.text!!.isNotEmpty()){
            val initialLength = editText.text.length
            val lastPosition = editText.selectionStart

            var inputText = if (currency.uppercase().startsWith(RUPIAH_CURRENCY)){
                editText.text.toString().replace(".", "")
            } else {
                editText.text.toString().replace(",", "")
            }
            inputText = inputText.replace(",", ".")

            if (inputText.count { it == '.' } > 1){
                inputText = inputText.replaceFirst(".", "")
            }

            if (!isDecimal)
                inputText = inputText.replace(".", "")

            val formattedText = if (inputText.contains(".")){
                val parts = inputText.split(".")
                val separator = if (currency.uppercase().startsWith(RUPIAH_CURRENCY)) "," else "."
                if (parts[0].isNotEmpty()){
                    if (parts[1].isNotEmpty()){
                        "${(parts[0].toBigDecimal()).toCurrency(false, currency)}$separator${parts[1].substring(0, minOf(parts[1].length, decimalDigit))}"
                    } else {
                        "${(parts[0].toBigDecimal()).toCurrency(false, currency)}$separator"
                    }
                } else {
                    "0"
                }
            } else {
                inputText.toBigDecimal().toCurrency(false, currency)
            }

            editText.removeTextChangedListener(this)
            editText.setText(formattedText)
            valueChangeListener?.let { it(formattedText) }

            val newPosition = lastPosition + (formattedText.length - initialLength)

            editText.setSelection(if (newPosition < formattedText.length && newPosition > 0) newPosition else formattedText.length)
            editText.addTextChangedListener(this)

        }
    }
}