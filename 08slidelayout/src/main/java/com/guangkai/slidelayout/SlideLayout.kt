package com.guangkai.slidelayout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Scroller

class SlideLayout : FrameLayout {
    companion object {
        private val TAG = SlideLayout::class.simpleName
    }

    // menu开关状态监听
    private var mOnStatusChangeListener: OnStatusChangeListener? = null

    // 两个视图
    private lateinit var mSlideMainView: View
    private lateinit var mSlideDeleteView: View

    // 视图的宽高
    private var mMainViewWidth: Int = 0
    private var mDeleteViewWidth: Int = 0
    private var mViewHeight: Int = 0

    // 起始坐标
    private var mStartX: Float = 0f
    private var mStartY: Float = 0f

    private var mStartDownX = 0f
    private var mStartDownY = 0f

    // 滑动器
    private var mScroller: Scroller

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mScroller = Scroller(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 获取到视图的宽和高
        mMainViewWidth = mSlideMainView.measuredWidth
        mDeleteViewWidth = mSlideDeleteView.measuredWidth
        mViewHeight = measuredHeight
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // 摆放子视图的位置
        mSlideMainView.layout(0, 0, mMainViewWidth, mViewHeight)
        mSlideDeleteView.layout(mMainViewWidth, 0, mMainViewWidth + mDeleteViewWidth, mViewHeight)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        // 获取视图中的子视图
        mSlideMainView = findViewById(R.id.slide_main_text)
        mSlideDeleteView = findViewById(R.id.slide_delete)
    }

    /**
     * 触摸事件实现滑动
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = event.x
                mStartY = event.y
                mStartDownX = event.x
                mStartDownY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = event.x
                val endY = event.y

                // 1. 获取到手指的滑动偏移量
                val distanceX = endX - mStartX

                // 2. 通过当前View已经偏移的偏移量getScrollX()+偏移量(distance,此处用-号是因为滑动方向与视图坐标系是反着的，所以用-)
                var scrollToX = scrollX - distanceX
                Log.d(
                    TAG,
                    "ACTION_MOVE, mStartX : $mStartX, scrollX : $scrollX, distanceX : $distanceX, scrollToX : $scrollToX"
                )

                // 3. 当前的偏移的距离的合理范围限制
                if (scrollToX < 0) {
                    scrollToX = 0f
                } else if (scrollToX > mDeleteViewWidth) {
                    scrollToX = mDeleteViewWidth.toFloat()
                }

                // 4. 跟随手指滑动
                scrollTo(scrollToX.toInt(), 0)
                // 5. 每次Move事件之后重新刷新了getScrollX()的值，所以此处刷新一下起始位置，确保获取到每次都是两次Move事件中的一段偏移量
                mStartX = event.x
                mStartY = event.y

                // 处理左右滑动和上下滑动冲突导致MotionEvent.ACTION_UP丢失
                val DX = Math.abs(endX - mStartDownX)
                val DY = Math.abs(endY - mStartDownY)
                if (DX > DY && DX > 10) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }

            MotionEvent.ACTION_UP -> {
                // 手指抬起时，根据滑动间距判断是回弹还是打开
                Log.d(TAG, "ACTION_UP, scrollX : $scrollX")

                val halfDeleteViewWidth = mDeleteViewWidth / 2
                if (scrollX >= halfDeleteViewWidth) {
                    // 打开
                    openMenu()
                } else {
                    // 回弹(关闭)
                    closeMenu()
                }
            }
        }
        return true
    }

    fun closeMenu() {
        // 关闭时目标偏移量为0
        val autoScrollDistanceX = 0 - scrollX

        mScroller.startScroll(scrollX, 0, autoScrollDistanceX, 0)

        invalidate()

        mOnStatusChangeListener?.onClose(this)
    }

    fun openMenu() {
        // 打开时目标偏移量为删除控件的宽度，实际已经偏移量scrollX，两者差就是需要在X轴的偏移量
        val autoScrollDistanceX = mDeleteViewWidth - scrollX

        // 开始平滑移动
        mScroller.startScroll(scrollX, 0, autoScrollDistanceX, 0)

        // 强制刷新
        invalidate()

        mOnStatusChangeListener?.onOpen(this)
    }

    override fun computeScroll() {
        super.computeScroll()

        // invalidate()强刷操作会回调该方法，此处的方法是计算从开始滑动到图像绘制这段时间内的偏移量
        if (mScroller.computeScrollOffset()) {

            Log.d(TAG, "computeScroll scrollX : $scrollX, mScroller.currX : ${mScroller.currX}")

            // 每次滑动一点
            scrollTo(mScroller.currX, 0)

            // 再次强刷
            invalidate()
        }
    }

    // 当内容mSlideMainView设置点击事件后，由于事件被消费，导致无法滑动，要在本层检测到滑动时进行拦截
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        super.onInterceptTouchEvent(event)
        var intercept = false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = event.x
                mStartY = event.y
                mStartDownX = event.x
                mStartDownY = event.y

                mOnStatusChangeListener?.onDown(this)
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = event.x

                // 当是滑动事件时就拦截
                val DX = Math.abs(endX - mStartDownX)
                if (DX > 10) {
                    intercept = true
                }
            }

            MotionEvent.ACTION_UP -> {
            }
        }
        return intercept
    }

    interface OnStatusChangeListener {
        fun onDown(slideLayout: SlideLayout)
        fun onOpen(slideLayout: SlideLayout)
        fun onClose(slideLayout: SlideLayout)
    }

    fun setOnStatusChangeListener(onStatusChangeListener: OnStatusChangeListener) {
        mOnStatusChangeListener = onStatusChangeListener
    }
}