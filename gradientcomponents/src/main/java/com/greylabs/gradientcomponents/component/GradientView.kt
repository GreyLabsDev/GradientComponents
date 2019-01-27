package com.greylabs.gradientcomponents.component

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.greylabs.gradientcomponents.DEFAULT_COLOR_GRADIENT_TEXTVIEW
import com.greylabs.gradientcomponents.DEFAULT_COLOR_GRADIENT_VIEW
import com.greylabs.gradientcomponents.animator.GradientViewAnimator
import com.greylabs.gradientcomponents.GradientOrientation
import com.greylabs.gradientcomponents.R
import com.greylabs.gradientcomponents.extensions.getPixelsFromDp
import kotlin.collections.ArrayList

interface EndAnimationCallback {
    fun startEndAnimation()
}

class GradientView : View {
    private val TAG = "GradientView"

    private var topLeftRadius = 0f
    private var topRightRadius = 0f
    private var bottomLeftRadius = 0f
    private var bottomRightRadius = 0f

    private var paintDrawable = PaintDrawable()
    private var cornerRadiusArray = floatArrayOf(0f,0f,0f,0f,0f,0f,0f,0f)
    private var endAnimationCallback: EndAnimationCallback? = null
    private var gradientColorsList: ArrayList<Int> = ArrayList()
    private var gradientPositionsList: ArrayList<FloatArray> = ArrayList()
    private var gradientAnimators: ArrayList<GradientViewAnimator> = ArrayList()
    private var colorsCount: Int = 5
    private var animationStepDuration = 2000L
    private var animationOnStart = false
    private var animationLoop = false
    private var gradientOrientation = GradientOrientation.Horizontal

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
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.GradientView)
        animationOnStart = typedArray.getBoolean(R.styleable.GradientView_animationOnStart, false)
        animationLoop = typedArray.getBoolean(R.styleable.GradientView_animationLoop, false)

        topLeftRadius = typedArray.getDimensionPixelSize(R.styleable.GradientView_topLeftCorner, 0).toFloat()
        topRightRadius = typedArray.getDimensionPixelSize(R.styleable.GradientView_topRightCorner, 0).toFloat()
        bottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.GradientView_bottomLeftCorner, 0).toFloat()
        bottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.GradientView_bottomRightCorner, 0).toFloat()

        val animationStepDuration = typedArray.getInt(R.styleable.GradientView_animationStepDuration, 2000).toLong()

        cornerRadiusArray = floatArrayOf(
            topLeftRadius,topLeftRadius,
            topRightRadius,topRightRadius,
            bottomRightRadius,bottomRightRadius,
            bottomLeftRadius,bottomLeftRadius
        )

        setAnimationStepDuration(animationStepDuration)

        when (typedArray.getInt(R.styleable.GradientView_gradientOrientation, 0)) {
            0 -> gradientOrientation = GradientOrientation.Horizontal
            1 -> gradientOrientation = GradientOrientation.Vertical
        }

        gradientColorsList.clear()
        gradientColorsList.add(Color.parseColor(DEFAULT_COLOR_GRADIENT_VIEW))
        gradientColorsList.add(Color.parseColor(DEFAULT_COLOR_GRADIENT_VIEW))
        gradientColorsList.add(Color.parseColor(DEFAULT_COLOR_GRADIENT_VIEW))

        Log.d("POSITIONS", " ${typedArray.getString(R.styleable.GradientView_startColorsPositions)}")
        Log.d("POSITIONS", " ${typedArray.getString(R.styleable.GradientView_endColorsPositions)}")

        typedArray.getString(R.styleable.GradientView_baseColors)?.let {
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

        typedArray.getString(R.styleable.GradientView_startColorsPositions)?.let {
            gradientPositionsList.clear()
            val startPositionsList: ArrayList<Float> = ArrayList()
            for (position in it.split(",")) {
                startPositionsList.add(position.trim().toFloat())
            }
            gradientPositionsList.add(startPositionsList.toFloatArray())
        }

        typedArray.getString(R.styleable.GradientView_endColorsPositions)?.let{
            val endPositionsList: ArrayList<Float> = ArrayList()
            for (position in it.split(",")) {
                endPositionsList.add(position.toFloat())
            }
            gradientPositionsList.add(endPositionsList.toFloatArray())
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
    
    fun initGradientShader() {
        Log.d(TAG, "gradientColorsList ${gradientColorsList.size}")
        val shapeDrawableShader = object : ShapeDrawable.ShaderFactory() {
            override fun resize(p0: Int, p1: Int): Shader {
                val direction = floatArrayOf(0f, 0f)
                when (gradientOrientation) {
                    GradientOrientation.Horizontal -> direction[0] = width.toFloat()
                    GradientOrientation.Vertical -> direction[1] = height.toFloat()
                }
                return LinearGradient(0f, 0f,
                        direction[0], direction[1],
                        gradientColorsList.toIntArray(), gradientPositionsList.first(),
                        Shader.TileMode.CLAMP)
            }
        }
        paintDrawable.shape = RoundRectShape(cornerRadiusArray, null, null)
        paintDrawable.shaderFactory = shapeDrawableShader
        this.background = paintDrawable
    }
    
    fun initAnimationQueue() {
        gradientAnimators.clear()
        if (animationLoop) {
            gradientPositionsList.add(gradientPositionsList[0])
        }
        if (gradientPositionsList.size == 2) {
            val grAnimator = GradientViewAnimator(paintDrawable, RoundRectShape(cornerRadiusArray, null, null),
                    gradientPositionsList[0], gradientPositionsList[1], animationStepDuration, gradientPositionsList[0])
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
                val grAnimator = GradientViewAnimator(paintDrawable, RoundRectShape(cornerRadiusArray, null, null),
                        gradientPositionsList[i], gradientPositionsList[i + 1], animationStepDuration, gradientPositionsList[0])
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

    fun setRoundCornersRadiuses(radiuses: FloatArray) {
        if (radiuses.size == 4) {
            val cornerRadiuses = mutableListOf<Float>()
            radiuses.forEach {cornerRadiusInDp ->
                context.getPixelsFromDp(cornerRadiusInDp).let {
                    cornerRadiuses.addAll(listOf(it, it))
                }
            }
            cornerRadiusArray = cornerRadiuses.toFloatArray()
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

    fun addGradientPosition(vararg positions : Float) {
        val positionsList = ArrayList<Float>()
        for (pos in positions) {
            positionsList.add(pos)
        }
        gradientPositionsList.add(positionsList.toFloatArray())
        Log.d(TAG, "Pose added $positionsList")
    }
}

