package com.techcent.momobankandroid.holders

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.activities.transactions.TransactionDetailsActivity
import com.techcent.momobankandroid.helpers.formatDate
import com.techcent.momobankandroid.models.Transaction


class TransactionListItemHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val typeTextView = itemView.findViewById<TextView>(R.id.tv_transaction_type)
    private val dateTextView = itemView.findViewById<TextView>(R.id.tv_transaction_date)
    private val amountTextView = itemView.findViewById<TextView>(R.id.tv_transaction_amount)

    private var currentTransaction: Transaction? = null

    init {
        itemView.setOnClickListener {
            val intent = Intent(it.context, TransactionDetailsActivity::class.java)
            intent.putExtra("transaction", currentTransaction)
            it.context.startActivity(intent)
        }
    }

    fun updateWithPage(transaction: Transaction) {
        currentTransaction = transaction

        typeTextView.text = transaction.type?.capitalize()
        dateTextView.text = transaction.dateCreated?.let { formatDate(it, "DD-MM-YYYY") }
        amountTextView.text = "%.2f".format(transaction.amount)
    }

}
