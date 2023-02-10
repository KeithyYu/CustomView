package com.guangkai.popupwindow

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(var mDataList: List<String>) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    private lateinit var mOnClickListener: MyOnClickListener
    private val TAG = RecyclerViewAdapter::class.simpleName

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mTitle: TextView

        val mDelete: ImageView

        init {
            mTitle = itemView.findViewById(R.id.title)
            mDelete = itemView.findViewById(R.id.delete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position < 0 || position >= mDataList.size) {
            Log.d(TAG, "onBindViewHolder param error : $position")
            return
        }

        val data = mDataList[position]
        holder.mTitle.text = data

        holder.itemView.setOnClickListener(object : OnClickListener {
            override fun onClick(view: View) {
                mOnClickListener.onClick(holder.mTitle.text.toString(), holder.adapterPosition)
            }
        })

        holder.mDelete.setOnClickListener(object : OnClickListener {
            override fun onClick(view: View) {
                mOnClickListener.delete(holder.mTitle.text.toString(), holder.adapterPosition)
            }
        })
    }

    interface MyOnClickListener {
        fun onClick(title: String, adapterPosition: Int)

        fun delete(title: String, adapterPosition: Int)
    }

    fun setMyOnClickListener(myOnClickListener: MyOnClickListener) {
        mOnClickListener = myOnClickListener
    }
}