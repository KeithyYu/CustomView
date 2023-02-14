package com.guangkai.contactquickindex

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(var mData: List<RecyclerViewDataBean>) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    private val TAG = RecyclerViewAdapter::class.simpleName

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTitle: TextView

        init {
            mTitle = itemView.findViewById(R.id.title)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mData[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mTitle.text = mData[position].name
    }
}