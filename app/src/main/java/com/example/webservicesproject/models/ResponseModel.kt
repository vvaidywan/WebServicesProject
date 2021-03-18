package com.example.webservicesproject.models

import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("posts")
    val posts: ArrayList<RetrofitModel>
)