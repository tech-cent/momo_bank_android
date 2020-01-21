package com.techcent.momobankandroid.activities.auth

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
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

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var phoneNumber: String
    private lateinit var nin: String
    private lateinit var password: String
    private lateinit var confirmPassword: String
    private lateinit var dateOfBirth: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val thisView: ScrollView = findViewById(R.id.signup)
        setupToHideKeyboard(thisView, this)

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

        et_first_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                lbl_first_name.error =
                    if (et_first_name!!.text.toString().isEmpty()) "First name is empty!" else null
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

        })

        et_last_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                lbl_last_name.error =
                    if (et_last_name!!.text.toString().isEmpty()) "Last name is empty!" else null
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

        })

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

        // Begin Date of birth
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

        et_dob.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                lbl_dob.error =
                    if (et_dob!!.text.toString().isEmpty()) "Date of birth is empty!" else null
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

        })
        // End Date of birth

        et_nin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                lbl_nin.error = if (et_nin!!.text.toString().isEmpty()) "NIN is empty!" else null
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
                    if (!isValidPassword(et_password!!.text.toString())) {
//                        "Passwords must have at least 8 characters (with 1 Letter, 1 Number and 1 Special Character)!"
                        "Passwords must have at least 8 characters"
                    } else null
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

        })

        et_confirm_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                lbl_confirm_password.error =
                    if (et_password!!.text.toString() != et_confirm_password!!.text.toString()) {
                        "Passwords must match!"
                    } else null
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

        })

        btn_register.setOnClickListener { validateInputs() }
    }

    private fun validateInputs() {

        firstName = et_first_name!!.text.toString()
        lastName = et_last_name!!.text.toString()
        phoneNumber = et_phone_number!!.text.toString()
        dateOfBirth = et_dob!!.text.toString()
        nin = et_nin!!.text.toString()
        password = et_password!!.text.toString()
        confirmPassword = et_confirm_password!!.text.toString()

        lbl_first_name.error = if (firstName.isEmpty()) "First name is empty!" else null
        lbl_last_name.error = if (lastName.isEmpty()) "Last name is empty!" else null

        // phone number validation
        if (phoneNumber.isEmpty()) {
            lbl_phone_number.error = "Phone number is empty!"
        } else if (phoneNumber.startsWith("0")) {
            phoneNumber.replaceFirst("0", "256")
        } else if (!isValidPhone(phoneNumber)) {
            lbl_phone_number.error = "Please use a valid MTN number!"
        } else if (phoneNumber.isNotEmpty()) {
            lbl_phone_number.error = null
        }

        lbl_dob.error = if (dateOfBirth.isEmpty()) "Date of birth is empty!" else null

        lbl_nin.error = if (nin.isEmpty()) "NIN is empty!" else null

        // password validation
        when {
            password.isEmpty() -> lbl_password.error = "Password is empty!"
            confirmPassword.isEmpty() -> lbl_confirm_password.error = "Password is empty!"
            confirmPassword != password -> {
                lbl_password.error = "Passwords must match!"
                lbl_confirm_password.error = "Passwords must match!"
            }
            password.isNotEmpty() -> {
                lbl_password.error = null
            }
        }

        if (lbl_first_name.error == null && lbl_last_name.error == null && lbl_phone_number.error == null && lbl_dob.error == null
            && lbl_nin.error == null && lbl_password.error == null && lbl_confirm_password.error == null
        ) {
            ProgressDialog.show(this, "Status", "Verifying details!", true, true)
            registerUser()
        }
    }

    private fun updateDateOfBirth() {
        val simpleDateFormat = SimpleDateFormat("YYYY-MM-DD", Locale.US)
        et_dob.setText(simpleDateFormat.format(calendar.time))
    }

    private fun registerUser() {

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val api =
            retrofit.create(ApiInterface::class.java)

        val call: Call<String?>? = api.signUp(
            firstName, lastName, phoneNumber, password, nin, dateOfBirth
        )
        call?.enqueue(object : Callback<String?> {
            override fun onResponse(
                call: Call<String?>?,
                response: Response<String?>
            ) {
                if (response.isSuccessful) {

                    Toast.makeText(
                        applicationContext,
                        "Registration Successful!",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    if (response.body() != null) {
                        val jsonResponse: String = response.body().toString()

                        // compiler then executes the parseLoginData() method
                        parseSignUpData(jsonResponse)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "No data returned!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    // TODO('Properly capture "Number or NIN already exists"')
                    Toast.makeText(applicationContext, "Try again!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<String?>?, t: Throwable?) {}
        })
    }

    private fun parseSignUpData(response: String) {
        try {
            // save some information in shared preference using PreferenceHelper class
            saveSignUpInfo(response)

            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun saveSignUpInfo(response: String) {
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
