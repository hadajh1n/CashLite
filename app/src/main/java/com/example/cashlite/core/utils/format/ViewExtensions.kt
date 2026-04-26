package com.example.cashlite.core.utils.format

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.setupAmountLogic() {

    this.isCursorVisible = false

    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val str = s.toString()

            removeTextChangedListener(this)

            if (str.isEmpty()) {
                setText("0")
                setSelection(1)
            } else if (str.length > 1 && str.startsWith("0") && str[1] != '.' && str[1] != ',') {
                val newText = str.substring(1)
                setText(newText)
                setSelection(newText.length)
            } else if (str == "00") {
                setText("0")
                setSelection(1)
            }

            addTextChangedListener(this)
        }
    })
}