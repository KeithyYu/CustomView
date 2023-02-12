package com.guangkai.contactquickindex

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class QuicklyIndex : View {
    private val TAG = QuicklyIndex::class.simpleName

    private var mOnTextChangeListener: OnTextChangeListener? = null

    // 每个Item的宽和高
    private var mItemWidth: Int = 0
    private var mItemHeight: Int = 0

    private var mPaint: Paint = Paint()

    private var mDownIndex: Int = -1

    private val mWorlds = arrayOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I",
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
        "W", "X", "Y", "Z"
    )

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        // 初始化画笔
        mPaint.isAntiAlias = true
        mPaint.color = Color.WHITE
        mPaint.textSize = 48f
        mPaint.isFakeBoldText = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 获取每一个字母Item的宽和高
        mItemWidth = measuredWidth
        mItemHeight = measuredHeight / mWorlds.size
    }

    override fun onDraw(canvas: Canvas) {
        for (index in 0 until mWorlds.size) {
            val world = mWorlds[index]

            // 获取文字的宽和高,通过给文字套一个边框的方法，就能间接获取到文字的大小
            val rect = Rect()
            mPaint.getTextBounds(world, 0, 1, rect)
            val worldWidth = rect.width()
            val worldHeight = rect.height()

            // 计算得出字母的索引位置
            val startX = (mItemWidth - worldWidth) / 2
            val startY = mItemHeight / 2 + worldHeight / 2 + index * mItemHeight

            if (mDownIndex != -1 && mDownIndex == index) {
                mPaint.color = Color.BLUE
            } else {
                mPaint.color = Color.WHITE
            }

            canvas.drawText(world, startX.toFloat(), startY.toFloat(), mPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                // 无论是Move还是Down都会触发字体颜色的改变
                val downX = event.y

                // 当前点击Y轴方向上的距离有多少个Item的高度就相当于是在第一个Item上
                mDownIndex = (downX / mItemHeight).toInt()

                // 回调监听
                mOnTextChangeListener?.onTextChange(mWorlds[mDownIndex])

                // 强制绘制，调用onDraw方法
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                // 手抬起来时恢复所有按压效果
                mDownIndex = -1
                invalidate()
            }
        }

        return true
    }

    /**
     * 当文字改变监听
     */
    interface OnTextChangeListener {
        fun onTextChange(world: String)
    }

    /**
     * 设置监听
     *
     * @param onTextChangeListener 监听器
     */
    fun setOnTextChangeListener(onTextChangeListener: OnTextChangeListener) {
        mOnTextChangeListener = onTextChangeListener
    }
}