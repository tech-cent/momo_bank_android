package com.techcent.momobankandroid.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techcent.momobankandroid.MoMoApplication.Companion.applicationContext
import com.techcent.momobankandroid.R

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = applicationContext().resources.getString(R.string.accounts_balance)
    }
    val text: LiveData<String> = _text
}
