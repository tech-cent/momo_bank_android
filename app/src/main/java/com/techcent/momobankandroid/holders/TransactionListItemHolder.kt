package com.techcent.momobankandroid.holders

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.activities.transactions.TransactionDetailsActivity
import com.techcent.momobankandroid.models.Transaction


class TransactionListItemHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val typeTextView = itemView.findViewById<TextView>(R.id.tv_type)
    private val dateTextView = itemView.findViewById<TextView>(R.id.tv_date_created)
    private val amountTextView = itemView.findViewById<TextView>(R.id.tv_amount)

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

        val dateString = transaction.dateCreated
        val date = dateString?.substring(0..9)
        dateTextView.text = "$date"

        amountTextView.text = "%.2f".format(transaction.amount)
    }

}
