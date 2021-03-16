package com.example.webservicesproject

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.example.webservicesproject.models.DataModel
import com.example.webservicesproject.retrofit.ApiClient
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createProgressDialog()

        getDetailsBtn.setOnClickListener {
            val postIdToFetch = postID_EditText.text
            if (postIdToFetch.isNullOrEmpty() || !postIdToFetch.isDigitsOnly() || postIdToFetch.toString()
                            .toInt() > 10 || postIdToFetch.toString().toInt() < 1
            ) {
                postID_EditText.error = "Enter digit between 1 to 10"
            } else {
                progressDialog.show()
                getData(postID_EditText.text.toString().toInt())
            }
    }
}
    private fun createProgressDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Please wait while we are fetching post...")
        progressDialog.setCancelable(false)
    }

    private fun getData(postID: Int) {
        val call = ApiClient.getClient.getPost(postID)
        call.enqueue(object : retrofit2.Callback<List<DataModel>> {
            override fun onFailure(call: retrofit2.Call<List<DataModel>>, t: Throwable) {
                Log.i("MainActivity", "Error is ${t.localizedMessage}")
                Toast.makeText(this@MainActivity, "There is some error while getting post", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }

            override fun onResponse(
                    call: retrofit2.Call<List<DataModel>>,
                    response: Response<List<DataModel>>
            ) {
                progressDialog.dismiss()
                Log.i("MainActivity", "Data is ${response.body()}")
                for (data in response.body()!!) {
                    //println("Data is ${data.postID}, ${data.postTitle}, ${data.postBody}")
                    id_TV.text = "Post Id: ${data.postID.toString()}"
                    title_TV.text = "Title: ${data.postTitle}"
                    body_TV.text = "Body: ${data.postBody}"
                }
            }

        })
    }
}