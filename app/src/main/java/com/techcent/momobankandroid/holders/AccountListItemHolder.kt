package com.techcent.momobankandroid.holders

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.activities.accounts.AccountDetailsActivity
import com.techcent.momobankandroid.models.Account

class AccountListItemHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val idTextView = itemView.findViewById<TextView>(R.id.tv_account_id)
    private val balanceTextView = itemView.findViewById<TextView>(R.id.tv_account_detail_balance)

    private var currentAccount: Account? = null

    init {
        itemView.setOnClickListener {
            val intent = Intent(it.context, AccountDetailsActivity::class.java)
            intent.putExtra("account", currentAccount)
            it.context.startActivity(intent)
        }
    }

    fun updateWithPage(account: Account) {
        currentAccount = account

        idTextView.text = account.id.toString()
        balanceTextView.text = "%.2f".format(account.balance)
    }

}
