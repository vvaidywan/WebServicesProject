package com.example.webservicesproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.webservicesproject.R
import com.example.webservicesproject.models.RetrofitModel

class DataAdapter(val myArrayList: ArrayList<RetrofitModel>, val context: Context) : RecyclerView.Adapter<DataAdapter.MyCustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCustomViewHolder {
        return MyCustomViewHolder(
            (LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false))
        )
    }

    override fun onBindViewHolder(holder: MyCustomViewHolder, position: Int) {
        holder.tv_name.text = myArrayList[position].postName
        holder.tv_message.text = myArrayList[position].postMessage
        Glide.with(context).load(myArrayList[position].postImageUrl).apply(RequestOptions().placeholder(R.drawable.ic_launcher_background)).into(holder.iv_image)
    }

    override fun getItemCount(): Int {
        return myArrayList.size
    }

    class MyCustomViewHolder(item_view : View) : RecyclerView.ViewHolder(item_view) {
        val iv_image = item_view.findViewById<ImageView>(R.id.iv_image)
        val tv_name = item_view.findViewById<TextView>(R.id.tv_name)
        val tv_message = item_view.findViewById<TextView>(R.id.tv_message)

    }
}