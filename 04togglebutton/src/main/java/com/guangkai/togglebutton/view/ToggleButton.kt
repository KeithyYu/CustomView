package com.guangkai.togglebutton.view

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.guangkai.togglebutton.R

class ToggleButton : View {
    private val TAG = ToggleButton::class.simpleName
    private var mBackgroundBitmap =
        BitmapFactory.decodeResource(resources, R.drawable.switch_background)
    private var mSlideBitmap = BitmapFactory.decodeResource(resources, R.drawable.slide_button)
    private var mSlideLeftMaxDistance: Int = 0
    private var mSlideLeftDistance: Float = 0f
    private val mPaint: Paint
    private var mStatus: Boolean = false
    private var mStartX: Float = 0f
    private var mDownStartX: Float = 0f
    private var mEnableClick : Boolean = true

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mSlideLeftMaxDistance = mBackgroundBitmap.width - mSlideBitmap.width
        mPaint = Paint()
        mPaint.isAntiAlias = true
        initView()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mBackgroundBitmap.width, mBackgroundBitmap.height)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(mBackgroundBitmap, 0f, 0f, mPaint)
        canvas.drawBitmap(mSlideBitmap, mSlideLeftDistance, 0f, mPaint)
    }

    /**
     * 初始化视图
     */
    private fun initView() {
        // 当前视图添加点击事件
        setOnClickListener(object : OnClickListener {
            override fun onClick(view: View) {
                if (mEnableClick) {
                    mStatus = !mStatus
                    flushView(mStatus)
                }
            }
        })
    }

    /**
     * 刷新页面
     *
     * @param status 开关状态
     */
    private fun flushView(status: Boolean) {
        mSlideLeftDistance = if (status) {
            mSlideLeftMaxDistance.toFloat()
        } else {
            0f
        }

        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = event.x
                mDownStartX = mStartX
                mEnableClick = true
            }

            MotionEvent.ACTION_MOVE -> {
                val endX = event.x
                val distance = endX - mStartX
                if (Math.abs(distance) > 5) {
                    mEnableClick = false
                }
                mSlideLeftDistance += distance

                Log.d(TAG, "ACTION_MOVE mSlideLeftDistance : $mSlideLeftDistance")
                // 避免跑出背景图片，所以对间距值进行处理
                if (mSlideLeftDistance > mSlideLeftMaxDistance) {
                    mSlideLeftDistance = mSlideLeftMaxDistance.toFloat()
                } else if (mSlideLeftDistance <= 0){
                    mSlideLeftDistance = 0f
                }

                // 重新绘制图片
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                if (!mEnableClick) {
                    val status = mSlideLeftDistance >= mSlideLeftMaxDistance / 2
                    Log.d(TAG, "ACTION_UP status : $status")
                    flushView(status)
                }
            }
        }

        return true
    }
}