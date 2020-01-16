package com.techcent.momobankandroid.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techcent.momobankandroid.MoMoApplication
import com.techcent.momobankandroid.R

class SettingsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = MoMoApplication.applicationContext().resources.getString(R.string.settings_header)
    }
    val text: LiveData<String> = _text
}
