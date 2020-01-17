package com.techcent.momobankandroid.activities.transactions

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.models.Transaction
import kotlinx.android.synthetic.main.activity_transaction_details.*

class TransactionDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        val transaction = intent.getParcelableExtra<Transaction>("transaction")

        tv_account.text = "Account: ${transaction.account.toString()}"
        tv_type.text = "Type: ${transaction.type?.capitalize()}"
        tv_status.text = "Status: ${transaction.status?.capitalize()}"
        tv_amount.text = "Amount: %.2f".format(transaction.amount)
        tv_prev_balance.text = "Prev. Bal.: %.2f".format(transaction.prevBalance)
        tv_new_balance.text = "Curr. Bal.: %.2f".format(transaction.newBalance)

        val dateString = transaction.dateCreated
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