package ru.zabster.safepassapp.utils.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/**
 * Simple call toast
 *
 * @param message text for toast message
 * @param duration length toast showing. Default Toast.LENGTH_SHORT
 */
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * Simple call toast
 *
 * @param messageRes text resource for toast message
 * @param duration length toast showing. Default Toast.LENGTH_SHORT
 */
fun Context.toast(messageRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, messageRes, duration).show()
}

/**
 * Simple hide keyboard
 */
fun Activity.hideKeyBoard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(window.decorView.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

/**
 * Simple show keyboard and request focus for view
 */
fun View.showKeyBoard() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    postDelayed({
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }, 200)
}