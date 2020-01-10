package com.techcent.momobankandroid.helpers

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*


fun hideSoftKeyboard(activity: Activity) {
    val inputMethodManager = activity.getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(
        activity.currentFocus.windowToken, 0
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

fun formatDate(dateString: String, format: String): String {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-DD", Locale.US)
    val simpleDateFormat2 = SimpleDateFormat(format, Locale.US)
    val date = simpleDateFormat.parse(dateString)
    return simpleDateFormat2.format(date)
}
