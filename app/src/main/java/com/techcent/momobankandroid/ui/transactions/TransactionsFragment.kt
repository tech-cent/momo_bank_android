package com.techcent.momobankandroid.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.techcent.momobankandroid.MoMoApplication
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.adapters.TransactionsListAdapter
import com.techcent.momobankandroid.helpers.PreferenceHelper
import com.techcent.momobankandroid.models.Transaction
import java.util.*

class TransactionsFragment : Fragment() {

    private lateinit var transactionsViewModel: TransactionsViewModel
    private var preferenceHelper: PreferenceHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        transactionsViewModel = ViewModelProviders.of(this).get(TransactionsViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_transactions, container, false)

        val textView: TextView = root.findViewById(R.id.tv_transactions_header)
        transactionsViewModel.text.observe(this, Observer {
            textView.text = it
        })

        val recyclerView = root.findViewById<View>(R.id.rv_transactions_list) as RecyclerView
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        preferenceHelper = PreferenceHelper(MoMoApplication.applicationContext())

        val transactionsString = preferenceHelper!!.transactions

        val gson = Gson()
        val transactionsArrayType = object : TypeToken<ArrayList<Transaction>>() {}.type
        val transactions: ArrayList<Transaction> =
            gson.fromJson(transactionsString, transactionsArrayType)

        val adapter = TransactionsListAdapter(transactions)
        recyclerView.adapter = adapter

        if (transactions.isEmpty()) {
            textView.text = "No Transactions!"
        } else {
            textView.text = "All ${adapter.itemCount} Transactions"
        }

        return root
    }
}