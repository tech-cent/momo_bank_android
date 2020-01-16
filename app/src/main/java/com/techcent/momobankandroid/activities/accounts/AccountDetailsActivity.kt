package com.techcent.momobankandroid.activities.accounts

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.models.Account
import kotlinx.android.synthetic.main.activity_account_details.*


class AccountDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        val account = intent.getParcelableExtra<Account>("account")

        tv_bank.text = "Account: ${account.bank}"
        tv_balance.text = "Balance: %.2f".format(account.balance)
        tv_type.text = "Type: ${account.type}"

        val dateString = account.dateCreated
        val date = dateString?.substring(0..9)
        val time = dateString?.substring(11..18)
        tv_date_created.text = "Date: $date $time"
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
}
