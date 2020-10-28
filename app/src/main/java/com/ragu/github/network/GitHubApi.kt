package com.lcldost.partnerapp.network

import com.ragu.github.data.model.GitHubModel
import retrofit2.Call
import retrofit2.http.*

interface GitHubApi {
    @GET("repos/{userName}/{repoName}/issues")
    fun getPRDetails(@Path("userName") userName:String, @Path("repoName") repoName:String
        ,@Query("page") cursor: Int,@Query("per_page") perPage: Int, ): Call<List<GitHubModel>>
}