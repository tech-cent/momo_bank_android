package com.techcent.momobankandroid.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.techcent.momobankandroid.MoMoApplication.Companion.applicationContext
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.activities.auth.LoginActivity
import com.techcent.momobankandroid.helpers.PreferenceHelper
import com.techcent.momobankandroid.models.User

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
        val phoneNumber = root.findViewById<TextView>(R.id.tv_phone_number)
        val dob = root.findViewById<TextView>(R.id.tv_dob)
        val nin = root.findViewById<TextView>(R.id.tv_nin)

        preferenceHelper = PreferenceHelper(applicationContext())
        val profileString = preferenceHelper!!.profile

        val gson = Gson()
        val profileType = object : TypeToken<User>() {}.type
        val profile: User = gson.fromJson(profileString, profileType)

        textView.text = "Name: ${profile.firstName} ${profile.lastName}"
        phoneNumber.text = "Phone: ${profile.phoneNumber}"
        dob.text = "Born: ${profile.dob}"
        nin.text = "NIN: ${profile.nin}"

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