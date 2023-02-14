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

                // RecyclerView滑动并且置顶
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
     * 初始化数据
     */
    private fun initData() {
        mPersons.add(Person("张晓飞"))
        mPersons.add(Person("杨光福"))
        mPersons.add(Person("胡继群"))
        mPersons.add(Person("刘畅"))
        mPersons.add(Person("钟泽兴"))
        mPersons.add(Person("尹革新"))
        mPersons.add(Person("安传鑫"))
        mPersons.add(Person("张骞壬"))
        mPersons.add(Person("温松"))
        mPersons.add(Person("李凤秋"))
        mPersons.add(Person("刘甫"))
        mPersons.add(Person("娄全超"))
        mPersons.add(Person("张猛"))
        mPersons.add(Person("王英杰"))
        mPersons.add(Person("李振南"))
        mPersons.add(Person("孙仁政"))
        mPersons.add(Person("唐春雷"))
        mPersons.add(Person("牛鹏伟"))
        mPersons.add(Person("姜宇航"))
        mPersons.add(Person("刘挺"))
        mPersons.add(Person("张洪瑞"))
        mPersons.add(Person("张建忠"))
        mPersons.add(Person("侯亚帅"))
        mPersons.add(Person("刘帅"))
        mPersons.add(Person("乔竞飞"))
        mPersons.add(Person("徐雨健"))
        mPersons.add(Person("吴亮"))
        mPersons.add(Person("王兆霖"))
        mPersons.add(Person("阿三"))
        mPersons.add(Person("李博俊"))


        //排序
        Collections.sort(mPersons) { lhs, rhs ->
            lhs.pinyin.compareTo(rhs.pinyin)
        }

        // 填充adapter的数据
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