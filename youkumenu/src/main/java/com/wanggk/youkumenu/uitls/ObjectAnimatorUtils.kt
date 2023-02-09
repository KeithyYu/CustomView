package com.wanggk.youkumenu.uitls

import android.animation.ObjectAnimator
import android.view.View

object ObjectAnimatorUtils {
    fun rotationView(view: View, startDelay: Long = 0) {
        view.pivotX = view.width / 2.toFloat()
        view.pivotY = view.height.toFloat()
        val objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 180f)
        objectAnimator.duration = 500
        objectAnimator.startDelay = startDelay
        objectAnimator.start()
    }

    fun revertRotationView(view: View, startDelay: Long = 0) {
        view.pivotX = view.width / 2.toFloat()
        view.pivotY = view.height.toFloat()
        val objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 180f, 360f)
        objectAnimator.duration = 500
        objectAnimator.startDelay = startDelay
        objectAnimator.start()
    }
}