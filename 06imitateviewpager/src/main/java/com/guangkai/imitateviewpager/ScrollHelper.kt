package com.guangkai.imitateviewpager

class ScrollHelper {
    private var mDistanceY: Float
    private var mDistanceX: Float
    private var mStartY: Float
    private var mStartX: Float
    private var mIsScrollFinished = false
    private var mStartScrollTime : Long
    private var mTotalScrollTime = 500

    constructor(startX: Float, startY: Float, distanceX: Float, distanceY: Float) {
        mStartX = startX
        mStartY = startY
        mDistanceX = distanceX
        mDistanceY = distanceY
        mStartScrollTime  = System.currentTimeMillis()
    }

    /**
     * 计算时间段内的偏移量
     *
     * @param callBack 回调函数
     */
    fun computeOffset(callBack : (currentX : Float, isScrollFinished : Boolean)->Unit){
        if (mIsScrollFinished) {
            return
        }

        val afterOffsetCurrentX : Float
        val passTime = System.currentTimeMillis() - mStartScrollTime
        if (passTime < mTotalScrollTime) {
            // 获取到偏移量
            val offset = passTime * mDistanceX / mTotalScrollTime

            // 当前滑动到的位置 = 开始时的位置 + 时间段内的便宜量
            afterOffsetCurrentX = mStartX + offset

            mIsScrollFinished = false
        } else {
            afterOffsetCurrentX = mStartX + mDistanceX
            mIsScrollFinished = true
        }

        callBack.invoke(afterOffsetCurrentX, mIsScrollFinished)
    }
}