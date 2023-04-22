package com.arya.submission2.data.remote.retrofit

import com.arya.submission2.data.remote.response.GithubResponse
import com.arya.submission2.data.remote.response.Item
import com.arya.submission2.data.remote.response.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun searchUser(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<User>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<ArrayList<Item>>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<Item>>
}