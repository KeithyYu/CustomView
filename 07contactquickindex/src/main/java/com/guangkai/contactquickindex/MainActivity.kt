package com.guangkai.contactquickindex

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.simpleName
    }

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mTextView: TextView
    private lateinit var mQuicklyIndex: QuicklyIndex

    private var mHandler: Handler? = null

    private val mPersons = arrayListOf<Person>()

    private val mDataList = mutableListOf<RecyclerViewDataBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerView = findViewById(R.id.recycler_view)
        mTextView = findViewById(R.id.text)
        mQuicklyIndex = findViewById(R.id.quickly_index)
        mHandler = Handler(Looper.getMainLooper(), object : Handler.Callback {
            override fun handleMessage(msg: Message): Boolean {
                mTextView.visibility = View.GONE
                return true
            }
        })

        initData()

        initView()
    }

    private fun initView() {
        mQuicklyIndex.setOnTextChangeListener(object : QuicklyIndex.OnTextChangeListener {
            override fun onTextChange(world: String) {
                mTextView.visibility = View.VISIBLE
                mTextView.text = world

                mHandler?.removeCallbacksAndMessages(null)
                mHandler?.sendEmptyMessageDelayed(0, 3000)

                var scrollPosition: Int = 0
                for (index in mDataList.indices) {
                    if (world == mDataList[index].name) {
                        scrollPosition = index
                        break
                    }
                }

                // RecyclerView??????????????????
                val mScroller = TopSmoothScroller(this@MainActivity)
                mScroller.targetPosition = scrollPosition
                mRecyclerView.layoutManager?.startSmoothScroll(mScroller)
            }
        })

        mRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mRecyclerView.adapter = RecyclerViewAdapter(mDataList)
    }

    class TopSmoothScroller(context: Context) : LinearSmoothScroller(context) {
        override fun getHorizontalSnapPreference(): Int {
            return SNAP_TO_START
        }

        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }
    }

    /**
     * ???????????????
     */
    private fun initData() {
        mPersons.add(Person("?????????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("??????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("??????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("??????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("??????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("??????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("??????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("??????"))
        mPersons.add(Person("?????????"))
        mPersons.add(Person("??????"))
        mPersons.add(Person("?????????"))


        //??????
        Collections.sort(mPersons) { lhs, rhs ->
            lhs.pinyin.compareTo(rhs.pinyin)
        }

        // ??????adapter?????????
        for (index in 0 until mPersons.size) {
            val person = mPersons[index]
            if (index == 0) {
                mDataList.add(RecyclerViewDataBean(person.pinyin.substring(0, 1), 0))
                mDataList.add(RecyclerViewDataBean(person.name, 1))
            } else {
                val prePerson = mPersons[index - 1]
                if (person.pinyin.substring(0, 1) == prePerson.pinyin.substring(0, 1)) {
                    mDataList.add(RecyclerViewDataBean(person.name, 1))
                } else {
                    mDataList.add(RecyclerViewDataBean(person.pinyin.substring(0, 1), 0))
                    mDataList.add(RecyclerViewDataBean(person.name, 1))
                }
            }
        }
    }
}