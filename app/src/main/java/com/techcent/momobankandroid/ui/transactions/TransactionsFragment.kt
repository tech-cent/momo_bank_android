package com.techcent.momobankandroid.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.techcent.momobankandroid.MoMoApplication.Companion.applicationContext
import com.techcent.momobankandroid.R
import com.techcent.momobankandroid.adapters.TransactionsListAdapter
import com.techcent.momobankandroid.api.ApiInterface
import com.techcent.momobankandroid.constants.BASE_URL
import com.techcent.momobankandroid.helpers.PreferenceHelper
import com.techcent.momobankandroid.models.Account
import com.techcent.momobankandroid.models.Transaction
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

class TransactionsFragment : Fragment() {

    private lateinit var transactionsViewModel: TransactionsViewModel
    private var preferenceHelper: PreferenceHelper? = null

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiInterface::class.java)
    private lateinit var transactionsListAdapter: TransactionsListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var accessToken: String
    private lateinit var accountsString: String

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

        preferenceHelper = PreferenceHelper(applicationContext())

        accessToken = preferenceHelper!!.token!!
        accountsString = preferenceHelper!!.accounts!!

        fetchTransactions(accessToken, accountsString)
        val transactionsString = preferenceHelper!!.transactions

        val gson = Gson()
        val transactionsArrayType = object : TypeToken<ArrayList<Transaction>>() {}.type
        val transactions: ArrayList<Transaction> =
            gson.fromJson(transactionsString, transactionsArrayType)

        transactionsListAdapter = TransactionsListAdapter(transactions)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView = root.findViewById(R.id.rv_transactions_list)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = transactionsListAdapter

        if (transactions.isEmpty()) {
            textView.text = "No Transactions!"
        } else {
            textView.text = "All ${transactionsListAdapter.itemCount} Transactions"
        }

        swipeRefreshLayout = root.findViewById(R.id.swipe_transactions_list)

        // Listen for a User's swipe-to-refresh action on the Transactions list
        swipeRefreshLayout.setOnRefreshListener { reloadTransactionsList() }

        return root
    }

    private fun reloadTransactionsList() {
        fetchTransactions(accessToken, accountsString)
        transactionsListAdapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = false // Disable the refresh icon
    }

    private fun fetchTransactions(accessToken: String, accountsString: String) {
        Toast.makeText(applicationContext(), "Getting Transactions", Toast.LENGTH_SHORT).show()
        val gson = Gson()
        val accountsArrayType = object : TypeToken<ArrayList<Account>>() {}.type
        val accounts: ArrayList<Account> = gson.fromJson(accountsString, accountsArrayType)

        accounts.forEach {
            getUserAccountTransactions(accessToken, it.id!!)
        }
    }

    private fun getUserAccountTransactions(accessToken: String, accountId: Int) {
        // make the http call to the web server using retrofit
        val call: Call<String?>? = api.getUserAccountTransactions(accessToken, accountId)
        call?.enqueue(object : Callback<String?> {
            // if the server gives the JSON response, the compiler calls the onResponse() method
            override fun onResponse(
                call: Call<String?>?,
                response: Response<String?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val jsonResponse = response.body().toString()

                        // compiler then executes the saveTransactionsInfo() method
                        saveTransactionsInfo(jsonResponse)
                    }
                }
            }

            override fun onFailure(call: Call<String?>?, t: Throwable?) {}
        })
    }

    private fun saveTransactionsInfo(response: String) {
        try {
            val transactionsString = preferenceHelper!!.transactions

            val newTransactions = JSONArray(response)
            val currentTransactions = JSONArray(transactionsString)

            for (i in 0 until newTransactions.length()) {
                currentTransactions.put(newTransactions.getJSONObject(i))
            }

            val transactions = newTransactions.toString()

            val gson = Gson()
            val transactionsArrayType = object : TypeToken<ArrayList<Transaction>>() {}.type
            val transactionsArray: ArrayList<Transaction> =
                gson.fromJson(transactions, transactionsArrayType)
            transactionsListAdapter.setData(transactionsArray)

            preferenceHelper!!.putTransactions(transactions)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

}