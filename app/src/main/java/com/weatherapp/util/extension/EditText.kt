package com.weatherapp.util.extension

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.weatherapp.util.helper.SingleBlock
fun EditText.onEditorActionListener(onTextChanged: SingleBlock<String>) =
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onTextChanged(text.toString())
            val imm: InputMethodManager =
                this.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
            clearFocus()
            return@setOnEditorActionListener true
        } else {
            return@setOnEditorActionListener false
        }
    }