package com.dut.trashdetect.extention

import android.view.animation.OvershootInterpolator
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.hideMini(duration: Long, translateX: Float, translateY: Float) {
    this.animate()
        .translationX(translateX)
        .translationY(translateY)
        .alpha(0f)
        .scaleX(0f)
        .scaleY(0f)
        .setInterpolator(OvershootInterpolator())
        .setDuration(duration)
        .start()
}

fun FloatingActionButton.showMini(duration: Long, translateX: Float, translateY: Float) {
    this.animate()
        .translationX(translateX)
        .translationY(translateY)
        .alpha(1f)
        .scaleX(1f)
        .scaleY(1f)
        .setInterpolator(OvershootInterpolator())
        .setDuration(duration)
        .start()
}
