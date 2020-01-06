package com.techcent.momobankandroid

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.techcent.momobankandroid.auth.LoginActivity
import com.techcent.momobankandroid.helpers.PreferenceHelper
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {
    private var preferenceHelper: PreferenceHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        preferenceHelper = PreferenceHelper(this)
        tv_welcome!!.text = "Welcome ${preferenceHelper!!.name}"

        tv_skip.setOnClickListener { goToMain() }

        btn_logout.setOnClickListener {
            preferenceHelper!!.putIsLoggedIn(false)

            val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        // 5 second counter
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tv_skip.text = "${millisUntilFinished / 1000} Skip>>"
            }

            override fun onFinish() {
                goToMain()
            }
        }.start()

    }

    private fun goToMain() {
        val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

}
