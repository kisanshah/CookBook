package com.kisan.cookbook.utils

import android.app.Activity
import android.widget.EditText
import android.widget.Toast


fun EditText.validate(message: String, validator: (String) -> Boolean) {
    this.error = if (validator(this.text.toString())) null else message
}

fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}