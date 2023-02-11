package com.guangkai.imitateviewpager

import android.content.Context
import android.icu.text.CaseMap.Fold
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Scroller
import kotlin.math.abs

class MyViewPaper : ViewGroup {
    // 页面改变监听器
    private var mOnPaperChangeListener: OnPaperChangeListener? = null

    /**
     * 通过手势识别器实现页面的滑动
     *
     * 使用手势识别器，分为三个步骤
     * 1.定义手势识别器
     * 2.初始化手势识别器
     * 3.在OnTouchEvent中传入Event事件给手势识别器
     */
    private val mGestureDetector: GestureDetector

    // Action_down的时候X的坐标
    private var mStartX: Float = 0f

    // 当前索引
    private var mCurrentIndex: Int = 0

    // 视图宽度的一半
    private var mHalfWidth: Int

    // 滑动计算器
    private var mScrollHelper: ScrollHelper? = null

    // 使用系统的Scroll替换我们自己写的ScrollHelper
    private var mScroller : Scroller

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mGestureDetector = GestureDetector(context, object : GestureDetector.OnGestureListener {
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onShowPress(e: MotionEvent) {
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            /**
             * 滑动的时候回调
             *
             * @param e1 滑动开始时的事件
             * @param e2 滑动结束时的事件
             * @param distanceX X轴上滑动的距离
             * @param distanceY Y轴上滑动的距离
             */
            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                scrollBy(distanceX.toInt(), scrollY)
                return true
            }

            override fun onLongPress(e: MotionEvent) {
            }

            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                return true
            }

        })

        mHalfWidth = width / 2
        mScroller = Scroller(context)
    }

    /**
     * 摆放子视图的位置
     *
     * @param changed 布局是否改变
     * @param l 视图左上角坐标的x轴
     * @param t 视图左上角坐标的y轴
     * @param r 视图右下角坐标的x轴
     * @param b 视图右下角坐标的y轴
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            child.layout(index * width, scrollY, (index + 1) * width, height)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 为了使事件能够回传，不然出现划不动的情况
        super.onTouchEvent(event)

        // 将触摸事件传给手势识别器
        mGestureDetector.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = event.x
            }

            MotionEvent.ACTION_MOVE -> {
            }

            MotionEvent.ACTION_UP -> {
                // 手指抬起时X的坐标
                val endX = event.x

                // 当滑动的页面超过半屏时，将会自动滑动到下一屏
                var tempIndex = mCurrentIndex
                if ((endX - mStartX) > mHalfWidth) {
                    tempIndex--
                } else if ((mStartX - endX) > mHalfWidth) {
                    tempIndex++
                }

                // 滑动到指定索引的paper页
                scrollToPaper(tempIndex)
            }
        }

        // 事件被消费
        return true
    }

    fun scrollToPaper(pagerIndex: Int, halfWidth: Int = mHalfWidth) {
        var tempIndex = pagerIndex
        // 索引页安全性判断
        if (tempIndex < 0) {
            tempIndex = 0
        }
        if (tempIndex > childCount - 1) {
            tempIndex = childCount - 1
        }

        // 重新赋值当前页面所在的索引
        mCurrentIndex = tempIndex

        // 当页面改变时回调
        mOnPaperChangeListener?.onPaperChange(mCurrentIndex)

        // 第一种方式使用scrollTo滑动到指定位置,这样直接滑动太生硬，所以使用分段滑动解决生硬滑动的问题
//        scrollTo(tempIndex * width, scrollY)
        // 第二种方式使用分段滑动解决生硬滑动的问题
//        val distanceX = tempIndex * width - scrollX
//        mScrollHelper = ScrollHelper(scrollX.toFloat(), scrollY.toFloat(), distanceX.toFloat(), 0f)
//        scroll()

        // 第三种使用系统的Scroller替换我们自定义的ScrollHelper解决滑动生硬的问题
        val distanceX = tempIndex * width - scrollX
        mScroller.startScroll(scrollX, scrollY, distanceX, 0, abs(distanceX))
        invalidate()
    }

    /**
     * 分段滑动-平滑的滑动
     */
    private fun scroll() {
        mScrollHelper?.computeOffset { currentX, isScrollFinished ->
            scrollTo(currentX.toInt(), scrollY)

            if (!isScrollFinished) {
                invalidate() // 触发onDraw、computeScroll
            }
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        // 第二种方式：使用自定义的ScrollHelper解决滑动生硬的问题
//        scroll()

        // 第三种方式：使用系统的Scroller解决生硬滑动的问题
        if(mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, 0)
            invalidate()
        }
    }

    interface OnPaperChangeListener{
        fun onPaperChange(paperIndex:Int)
    }

    fun setOnPaperChangeListener(onPaperChangeListener: OnPaperChangeListener) {
        mOnPaperChangeListener = onPaperChangeListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 解决一级页面如果是一个ViewGroup的话，那么其二级View无法显示的问题
        for(index in 0 until childCount) {
            val child = getChildAt(index)
            child.measure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    private var mInterceptDownX: Float = 0f
    private var mInterceptDownY: Float = 0f

    // 有需要时放开此处代码
    // 内外层的ViewGroup滑动冲突的解决方法
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // 此处是为了解决滑动时闪现的问题
        mGestureDetector.onTouchEvent(ev)

        // 滑动冲突的解决
        var result = false
        when(ev.action){
            MotionEvent.ACTION_DOWN-> {
                mInterceptDownX = ev.x
                mInterceptDownY = ev.y
            }
            MotionEvent.ACTION_MOVE->{
                val endX = ev.x
                val endY = ev.y

                val distanceX = Math.abs(endX - mInterceptDownX)
                val distanceY = Math.abs(endY - mInterceptDownY)

                if (distanceX > distanceY && distanceX > 10) {
                    result = true
                }
            }
            MotionEvent.ACTION_UP -> {
            }
        }

        return result
    }
}