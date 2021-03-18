package com.example.webservicesproject.models

import com.google.gson.annotations.SerializedName

data class RetrofitModel (
    @SerializedName("name")
    val postName : String,
    @SerializedName("message")
    val postMessage : String,
    @SerializedName("profileImage")
    val postImageUrl : String

)