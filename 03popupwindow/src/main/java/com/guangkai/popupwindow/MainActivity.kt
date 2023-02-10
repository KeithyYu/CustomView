package com.guangkai.popupwindow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.simpleName

    private lateinit var mEditText: EditText
    private lateinit var mDropDown: ImageView
    private lateinit var mPopupWindow: PopupWindow
    private lateinit var mRecyclerView: RecyclerView

    private var mTitleDataList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mEditText = findViewById(R.id.edit_view)
        mDropDown = findViewById(R.id.dropdown_crow)
        mPopupWindow = PopupWindow(this@MainActivity)
        mRecyclerView = RecyclerView(this@MainActivity)

        preLoadData()
        initView()
    }

    private fun preLoadData() {
        for (index in 0 until 30) {
            mTitleDataList.add("$index title")
        }
    }

    private fun initView() {
        initRecyclerView()

        mDropDown.setOnClickListener(object : OnClickListener {
            override fun onClick(view: View) {
                mPopupWindow.contentView = mRecyclerView
                mPopupWindow.width = mEditText.width
                mPopupWindow.height = 1000
                mPopupWindow.isFocusable = true

                mPopupWindow.showAsDropDown(mEditText, 0, 0)
            }
        })
    }

    private fun initRecyclerView() {
        mRecyclerView.layoutManager =
            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        mRecyclerView.adapter = RecyclerViewAdapter(mTitleDataList)
        (mRecyclerView.adapter as RecyclerViewAdapter).apply {
            setMyOnClickListener(object :
            RecyclerViewAdapter.MyOnClickListener {
            override fun onClick(title: String, adapterPosition: Int) {
                Log.d(TAG, "click position : $adapterPosition, title : $title")
                mEditText.setText(title)
            }

            override fun delete(title: String, adapterPosition: Int) {
                mTitleDataList.remove(title)
                notifyItemMoved(adapterPosition + 1, mTitleDataList.size - 1)
            }
        })
        }
    }
}