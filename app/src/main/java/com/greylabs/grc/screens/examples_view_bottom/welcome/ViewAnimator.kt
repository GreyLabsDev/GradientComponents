package com.greylabs.grc.screens.examples_view_bottom.welcome

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation

class ViewAnimator(val viewToAnimate: View) {
    var duration = 400L

    fun startAnimator() {
        scaleView(viewToAnimate, duration, 0L, 1f, 1.06f, object : Animation.AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                scaleView(viewToAnimate, duration, 0L, 1.06f, 1f, object : Animation.AnimationListener{
                    override fun onAnimationRepeat(p0: Animation?) {}
                    override fun onAnimationEnd(p0: Animation?) {
                        startAnimator()
                    }
                    override fun onAnimationStart(p0: Animation?) {}
                })
            }

            override fun onAnimationStart(p0: Animation?) {}
        })
    }

    private fun scaleView(v: View, duration: Long, offset: Long, startScale: Float, endScale: Float, listener: Animation.AnimationListener) {
        val anim = ScaleAnimation(
            startScale, endScale, // Start and end values for the X axis scaling
            startScale, endScale, // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0.5f) // Pivot point of Y scaling
        anim.fillAfter = true
        anim.duration = duration
        anim.startOffset = offset

        anim.setAnimationListener(listener)
        v.startAnimation(anim)
    }
}