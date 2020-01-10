package com.techcent.momobankandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.holders.TransactionListItemHolder
import com.techcent.momobankandroid.models.Transaction

class TransactionsListAdapter(private val transactions: ArrayList<Transaction>) :
    RecyclerView.Adapter<TransactionListItemHolder>() {

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionListItemHolder, position: Int) {
        val transaction = transactions[position]
        holder.updateWithPage(transaction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionListItemHolder {
        // create view holder here
        val cardItem =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.transaction_list_item, parent, false)
        return TransactionListItemHolder(cardItem)
    }
}