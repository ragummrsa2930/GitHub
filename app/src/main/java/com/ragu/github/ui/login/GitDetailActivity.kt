package com.ragu.github.ui.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lcldost.partnerapp.adapter.AdapterItem
import com.lcldost.partnerapp.viewmodel.GitHubViewModel


import com.ragu.github.R
import com.ragu.github.data.model.GitHubModel

class GitDetailActivity : AppCompatActivity(), AdapterItem.OnLoadMoreListener {

    private lateinit var gitHubViewModel: GitHubViewModel
    private lateinit var adapterItem: AdapterItem
    private lateinit var recyclerView: RecyclerView
    private var page: Int  = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_git)




        recyclerView = findViewById(R.id.recyclerView)
        val mLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.setHasFixedSize(true)
        adapterItem = AdapterItem(this, this)
        adapterItem.setLinearLayoutManager(mLayoutManager)
        adapterItem.setRecyclerView(recyclerView)
        recyclerView.adapter = adapterItem


        gitHubViewModel = ViewModelProvider(this).get(GitHubViewModel::class.java)

        gitHubViewModel.responseModel.observe(this@GitDetailActivity, Observer {
            if(it.isNotEmpty() && it.size > 0){
                if(page == 1){
                    adapterItem.addAll(it as ArrayList<GitHubModel>)
                }else{
                    adapterItem.setProgressMore(false)
                    adapterItem.addItemMore(it as ArrayList<GitHubModel>)
                    adapterItem.setMoreLoading(false)
                    adapterItem.notifyDataSetChanged()
                }
            }else{
                adapterItem.setProgressMore(false)
            }
        })

        gitHubViewModel.error.observe(this@GitDetailActivity, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT)
        })

        gitHubViewModel.getPRDetails(intent.getStringExtra("name")!!, intent.getStringExtra("repo")!!, page)
    }

    override fun onLoadMore() {
        page += page
        gitHubViewModel.getPRDetails(intent.getStringExtra("name")!!, intent.getStringExtra("repo")!!, page)
    }
}