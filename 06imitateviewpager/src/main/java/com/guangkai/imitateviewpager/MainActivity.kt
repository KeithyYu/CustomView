package com.guangkai.imitateviewpager

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup

class MainActivity : Activity() {
    private lateinit var mMyViewPaper: MyViewPaper

    private lateinit var mRadioGroup: RadioGroup

    // 所有图片的Id
    private val mImageIds = listOf<Int>(
        R.drawable.a1,
        R.drawable.a2,
        R.drawable.a3,
        R.drawable.a4,
        R.drawable.a5,
        R.drawable.a6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        mMyViewPaper = findViewById(R.id.my_vp)
        mRadioGroup = findViewById(R.id.radio_group)

        // 将所有视图添加到自定义的ViewPaper中
        for (index in mImageIds.indices) {
            val imageView = ImageView(this)
            imageView.setBackgroundResource(mImageIds[index])
            mMyViewPaper.addView(imageView)
        }

        // 临时添加一个测试页面,结果是此页面中的内容无法显示，但是页面显示出来了。
        // 原因是没有进行测量操作
        val view = View.inflate(this,R.layout.layout_test, null)
        mMyViewPaper.addView(view, 2)

        // 根据自定义控件中子视图的数量动态添加radiobutton的个数
        for (index in 0 until mMyViewPaper.childCount) {
            val radioButton = RadioButton(this)
            radioButton.id = index
            if (index == 0) {
                radioButton.isChecked = true
            }

            mRadioGroup.addView(radioButton)
        }

        // 设置RadioGroup的监听器
        mRadioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
                mMyViewPaper.scrollToPaper(checkedId)
            }
        })

        // 当页面切换时回调监听
        mMyViewPaper.setOnPaperChangeListener(object : MyViewPaper.OnPaperChangeListener {
            override fun onPaperChange(paperIndex: Int) {
                mRadioGroup.check(paperIndex)
            }
        })
    }
}