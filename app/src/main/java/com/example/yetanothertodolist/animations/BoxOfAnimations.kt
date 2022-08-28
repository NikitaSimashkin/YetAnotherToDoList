package com.example.yetanothertodolist.animations

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.animation.ValueAnimator.REVERSE
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

object BoxOfAnimations {

    // use for floatingActionButton
    fun changeSizeAndColorAnimation(view: View, colorStart: Int, colorEnd: Int) {
        val durationRes = 100L

        val sizeAnimator = getSizeAnimator(durationRes, view, 1f, 1.5f)

        AnimatorSet().apply {
            play(sizeAnimator)
            start()
        }
    }

    private fun getSizeAnimator(
        durationRes: Long,
        view: View,
        start: Float,
        end: Float
    ): ValueAnimator {
        val sizeAnimator = ValueAnimator.ofFloat(start, end).apply {
            duration = durationRes
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Float
                view.scaleX = progress
                view.scaleY = progress
            }
            repeatCount = 1
            repeatMode = REVERSE
        }
        return sizeAnimator
    }
}