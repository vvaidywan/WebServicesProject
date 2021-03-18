package com.example.webservicesproject.retrofit

import com.example.webservicesproject.models.ResponseModel
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitApiInterface {
    // Define end point
    @GET("v2/posts.json")
    fun getPost(): Call<ResponseModel>
}