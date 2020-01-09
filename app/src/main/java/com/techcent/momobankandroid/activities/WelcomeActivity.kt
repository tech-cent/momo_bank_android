package com.techcent.momobankandroid.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.helpers.PreferenceHelper
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {
    private var preferenceHelper: PreferenceHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        preferenceHelper = PreferenceHelper(this)

        skipTimer.start()

        tv_welcome!!.text = "Welcome ${preferenceHelper!!.name}"

        tv_skip.setOnClickListener {
            skipTimer.cancel()
            goToActivity(MainActivity::class.java)
        }

    }

    private fun goToActivity(destination: Class<*>) {
        val intent = Intent(this@WelcomeActivity, destination)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    // 5 second counter
    private var skipTimer: CountDownTimer = object : CountDownTimer(5000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            tv_skip.text = "${millisUntilFinished / 1000} Skip>>"
        }

        override fun onFinish() {
            goToActivity(MainActivity::class.java)
        }
    }

}
