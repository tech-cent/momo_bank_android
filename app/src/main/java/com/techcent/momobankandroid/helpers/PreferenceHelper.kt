package com.techcent.momobankandroid.helpers

import android.content.Context
import android.content.SharedPreferences


class PreferenceHelper(private val context: Context) {
    private val INTRO = "intro"
    private val NAME = "name"
    private val appPrefs: SharedPreferences = context.getSharedPreferences(
        "shared",
        Context.MODE_PRIVATE
    )

    fun putIsLoggedIn(loggedIn: Boolean) {
        val edit = appPrefs.edit()
        edit.putBoolean(INTRO, loggedIn)
        edit.apply()
    }

    val isLoggedIn: Boolean
        get() = appPrefs.getBoolean(INTRO, false)

    fun putName(loggedIn: String?) {
        val edit = appPrefs.edit()
        edit.putString(NAME, loggedIn)
        edit.apply()
    }

    val name: String?
        get() = appPrefs.getString(NAME, "")
}
