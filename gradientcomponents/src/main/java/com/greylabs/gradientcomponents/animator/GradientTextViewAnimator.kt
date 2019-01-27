package com.greylabs.gradientcomponents.animator

import android.animation.TimeAnimator
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.Log
import android.widget.TextView
import com.greylabs.gradientcomponents.component.EndAnimationCallback
import java.util.*

class GradientTextViewAnimator(textView: TextView,
                               startPosition: FloatArray,
                               endPosition: FloatArray,
                               initialPosition: FloatArray,
                               animationDuration: Long,
                               gradientDirections: FloatArray,
                               gradientColorsList: ArrayList<Int>) {

    private val TAG = "GradientTextView"
    private var timeAnimator: TimeAnimator = TimeAnimator()
    private var nextAnimator: GradientTextViewAnimator? = null
    private var endCallBack: EndAnimationCallback? = null

    init {
        val positionsSteps = arrayListOf<Float>()
        timeAnimator.duration = animationDuration
        for (i in 0 until startPosition.size) {
            positionsSteps.add(((endPosition[i] - startPosition[i]) / animationDuration.toFloat()))
        }

        timeAnimator.setTimeListener{ animation, totalTime, deltaTime ->
            for (i in 0 until startPosition.size) {
                initialPosition[i] += positionsSteps[i] * deltaTime
            }
            if (totalTime < animationDuration) {
                val shader = LinearGradient(0f, 0f,
                        gradientDirections[0], gradientDirections[1],
                        gradientColorsList.toIntArray(),initialPosition,
                        Shader.TileMode.CLAMP)
                textView.paint.shader = shader
                textView.invalidate()
            } else {
                animation.cancel()
                endCallBack?.let {
                    it.startEndAnimation()
                } ?: run {
                    nextAnimator?.let {
                        it.startAnimation()
                    }
                }
            }
        }
    }

    fun startAnimation() {
        timeAnimator.start()
    }

    fun setEndCallback(inEndCallacbk: EndAnimationCallback) {
        endCallBack = inEndCallacbk
    }

    fun setNextAnimator(inAnimator: GradientTextViewAnimator) {
        nextAnimator = inAnimator
    }
}