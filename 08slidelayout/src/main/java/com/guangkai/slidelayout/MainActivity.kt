package com.guangkai.slidelayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView

    private val mDataList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preLoadData()

        mRecyclerView = findViewById(R.id.main_recycler_view)

        initView()
    }

    private fun preLoadData() {
        for (index in 0 until 100) {
            mDataList.add("Slide $index")
        }
    }

    private fun initView() {
        mRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mRecyclerView.adapter = MyRvAdapter(mDataList)
        (mRecyclerView.adapter as MyRvAdapter).apply {
            setOnClickListener(object :
                MyRvAdapter.OnClickListener {
                override fun onDelete(title: String, adapterPosition: Int) {
                    mDataList.remove(title)
                    notifyDataSetChanged()
                }

                override fun onClickContent(title: String, adapterPosition: Int) {
                    Toast.makeText(this@MainActivity,title, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}