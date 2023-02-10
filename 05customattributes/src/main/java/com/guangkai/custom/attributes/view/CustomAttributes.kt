package com.guangkai.custom.attributes.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.guangkai.custom.attributes.R

class CustomAttributes : View {
    private val TAG = CustomAttributes::class.simpleName

    private var mTitle: String? = null

    private var mAge: Int = 0

    private var mBg: Drawable? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomAttributes)
        for (index in 0 until typeArray.indexCount) {
            val styleableIndex = typeArray.getIndex(index)
            when (styleableIndex) {
                R.styleable.CustomAttributes_title -> {
                    mTitle = typeArray.getString(styleableIndex)
                    Log.d(TAG, "$mTitle")
                }

                R.styleable.CustomAttributes_age -> {
                    mAge = typeArray.getInt(styleableIndex, 0)
                }

                R.styleable.CustomAttributes_bg -> {
                    mBg = typeArray.getDrawable(styleableIndex)
                }
            }
        }
        typeArray.recycle()
    }

    @SuppressLint("ResourceAsColor")
    override fun onDraw(canvas: Canvas) {
        val textPaint = Paint()
        textPaint.color = R.color.black
        textPaint.textSize = 60f
        canvas.drawText("$mTitle ------ $mAge", 50f, 50f, textPaint)

        val paint = Paint()
        paint.isAntiAlias = true
        canvas.drawBitmap((mBg as BitmapDrawable).bitmap, 50f, 100f, paint)
    }
}