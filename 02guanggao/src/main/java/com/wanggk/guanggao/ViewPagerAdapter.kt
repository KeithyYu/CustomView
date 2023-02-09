package com.wanggk.guanggao

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(var mDataList: List<Int> = listOf()) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewPaperViewHolder>() {
    private val TAG = ViewPagerAdapter::class.java.simpleName

    private lateinit var mOnTouchListener: OnTouchListener
    private lateinit var mOnClickListener: OnClickListener

    class ViewPaperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImageView: ImageView

        init {
            mImageView = itemView.findViewById(R.id.vp_item_iv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPaperViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_paper_layout_item, parent, false)
        return ViewPaperViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE - 1
    }

    override fun onBindViewHolder(holder: ViewPaperViewHolder, position: Int) {
        val realPosition = position % mDataList.size
        if (realPosition < 0 || realPosition >= mDataList.size) {
            Log.d(TAG, "onBindViewHolder position param error : $realPosition")
            return
        }
        val image = ContextCompat.getDrawable(holder.mImageView.context, mDataList[realPosition])
        holder.mImageView.background = image

        holder.itemView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                mOnTouchListener.onTouch(event)
                return false
            }
        })

        holder.itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View) {
                mOnClickListener.onClick(view, holder.adapterPosition)
            }
        })
    }

    interface OnTouchListener{
        fun onTouch(event: MotionEvent)
    }

    fun setOnTouchListener(onTouchListener: OnTouchListener) {
        mOnTouchListener = onTouchListener
    }

    interface OnClickListener{
        fun onClick(view: View, adapterPosition: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        mOnClickListener = onClickListener
    }
}