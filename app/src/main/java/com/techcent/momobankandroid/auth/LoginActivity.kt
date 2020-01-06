package com.techcent.momobankandroid.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.WelcomeActivity
import com.techcent.momobankandroid.constants.BASE_URL
import com.techcent.momobankandroid.helpers.PreferenceHelper
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


class LoginActivity : AppCompatActivity() {
    private var preferenceHelper: PreferenceHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        preferenceHelper = PreferenceHelper(this)

        tv_register!!.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_login.setOnClickListener { loginUser() }
    }

    private fun loginUser() {
        val phoneNumber = et_phone_number!!.text.toString().trim { it <= ' ' }
        val password = et_password!!.text.toString().trim { it <= ' ' }

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val api = retrofit.create(LoginInterface::class.java)

        // make the http call to the web server using retrofit
        val call: Call<String?>? = api.login(phoneNumber, password)
        call?.enqueue(object : Callback<String?> {
            // if the server gives the JSON response, the compiler calls the onResponse() method
            override fun onResponse(
                call: Call<String?>?,
                response: Response<String?>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT)
                        .show()
                    if (response.body() != null) {
                        val jsonResponse: String = response.body().toString()

                        // compiler then executes the parseLoginData() method
                        parseLoginData(jsonResponse)
                    } else {
                        Toast.makeText(this@LoginActivity, "No data returned!", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<String?>?, t: Throwable?) {}
        })
    }

    private fun parseLoginData(response: String) {
        try {
            // save some information in shared preference using PreferenceHelper class
            saveInfo(response)

            val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun saveInfo(response: String) {
        preferenceHelper!!.putIsLoggedIn(true)
        try {
            val jsonObject = JSONObject(response)
            val name = jsonObject.getString("name")

            preferenceHelper!!.putName(name)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
