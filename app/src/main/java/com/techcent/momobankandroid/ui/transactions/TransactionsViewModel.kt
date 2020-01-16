package com.techcent.momobankandroid.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techcent.momobankandroid.MoMoApplication
import com.techcent.momobankandroid.R

class TransactionsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value =
            MoMoApplication.applicationContext().resources.getString(R.string.transactions_header)
    }
    val text: LiveData<String> = _text
}
