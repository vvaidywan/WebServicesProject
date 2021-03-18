package com.example.webservicesproject

import android.app.ProgressDialog
import android.icu.lang.UCharacter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.webservicesproject.adapter.DataAdapter
import com.example.webservicesproject.models.ResponseModel
import com.example.webservicesproject.models.RetrofitModel
import com.example.webservicesproject.retrofit.RetrofitApiClient
import retrofit2.Response


class RetrofitActivity : AppCompatActivity() {

    var list = ArrayList<RetrofitModel>()
    private lateinit var adapter: DataAdapter
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit)

        adapter = DataAdapter(list,this@RetrofitActivity)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Fetching data...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val call = RetrofitApiClient.getClient.getPost()
        call.enqueue(object : retrofit2.Callback<ResponseModel>{

            override fun onFailure(call: retrofit2.Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@RetrofitActivity, "Error in post", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }

            override fun onResponse(
                call: retrofit2.Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
                if(response.isSuccessful){
                    list.addAll(response.body()?.posts?:ArrayList())
                    recyclerView.adapter!!.notifyDataSetChanged()
                    //progressDialog.dismiss()
                }
                progressDialog.dismiss()
            }

        })


    }
}