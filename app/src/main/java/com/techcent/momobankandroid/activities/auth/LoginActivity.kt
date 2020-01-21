package com.techcent.momobankandroid.activities.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.activities.WelcomeActivity
import com.techcent.momobankandroid.api.ApiInterface
import com.techcent.momobankandroid.constants.BASE_URL
import com.techcent.momobankandroid.helpers.PreferenceHelper
import com.techcent.momobankandroid.helpers.isValidPassword
import com.techcent.momobankandroid.helpers.isValidPhone
import com.techcent.momobankandroid.helpers.setupToHideKeyboard
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

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val thisView: ScrollView = findViewById(R.id.login)
        setupToHideKeyboard(thisView, this)

        preferenceHelper = PreferenceHelper(this)

        tv_register!!.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        et_phone_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val phone = et_phone_number!!.text.toString()
                if (phone.isEmpty()) {
                    lbl_phone_number.error = "Phone number is empty!"
                } else if (!isValidPhone(phone)) {
                    lbl_phone_number.error = "Please use a valid MTN number!"
                } else if (phone.isNotEmpty()) {
                    lbl_phone_number.error = null
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

        })

        et_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                lbl_password.error =
                    if (!isValidPassword(et_password!!.text.toString())) "Passwords must have at least 8 characters" else null
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

        })

        btn_login.setOnClickListener {
            ProgressDialog.show(this, "Status", "Verifying details!", true, true)
            loginUser()
        }
    }

    private fun loginUser() {
        val phoneNumber = et_phone_number!!.text.toString().trim { it <= ' ' }
        val password = et_password!!.text.toString().trim { it <= ' ' }

        if (phoneNumber.startsWith("0")) {
            phoneNumber.replaceFirst("0", "256")
        }

        // make the http call to the web server using retrofit
        val call: Call<String?>? = api.login(phoneNumber, password)
        call?.enqueue(object : Callback<String?> {
            // if the server gives the JSON response, the compiler calls the onResponse() method
            override fun onResponse(
                call: Call<String?>?,
                response: Response<String?>
            ) {
                if (response.isSuccessful) {
                    lbl_phone_number.error = null
                    lbl_password.error = null
                    if (response.body() != null) {
                        val jsonResponse: String = response.body().toString()

                        // compiler then executes the parseLoginData() method
                        parseLoginData(jsonResponse)
                    } else {
                        Toast.makeText(applicationContext, "No data returned!", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    lbl_phone_number.error = "Confirm MTN phone number!"
                    lbl_password.error = "Confirm password!"
                    Toast.makeText(applicationContext, "Invalid credentials!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<String?>?, t: Throwable?) {}
        })
    }

    private fun parseLoginData(response: String) {
        try {
            // save some information in shared preference using PreferenceHelper class
            saveInfo(response)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun saveInfo(response: String) {
        val jsonObject = JSONObject(response)
        try {
            val token = jsonObject.getString("access")

            if (token != null) {
                try {
                    preferenceHelper!!.putIsLoggedIn(true)
                    val name = jsonObject.getString("name")
                    val accessToken = "Bearer ${jsonObject.getString("access")}"

                    preferenceHelper!!.putName(name)

                    getProfile(accessToken)
                    getAccounts(accessToken)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()

            Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getAccounts(accessToken: String) {
        // make the http call to the web server using retrofit
        val call: Call<String?>? = api.getAccounts(accessToken)
        call?.enqueue(object : Callback<String?> {
            // if the server gives the JSON response, the compiler calls the onResponse() method
            override fun onResponse(
                call: Call<String?>?,
                response: Response<String?>
            ) {
                Toast.makeText(applicationContext, "Getting Accounts", Toast.LENGTH_SHORT).show()
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val jsonResponse = response.body().toString()

                        // compiler then executes the saveAccountsInfo() method
                        preferenceHelper!!.putAccessToken(accessToken)
                        saveAccountsInfo(jsonResponse)

                        val intent = Intent(applicationContext, WelcomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "No Accounts found!", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<String?>?, t: Throwable?) {}
        })
    }

    private fun saveAccountsInfo(response: String): Boolean {
        try {
            preferenceHelper!!.putAccounts(response)

            return true
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return false
    }

    private fun getProfile(accessToken: String) {
        // make the http call to the web server using retrofit
        val call: Call<String?>? = api.getProfile(accessToken)
        call?.enqueue(object : Callback<String?> {
            // if the server gives the JSON response, the compiler calls the onResponse() method
            override fun onResponse(
                call: Call<String?>?,
                response: Response<String?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val jsonResponse = response.body().toString()

                        // compiler then executes the saveAccountsInfo() method
                        preferenceHelper!!.putAccessToken(accessToken)
                        saveProfileInfo(jsonResponse)
                    }
                }
            }

            override fun onFailure(call: Call<String?>?, t: Throwable?) {}
        })
    }

    private fun saveProfileInfo(response: String): Boolean {
        try {
            preferenceHelper!!.putProfile(response)

            return true
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return false
    }

}
