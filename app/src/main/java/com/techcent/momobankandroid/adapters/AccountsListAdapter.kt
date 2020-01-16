package com.techcent.momobankandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.holders.AccountListItemHolder
import com.techcent.momobankandroid.models.Account

class AccountsListAdapter(private val accounts: ArrayList<Account>) :
    RecyclerView.Adapter<AccountListItemHolder>() {

    override fun getItemCount(): Int {
        return accounts.size
    }

    override fun onBindViewHolder(holder: AccountListItemHolder, position: Int) {
        val account = accounts[position]
        holder.updateWithPage(account)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountListItemHolder {
        // create view holder here
        val cardItem =
            LayoutInflater.from(parent.context).inflate(R.layout.account_list_item, parent, false)
        return AccountListItemHolder(cardItem)
    }

    fun getTotalBalance(): Double {
        var totalBalance = 0.00
        accounts.forEach { totalBalance += it.balance }
        return totalBalance
    }
}
