package com.techcent.momobankandroid.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.techcent.momobankandroid.MoMoApplication.Companion.applicationContext
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.activities.auth.LoginActivity
import com.techcent.momobankandroid.helpers.PreferenceHelper

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var preferenceHelper: PreferenceHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val textView: TextView = root.findViewById(R.id.text_settings)
        settingsViewModel.text.observe(this, Observer {
            textView.text = it
        })

        val phoneNumber = root.findViewById<TextView>(R.id.text_settings)
        val dob = root.findViewById<TextView>(R.id.tv_dob)
        val nin = root.findViewById<TextView>(R.id.tv_nin)

        preferenceHelper = PreferenceHelper(applicationContext())

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLogout = view.findViewById<Button>(R.id.btn_logout)

        btnLogout.setOnClickListener {
            preferenceHelper!!.clearPrefs()

            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}