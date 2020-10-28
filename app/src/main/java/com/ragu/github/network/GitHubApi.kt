package com.lcldost.partnerapp.network

import com.ragu.github.data.model.GitHubModel
import retrofit2.Call
import retrofit2.http.*

interface GitHubApi {
    @GET("repos/{userName}/{repoName}/issues?page=1&per_page=10")
    fun getPRDetails(@Path("userName") userName:String, @Path("repoName") repoName:String): Call<List<GitHubModel>>
}