package com.guangkai.slidelayout

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyRvAdapter(var mData: List<String>) : RecyclerView.Adapter<MyRvAdapter.MyViewHolder>() {
    companion object {
        private val TAG = MyRvAdapter::class.simpleName
    }

    // 滑动状态监听
    private var mSlideLayout: SlideLayout? = null

    // 点击监听器
    private var mOnClickListener: OnClickListener? = null

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mContentView: TextView
        var mDeleteView: TextView

        init {
            mContentView = itemView.findViewById(R.id.slide_main_text)
            mDeleteView = itemView.findViewById(R.id.slide_delete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_silde_main, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position > mData.size || position < 0) {
            Log.d(TAG, "onBindViewHolder position : $position error")
            return
        }
        val title = mData[position]
        holder.mContentView.text = title

        holder.mDeleteView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                (view.parent as SlideLayout).closeMenu()
                mOnClickListener?.onDelete(title, holder.adapterPosition)
            }
        })

        holder.mContentView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                mOnClickListener?.onClickContent(title, holder.adapterPosition)
            }
        })

        (holder.itemView as SlideLayout).setOnStatusChangeListener(MyStatusChangeListener())
    }

    interface OnClickListener {
        fun onDelete(title: String, adapterPosition: Int)

        fun onClickContent(title: String, adapterPosition: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        mOnClickListener = onClickListener
    }

    /**
     * 保证每次都只有一个Menu打开
     */
    inner class MyStatusChangeListener : SlideLayout.OnStatusChangeListener {
        override fun onDown(slideLayout: SlideLayout) {
            mSlideLayout?.closeMenu()
        }

        override fun onOpen(slideLayout: SlideLayout) {
            mSlideLayout = slideLayout
        }

        override fun onClose(slideLayout: SlideLayout) {
            mSlideLayout = null
        }
    }
}