package com.greylabs.gradientcomponents.animator

import android.animation.TimeAnimator
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.Log
import com.greylabs.gradientcomponents.component.EndAnimationCallback
import java.util.*

class GradientViewAnimator(paintDrawable: PaintDrawable,
                           rectShape: RectShape,
                           startPosition: FloatArray,
                           endPosition: FloatArray,
                           animationDuration: Long,
                           initialPosition: FloatArray) {

    private val TAG = "GradientView.Animator"

    private var timeAnimator: TimeAnimator = TimeAnimator()
    private var nextAnimator: GradientViewAnimator? = null
    private var endCallBack: EndAnimationCallback? = null

    init {
        val positionsSteps: java.util.ArrayList<Float> = java.util.ArrayList()

        for (i in 0 until startPosition.size) {
            positionsSteps.add(((endPosition[i] - startPosition[i]) / animationDuration.toFloat()))
        }

        timeAnimator.setTimeListener { animation, totalTime, deltaTime ->
            for (i in 0 until startPosition.size) {
                initialPosition[i] += positionsSteps[i] * deltaTime
            }
            if (totalTime < animationDuration) {
                paintDrawable.shape = rectShape
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

    fun setEndCallback(inEndCallback: EndAnimationCallback) {
        endCallBack = inEndCallback
    }

    fun setNextAnimator(inAnimator: GradientViewAnimator) {
        nextAnimator = inAnimator
    }

}