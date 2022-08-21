package com.example.yetanothertodolist.animations

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.REVERSE
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class BoxOfAnimations {

    companion object {

        // use for floatingActionButton
        fun changeSizeAndColorAnimation(view: View, colorStart: Int, colorEnd: Int) {
            val durationRes = 100L

            val sizeAnimator = getSizeAnimator(durationRes, view, 1f, 1.5f)

            val colorAnimator = getColorAnimator(colorStart, colorEnd, durationRes, view)

            AnimatorSet().apply {
                play(sizeAnimator)
                    //.with(colorAnimator)
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

        private fun getColorAnimator(
            colorStart: Int,
            colorEnd: Int,
            durationRes: Long,
            view: View
        ): ValueAnimator {
            val colorAnimator =
                ValueAnimator.ofObject(ArgbEvaluator(), colorStart, colorEnd).apply {
                    duration = durationRes
                    interpolator = AccelerateDecelerateInterpolator()
                    addUpdateListener {
                        view.background.setTint(it.animatedValue as Int)
                    }
                    repeatCount = 1
                    repeatMode = REVERSE
                }
            return colorAnimator
        }
    }
}