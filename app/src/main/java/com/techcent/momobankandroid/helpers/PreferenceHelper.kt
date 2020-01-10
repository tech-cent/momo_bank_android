package com.techcent.momobankandroid.helpers

import android.content.Context
import android.content.SharedPreferences


class PreferenceHelper(context: Context) {
    private val INTRO = "intro"
    private val NAME = "name"
    private val ACCOUNTS = "accounts"
    private val TRANSACTIONS = "transactions"
    private val ACCESS_TOKEN = "access_token"
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

    fun putAccounts(accounts: String?) {
        val edit = appPrefs.edit()
        edit.putString(ACCOUNTS, accounts)
        edit.apply()
    }

    val accounts: String?
        get() = appPrefs.getString(ACCOUNTS, "[]")

    fun putTransactions(transactions: String?) {
        val edit = appPrefs.edit()
        edit.putString(TRANSACTIONS, transactions)
        edit.apply()
    }

    val transactions: String?
        get() = appPrefs.getString(TRANSACTIONS, "[]")

    fun putAccessToken(token: String?) {
        val edit = appPrefs.edit()
        edit.putString(ACCESS_TOKEN, token)
        edit.apply()
    }

    val token: String?
        get() = appPrefs.getString(ACCESS_TOKEN, "")
}
