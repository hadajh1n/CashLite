package com.example.cashlite.core.utils.filters

import android.text.InputFilter
import android.text.Spanned

class DecimalDigitsInputFilter(
    private val digitsBeforeZero: Int,
    private val digitsAfterZero: Int,
) : InputFilter {

    private val regex = Regex("[0-9]{0,$digitsBeforeZero}+((\\.|,)[0-9]{0,$digitsAfterZero})?")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        val newText = dest.substring(0, dstart) +
                source.substring(start, end) +
                dest.substring(dend)

        return if (regex.matches(newText)) null else ""
    }
}