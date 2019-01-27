package com.greylabs.gradientcomponents.component

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import com.greylabs.gradientcomponents.DEFAULT_COLOR_GRADIENT_TEXTVIEW
import com.greylabs.gradientcomponents.GradientOrientation
import com.greylabs.gradientcomponents.R
import com.greylabs.gradientcomponents.animator.GradientTextViewAnimator
import kotlin.collections.ArrayList

open class GradientTextView : TextView {

	private val TAG = "GradientTextView"

	private var gradientOrientation = GradientOrientation.Horizontal
	private val gradientDirection = floatArrayOf(0f, 0f)
	private var colorsCount: Int = 5
    private var gradientColorsList: ArrayList<Int> = ArrayList()
    private var gradientPositionsList: ArrayList<FloatArray> = ArrayList()
    private var gradientAnimators: ArrayList<GradientTextViewAnimator> = ArrayList()
	private var endAnimationCallback: EndAnimationCallback? = null
	private var animationStepDuration = 2000L
	private var animationOnStart = false
	private var animationLoop = false

	constructor(context: Context) : super (context) {}

	constructor(context: Context,
				attributeSet: AttributeSet) : super (context, attributeSet) {initAttributes(attributeSet)}

	constructor(context: Context,
				attributeSet: AttributeSet,
				defStyleAttr: Int) : super (context, attributeSet, defStyleAttr) {initAttributes(attributeSet)}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	constructor(context: Context,
				attributeSet: AttributeSet,
				defStyleAttr: Int,
				defStyleRes: Int) : super (context, attributeSet, defStyleAttr, defStyleRes) {initAttributes(attributeSet)}

	private fun initAttributes(attributeSet: AttributeSet) {
		val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.GradientTextView)

		animationOnStart = typedArray.getBoolean(R.styleable.GradientTextView_animationOnStart, false)
		animationLoop = typedArray.getBoolean(R.styleable.GradientTextView_animationLoop, false)
		animationStepDuration = typedArray.getInt(R.styleable.GradientTextView_animationStepDuration, 2000).toLong()

		gradientColorsList.add(Color.parseColor(DEFAULT_COLOR_GRADIENT_TEXTVIEW))
		gradientColorsList.add(Color.parseColor(DEFAULT_COLOR_GRADIENT_TEXTVIEW))
		gradientColorsList.add(Color.parseColor(DEFAULT_COLOR_GRADIENT_TEXTVIEW))

		typedArray.getString(R.styleable.GradientTextView_baseColors)?.let {
			gradientColorsList.clear()
			for (colorString in it.split(",")) {
				gradientColorsList.add(Color.parseColor(colorString.trim()))
			}
		}

		gradientPositionsList.clear()
		val startBasePositionsList: ArrayList<Float> = ArrayList()
		for (i in 0 until gradientColorsList.size) {
			startBasePositionsList.add(0.0f)
		}
		gradientPositionsList.add(startBasePositionsList.toFloatArray())

		typedArray.getString(R.styleable.GradientTextView_startColorsPositions)?.let {
			gradientPositionsList.clear()
			val startPositionsList: ArrayList<Float> = ArrayList()
			for (position in it.split(",")) {
				startPositionsList.add(position.trim().toFloat())
			}
			gradientPositionsList.add(startPositionsList.toFloatArray())
		}

		typedArray.getString(R.styleable.GradientTextView_endColorsPositions)?.let{
			val endPositionsList: ArrayList<Float> = ArrayList()
			for (position in it.split(",")) {
				endPositionsList.add(position.toFloat())
			}
			gradientPositionsList.add(endPositionsList.toFloatArray())
		}

		when (typedArray.getInt(R.styleable.GradientTextView_gradientOrientation, 0)) {
			0 -> gradientOrientation = GradientOrientation.Horizontal
			1 -> gradientOrientation = GradientOrientation.Vertical
		}

		typedArray.recycle()

		val colorsAndPositionsCountDifference = gradientPositionsList.first().size - gradientColorsList.size
		when {
			colorsAndPositionsCountDifference > 0 -> {
				for (i in 0 until colorsAndPositionsCountDifference) {
					addGradientColor(DEFAULT_COLOR_GRADIENT_TEXTVIEW)
				}
			}
			colorsAndPositionsCountDifference < 0 -> {
				for (i in 0 until (-1*colorsAndPositionsCountDifference)) {
					removeLastGradientColor()
				}
			}
		}

