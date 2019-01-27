package com.greylabs.grc.screens.examples_view_bottom.example_code

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SeekBar
import android.widget.TextView
import com.greylabs.grc.R
import com.greylabs.grc.screens.examples_view_bottom.example_code.adapter.StyledTextArrayAdapter

import com.greylabs.gradientcomponents.GradientOrientation
import com.greylabs.gradientcomponents.component.GradientView
import kotlinx.android.synthetic.main.fragment_top_ex_gradient_view_program.*

class FragmentGradientViewExOnlyProgram : Fragment() {

    var orderNumber = 0

    var firstColorPos = 0f
    var secondColorPos = 1f
    var isAnimationEnabled = false
    var isAnimationLoop = false
    var gradientOrientation = GradientOrientation.Vertical

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_ex_gradient_view_program, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
    }

    private fun initViews() {
        val gradientOrientationsAdapner = StyledTextArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,
            arrayOf("Vertical orientation", "Horizontal orientation"), Typeface.MONOSPACE)
        gradientOrientationsAdapner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_gradient_orientation.adapter = gradientOrientationsAdapner
        spinner_gradient_orientation.prompt = "Gradient orientations"

        spinner_gradient_orientation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, selectedItemView: View, p2: Int, p3: Long) {
                when ((selectedItemView as TextView).text) {
                    "Vertical orientation" -> gradientOrientation = GradientOrientation.Vertical
                    "Horizontal orientation" -> gradientOrientation = GradientOrientation.Horizontal
                }
            }
        }
    }

    private fun initListeners() {

        checkbox_enable_animation.setOnCheckedChangeListener { checkBox, isCheckedChange ->
            isAnimationEnabled = checkBox.isChecked
        }
        checkbox_loop_animation.setOnCheckedChangeListener { checkBox, isCheckedChange ->
            isAnimationLoop = checkBox.isChecked
        }

        seekbar_first_color_pos.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                firstColorPos = progress/100f
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        seekbar_second_color_pos.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                secondColorPos = progress/100f
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        button_create_gradient_view.setOnClickListener {

            TransitionManager.beginDelayedTransition(root)
            gradient_view_container.visibility = View.INVISIBLE

            val gradientView = GradientView(requireContext())

            gradientView.apply {
                setOrientation(gradientOrientation)
                setRoundCornersRadiuses(floatArrayOf(16f,16f,16f,16f))
                addGradientColor("#91276C")
                addGradientColor("#0F3368")
                addGradientPosition(firstColorPos, secondColorPos)
                addGradientPosition(0.8f, 0.9f)

                initGradientShader()
                if (isAnimationEnabled) {
                    setAnimationOnStart(isAnimationEnabled)
                    setLoopAnimation(isAnimationLoop)
                    setAnimationStepDuration(1600L)
                    initAnimationQueue()
                }

                gradient_view_container.addView(gradientView, 0)
                TransitionManager.beginDelayedTransition(root)
                gradient_view_container.visibility = View.VISIBLE
                startAnimation()
            }
        }
    }
}