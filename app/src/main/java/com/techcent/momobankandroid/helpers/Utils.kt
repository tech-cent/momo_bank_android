package com.techcent.momobankandroid.helpers

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


fun hideSoftKeyboard(activity: Activity) {
    val inputMethodManager = activity.getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(
        activity.currentFocus?.windowToken, 0
    )
}

fun setupToHideKeyboard(view: View, activity: Activity) {
    // Set up touch listener for non-text box views to hide keyboard.
    if (view !is EditText) {
        view.setOnTouchListener { _, _ ->
            hideSoftKeyboard(activity)
            false
        }
    }

    //If a layout container, iterate over children and seed recursion.
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val innerView: View = view.getChildAt(i)
            setupToHideKeyboard(innerView, activity)
        }
    }
}

fun isValidPassword(password: String): Boolean {
//        val passwordPattern = "\"^(?=.*[A-Z])(?=.*[@_.]).*\$\""
//        val pattern = Pattern.compile(passwordPattern)
//        val matcher = pattern.matcher(password)
//        return matcher.matches() && password.length >= 8
    return password.length >= 8
}

fun isValidPhone(phoneNumber: String): Boolean {
    return when {
        (phoneNumber.startsWith("077") || phoneNumber.startsWith("078")) && phoneNumber.length == 10 -> true
        (phoneNumber.startsWith("25678") || phoneNumber.startsWith("25677")) && phoneNumber.length == 12 -> true
        else -> false
    }
}
