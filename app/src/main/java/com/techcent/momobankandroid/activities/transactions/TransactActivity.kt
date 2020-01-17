package com.techcent.momobankandroid.activities.transactions

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.techcent.momobankandroid.MoMoApplication
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.activities.MainActivity
import com.techcent.momobankandroid.api.ApiInterface
import com.techcent.momobankandroid.constants.BASE_URL
import com.techcent.momobankandroid.helpers.PreferenceHelper
import com.techcent.momobankandroid.helpers.setupToHideKeyboard
import com.techcent.momobankandroid.models.Account
import kotlinx.android.synthetic.main.activity_transact.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


class TransactActivity : AppCompatActivity() {

    private var preferenceHelper: PreferenceHelper? = null

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transact)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val thisView: ConstraintLayout = findViewById(R.id.transact)
        setupToHideKeyboard(thisView, this@TransactActivity)

        val intent = intent
        val transaction = intent.getStringExtra("transaction")

        preferenceHelper = PreferenceHelper(MoMoApplication.applicationContext())
        val accessToken = preferenceHelper!!.token

        val accountsString = preferenceHelper!!.accounts
        val gson = Gson()
        val accountsArrayType = object : TypeToken<ArrayList<Account>>() {}.type
        val accounts: ArrayList<Account> = gson.fromJson(accountsString, accountsArrayType)
        val accountIds: MutableList<Int> = ArrayList()
        accounts.forEach {
            it.id?.let { it1 -> accountIds.add(it1) }
        }

        val accountsSpinner = findViewById<Spinner>(R.id.accounts_spinner)
        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, accountIds
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        accountsSpinner.adapter = dataAdapter

        tv_type.text = transaction.capitalize()

        btn_transact.setOnClickListener {
            val account = accountsSpinner.selectedItem.toString().toInt()
            val amount = et_amount.text.toString().toInt()

            if (accessToken != null) {
                transact(accessToken, account, transaction, amount)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun transact(
        accessToken: String,
        account: Int,
        transaction: String,
        amount: Int
    ) {
        // make the http call to the web server using retrofit
        val call: Call<String?>? = api.transact(accessToken, account, transaction, amount)
        call?.enqueue(object : Callback<String?> {
            // if the server gives the JSON response, the compiler calls the onResponse() method
            override fun onResponse(
                call: Call<String?>?,
                response: Response<String?>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@TransactActivity,
                        "Transaction Successful!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@TransactActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@TransactActivity, "Try Again!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String?>?, t: Throwable?) {}
        })
    }
}