		initView()
	}

	fun initView() {
		this.setTextColor(Color.parseColor(DEFAULT_COLOR_GRADIENT_TEXTVIEW))
		this.post {
			initGradientShader()
			if (gradientPositionsList.size >= 2) {
				when {
					animationOnStart -> {
						if (animationLoop) {
							gradientPositionsList.add(gradientPositionsList[0])
							initAnimationQueue()
							gradientAnimators.last().setNextAnimator(gradientAnimators.first())
						} else {
							initAnimationQueue()
						}
						startAnimation()
					}
					animationLoop -> {
						gradientPositionsList.add(gradientPositionsList[0])
						initAnimationQueue()
						gradientAnimators.last().setNextAnimator(gradientAnimators.first())
					}
				}
			}
		}
	}

	fun initGradientShader() {
        when (gradientOrientation) {
            GradientOrientation.Horizontal -> gradientDirection[0] = this.width.toFloat()
            GradientOrientation.Vertical -> gradientDirection[1] = this.height.toFloat()
        }
        val shader = LinearGradient(0f, 0f,
                gradientDirection[0], gradientDirection[1],
                gradientColorsList.toIntArray(),gradientPositionsList.first(),
                Shader.TileMode.CLAMP)
        this.paint.shader = shader
        this.invalidate()
	}

	fun initAnimationQueue() {
		gradientAnimators.clear()
		if (animationLoop) {
			gradientPositionsList.add(gradientPositionsList[0])
		}
		if (gradientPositionsList.size == 2) {
			val grAnimator = GradientTextViewAnimator(this, gradientPositionsList.first(),
					gradientPositionsList[1], gradientPositionsList.first(),
					animationStepDuration, gradientDirection, gradientColorsList)
			endAnimationCallback?.let {
				grAnimator.setEndCallback(it)
			}
			gradientAnimators.add(grAnimator)
			if (animationLoop) {
				gradientAnimators.last().setNextAnimator(gradientAnimators.first())
			}
		}
		if (gradientPositionsList.size >= 3) {
			for (i in 0 until gradientPositionsList.size - 1) {
				val grAnimator = GradientTextViewAnimator(this, gradientPositionsList[i],
						gradientPositionsList[i + 1],gradientPositionsList.first(),
						animationStepDuration, gradientDirection, gradientColorsList)
				gradientAnimators.add(grAnimator)
			}

			for (i in 0 until gradientAnimators.size - 1) {
				gradientAnimators[i].setNextAnimator(gradientAnimators[i + 1])
			}

			endAnimationCallback?.let{
				gradientAnimators[gradientAnimators.size - 1].setEndCallback(it)
			}
			if (animationLoop) {
				gradientAnimators.last().setNextAnimator(gradientAnimators.first())
			}
		}
	}

	fun startAnimation() {
		if (gradientAnimators.isNotEmpty()) {
			gradientAnimators.first().startAnimation()
		}
	}

	fun addGradientPosition(vararg positions : Float) {
		val positionsList = ArrayList<Float>()
		for (pos in positions) {
			positionsList.add(pos)
		}
		gradientPositionsList.add(positionsList.toFloatArray())
		Log.d(TAG, "Pose added $positionsList")
	}

	fun addGradientColor(inColor: String) {
		gradientColorsList.let {
			if (it.size < colorsCount) {
				it.add(Color.parseColor(inColor))
			} else {
				colorsCount++
				it.add(Color.parseColor(inColor))
			}
		}
	}

	fun removeLastGradientColor() {
		gradientColorsList.let {
			it.removeAt(it.lastIndex)
			colorsCount--
		}
	}

	fun setLoopAnimation(inValue : Boolean) {
		animationLoop = inValue
	}

	fun setAnimationOnStart(inValue : Boolean) {
		animationOnStart = inValue
	}

	fun setOrientation(inGradientOrientation: GradientOrientation) {
		gradientOrientation = inGradientOrientation
	}

	fun setAnimationStepDuration(n: Long) {
		animationStepDuration = n
	}

	fun setEndCallBack(endCallBack: EndAnimationCallback) {
		endAnimationCallback = endCallBack
	}
}