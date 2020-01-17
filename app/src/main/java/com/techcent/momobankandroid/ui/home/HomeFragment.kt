package com.techcent.momobankandroid.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.techcent.momobankandroid.MoMoApplication.Companion.applicationContext
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.activities.transactions.TransactActivity
import com.techcent.momobankandroid.adapters.AccountsListAdapter
import com.techcent.momobankandroid.helpers.PreferenceHelper
import com.techcent.momobankandroid.models.Account


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var preferenceHelper: PreferenceHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val textView: TextView = root.findViewById(R.id.tv_accounts_balance)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })

        val recyclerView = root.findViewById<View>(R.id.rv_accounts_list) as RecyclerView
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        preferenceHelper = PreferenceHelper(applicationContext())
        val accountsString = preferenceHelper!!.accounts

        val gson = Gson()
        val accountsArrayType = object : TypeToken<ArrayList<Account>>() {}.type
        val accounts: ArrayList<Account> = gson.fromJson(accountsString, accountsArrayType)

        val adapter = AccountsListAdapter(accounts)
        recyclerView.adapter = adapter

        textView.text = "A/C Balance: ${adapter.getTotalBalance()}"

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionsView = view.findViewById<LinearLayout>(R.id.actions_view)
        val depositButton = actionsView.findViewById<Button>(R.id.btn_deposit)
        val withdrawButton = actionsView.findViewById<Button>(R.id.btn_withdraw)

        depositButton.setOnClickListener {
            val intent = Intent(it.context, TransactActivity::class.java)
            intent.putExtra("transaction", "deposit")
            it.context.startActivity(intent)
        }

        withdrawButton.setOnClickListener {
            val intent = Intent(it.context, TransactActivity::class.java)
            intent.putExtra("transaction", "withdraw")
            it.context.startActivity(intent)
        }
    }

}
