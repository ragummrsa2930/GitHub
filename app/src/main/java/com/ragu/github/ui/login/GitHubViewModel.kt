package com.lcldost.partnerapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blowhorn.checkout.network.ApiClient
import com.lcldost.partnerapp.network.GitHubApi
import com.ragu.github.data.model.GitHubModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GitHubViewModel : ViewModel() {
    val TAG = GitHubViewModel::class.java.simpleName
    var error = MutableLiveData<String>()
    var responseModel = MutableLiveData<List<GitHubModel>>()
    fun getPRDetails(userName: String, repoName: String) {
        val api = ApiClient.getClient().create(GitHubApi::class.java)
        api.getPRDetails(userName, repoName)
           .enqueue(object : Callback<List<GitHubModel>> {
                override fun onFailure(call: Call<List<GitHubModel>>, t: Throwable) {
                   error.value = t.message
                    Log.d(TAG, "onFailure ")
                }
                override fun onResponse(
                    call: Call<List<GitHubModel>>,
                    response: Response<List<GitHubModel>>
                ) {
                    Log.d(TAG, "onResponse " + response.body())
                    Log.d(TAG, "onResponse " + response.message())
                    if (response.code() == 200) {
                        responseModel.value = response.body()
                    } else {
                        error.value = "Error"
                    }
                }
           })
    }

}