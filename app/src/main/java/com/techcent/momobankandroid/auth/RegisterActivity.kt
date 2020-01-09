package com.techcent.momobankandroid.auth

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.activities.WelcomeActivity
import com.techcent.momobankandroid.constants.BASE_URL
import com.techcent.momobankandroid.helpers.PreferenceHelper
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.SimpleDateFormat
import java.util.*


val calendar = Calendar.getInstance()!!

class RegisterActivity : AppCompatActivity() {
    private var preferenceHelper: PreferenceHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        preferenceHelper = PreferenceHelper(this)
        // if logged in, redirect to WelcomeActivity
        if (preferenceHelper!!.isLoggedIn) {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        tv_login!!.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val date =
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateOfBirth()
            }

        et_dob.setOnClickListener {
            DatePickerDialog(
                this, date, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btn_register.setOnClickListener { registerUser() }
    }

    private fun updateDateOfBirth() {
        val simpleDateFormat = SimpleDateFormat("YYYY-MM-DD", Locale.US)
        et_dob.setText(simpleDateFormat.format(calendar.time))
    }

    private fun registerUser() {
        val firstName = et_first_name!!.text.toString()
        val lastName = et_last_name!!.text.toString()
        val phoneNumber = et_phone_number!!.text.toString()
        val nin = et_nin!!.text.toString()
        val password = et_password!!.text.toString()
        val confirmPassword = et_confirm_password!!.text.toString()

        val dateOfBirth = et_dob!!.text.toString()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val api =
            retrofit.create(RegisterInterface::class.java)

        if (confirmPassword == password) {
            val call: Call<String?>? = api.signUp(
                firstName, lastName, phoneNumber, password, nin, dateOfBirth
            )
            call?.enqueue(object : Callback<String?> {
                override fun onResponse(
                    call: Call<String?>?,
                    response: Response<String?>
                ) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration Successful!",
                        Toast.LENGTH_SHORT
                    ).show()

                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val jsonResponse: String = response.body().toString()

                            // compiler then executes the parseLoginData() method
                            parseSignUpData(jsonResponse)
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "No data returned!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<String?>?, t: Throwable?) {}
            })
        } else {
            Toast.makeText(this, "Passwords must match!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun parseSignUpData(response: String) {
        try {
            // save some information in shared preference using PreferenceHelper class
            saveInfo(response)

            val intent = Intent(this, LoginActivity::class.java)
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
            val name = jsonObject.getString("first_name")

            preferenceHelper!!.putName(name)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
