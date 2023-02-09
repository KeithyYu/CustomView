package com.wanggk.youkumenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.wanggk.youkumenu.uitls.ObjectAnimatorUtils

class YouKuMenuMainActivity : AppCompatActivity() {

    private lateinit var mLevel1 : RelativeLayout
    private lateinit var mLevel2 : RelativeLayout
    private lateinit var mLevel3 : RelativeLayout
    private lateinit var mLevel1Home : ImageView
    private lateinit var mLevel2Menu : ImageView
    private lateinit var mLevel2MyYouKu : ImageView
    private lateinit var mLevel2Search : ImageView
    private lateinit var mLevel3Channel1 : ImageView
    private lateinit var mLevel3Channel2 : ImageView
    private lateinit var mLevel3Channel3 : ImageView
    private lateinit var mLevel3Channel4 : ImageView
    private lateinit var mLevel3Channel5 : ImageView
    private lateinit var mLevel3Channel6 : ImageView
    private lateinit var mLevel3Channel7 : ImageView

    private var mIsLevel3Hide = false
    private var mIsLevel2Hide = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_you_ku_menu_main)
        findView()
        initView()
    }

    /**
     * 初始化视图
     */
    private fun initView() {
        setOnClickListener()
    }

    /**
     * 设置点击监听
     */
    private fun setOnClickListener() {
        val clickListener = ClickListener()
        mLevel2Menu.setOnClickListener(clickListener)
        mLevel1Home.setOnClickListener(clickListener)
    }

    /**
     * 初始化变量
     */
    private fun findView() {
        mLevel1 = findViewById(R.id.menu_level1)
        mLevel2 = findViewById(R.id.menu_level2)
        mLevel3 = findViewById(R.id.menu_level3)

        mLevel1Home = findViewById(R.id.menu_level1_home)
        mLevel2Menu = findViewById(R.id.menu_level2_menu)
        mLevel2MyYouKu = findViewById(R.id.menu_level2_myyouku)
        mLevel2Search = findViewById(R.id.menu_level2_search)
        mLevel3Channel1 = findViewById(R.id.menu_level3_channel1)
        mLevel3Channel2 = findViewById(R.id.menu_level3_channel2)
        mLevel3Channel3 = findViewById(R.id.menu_level3_channel3)
        mLevel3Channel4 = findViewById(R.id.menu_level3_channel4)
        mLevel3Channel5 = findViewById(R.id.menu_level3_channel5)
        mLevel3Channel6 = findViewById(R.id.menu_level3_channel6)
        mLevel3Channel7 = findViewById(R.id.menu_level3_channel7)
    }

    /**
     * 点击事件监听
     */
    inner class ClickListener : View.OnClickListener{
        override fun onClick(view: View) {
            when(view.id) {
                R.id.menu_level2_menu -> {
                    mIsLevel3Hide = if (mIsLevel3Hide) {
                        ObjectAnimatorUtils.revertRotationView(mLevel3)
                        false
                    } else {
                        ObjectAnimatorUtils.rotationView(mLevel3)
                        true
                    }
                }

                R.id.menu_level1_home -> {
                    mIsLevel2Hide = if (mIsLevel2Hide) {
                        ObjectAnimatorUtils.revertRotationView(mLevel2)
                        false
                    } else {
                        ObjectAnimatorUtils.rotationView(mLevel2)
                        true
                    }

                    if (!mIsLevel3Hide) {
                        ObjectAnimatorUtils.rotationView(mLevel3, 200)
                        mIsLevel3Hide = true
                    }
                }
            }
        }

    }
}