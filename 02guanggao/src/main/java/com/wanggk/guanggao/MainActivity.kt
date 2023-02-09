package com.wanggk.guanggao

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    companion object {
        private const val AUTO_SWITCH_PAGER_TIME = 4000L
    }

    private val TAG = MainActivity::class.simpleName

    private lateinit var mViewPaper: ViewPager2
    private lateinit var mDescription: TextView
    private lateinit var mPointGroup: LinearLayout
    private var mIsDraggingCancelAutoRun = false
    private var mPreRedIndex = -1

    private var mImageIds = listOf(
        R.drawable.a,
        R.drawable.b,
        R.drawable.c,
        R.drawable.d,
        R.drawable.e
    )

    // 图片标题集合
    private val mImageDescriptions = listOf(
        "尚硅谷波河争霸赛！",
        "凝聚你我，放飞梦想！",
        "抱歉没座位了！",
        "7月就业名单全部曝光！",
        "平均起薪11345元"
    )

    private val mHandler by lazy { WeakReferenceHandler(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewPaper = findViewById(R.id.viewpaper)
        mDescription = findViewById(R.id.description)
        mPointGroup = findViewById(R.id.ll_point_group)

        initView()
    }

    private fun initView() {
        // 添加红点
        for (index in mImageIds.indices) {
            val pointIV = ImageView(this)
            val parma = LinearLayout.LayoutParams(dpToPx(this, 10), dpToPx(this, 10))
            pointIV.setBackgroundResource(R.drawable.point_selector)
            pointIV.isEnabled = false

            if (index != 0) {
                parma.leftMargin = dpToPx(this, 10)
            }

            pointIV.layoutParams = parma
            mPointGroup.addView(pointIV)
        }

        mViewPaper.apply {
            adapter = ViewPagerAdapter(mImageIds)
            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // 适配无限循环
                    val index = position % mImageDescriptions.size
                    mDescription.text = mImageDescriptions[index]

                    // 高亮红点
                    mPointGroup.getChildAt(index).isEnabled = true
                    if (mPreRedIndex != -1 && mPreRedIndex != index) {
                        mPointGroup.getChildAt(mPreRedIndex).isEnabled = false
                    }
                    mPreRedIndex = index
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    // 解决当用户按下ACTION_DOWN之后，并没有产生ACTION_UP,导致用户操作之后不能自动循环
                    when (state) {
                        ViewPager2.SCROLL_STATE_DRAGGING -> {
                            mIsDraggingCancelAutoRun = true
                            autoRun(false)
                        }
                        ViewPager2.SCROLL_STATE_IDLE -> {
                            if (mIsDraggingCancelAutoRun) {
                                mIsDraggingCancelAutoRun = false
                                autoRun(true)
                            }
                        }
                        ViewPager2.SCROLL_STATE_SETTLING -> {
                        }
                    }
                }
            })

            // 为了一开始能够向左滑动,所以要设置ViewPaper的起始位置在所有加载的页面个数的中间位置。
            // 为了保证在中间位置默认还是从0位置开始，那么需要保证取到的数是mImageIds.size的整数倍。
            val midIndex = Int.MAX_VALUE / 2 - Int.MAX_VALUE / 2 % mImageIds.size
            currentItem = midIndex

            // 触发无限自动循环
            mHandler.sendEmptyMessageDelayed(0, AUTO_SWITCH_PAGER_TIME)

            // 用户触摸事件时即停止自动循环
            (adapter as ViewPagerAdapter).setOnTouchListener(object :
                ViewPagerAdapter.OnTouchListener {
                override fun onTouch(event: MotionEvent) {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            Log.d(TAG, "ACTION_DOWN")
                            autoRun(false)
                        }
                        MotionEvent.ACTION_UP -> {
                            Log.d(TAG, "ACTION_UP")
                            autoRun(true)
                        }
                    }
                }
            })

            // 添加点击事件
            (adapter as ViewPagerAdapter).setOnClickListener(object:ViewPagerAdapter.OnClickListener{
                override fun onClick(view: View, adapterPosition: Int) {
                    val realPosition = adapterPosition % mImageIds.size
                    Toast.makeText(context, mImageDescriptions[realPosition], Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return dp * (context.resources
            .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun pxToDp(context: Context, px: Int): Int {
        return px / (context.resources
            .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun spToPx(context: Context, sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
    }

    fun pxToSp(context: Context, px: Float): Int {
        val fontScale: Float = context.resources.displayMetrics.scaledDensity
        return (px / fontScale + 0.5f).toInt()
    }

    //static + 弱引用
    class WeakReferenceHandler(activity: MainActivity) : Handler(Looper.getMainLooper()) {
        private val mRef: WeakReference<MainActivity> = WeakReference(activity)
        override fun handleMessage(msg: Message) {
            mRef.get()?.run {
                mViewPaper.currentItem = mViewPaper.currentItem + 1

                autoRun(true)
            }
        }
    }

    /**
     * 是否自动翻页
     *
     * @param isRun 是否运行
     */
    fun autoRun(isRun: Boolean) {
        if (isRun) {
            mHandler.removeCallbacksAndMessages(null)

            // 延迟4秒发送消息
            mHandler.sendEmptyMessageDelayed(0, AUTO_SWITCH_PAGER_TIME)
        } else {
            mHandler.removeCallbacksAndMessages(null)
        }
    }
}