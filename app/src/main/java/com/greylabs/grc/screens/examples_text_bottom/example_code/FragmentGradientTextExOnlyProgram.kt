package com.greylabs.grc.screens.examples_text_bottom.example_code

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SeekBar
import android.widget.TextView
import com.greylabs.grc.R
import com.greylabs.grc.screens.examples_view_bottom.example_code.adapter.StyledTextArrayAdapter
import com.greylabs.gradientcomponents.GradientOrientation
import com.greylabs.gradientcomponents.component.GradientTextView
import kotlinx.android.synthetic.main.fragment_top_ex_gradient_text_program.*

class FragmentGradientTextExOnlyProgram : Fragment() {

    var orderNumber = 0

    var firstColorPos = 0f
    var secondColorPos = 1f
    var isAnimationEnabled = false
    var isAnimationLoop = false
    var gradientOrientation = GradientOrientation.Vertical
    var gradientTextContent: String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_ex_gradient_text_program, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gradientTextContent = context?.getString(R.string.ex_gradient_text_code_title)

        initViews()
        initListeners()
    }

    private fun initViews() {
        val gradientOrientationsAdapner = StyledTextArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item,
            arrayOf("Vertical orientation", "Horizontal orientation"), Typeface.MONOSPACE
        )
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
        edit_user_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(textEditable: Editable?) {
                textEditable.toString().let { userText ->
                    gradientTextContent = if (userText != "") userText
                        else context?.getString(R.string.ex_gradient_text_code_title)
                    setupGradientText()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        checkbox_loop_animation.setOnCheckedChangeListener { checkBox, isCheckedChange ->
            isAnimationLoop = checkBox.isChecked
        }

        checkbox_enable_animation.setOnCheckedChangeListener { checkBox, isCheckedChange ->
            isAnimationEnabled = checkBox.isChecked
        }

        seekbar_first_color_pos.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                firstColorPos = progress / 100f
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        seekbar_second_color_pos.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                secondColorPos = progress / 100f
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        button_create_gradient_text.setOnClickListener {
            setupGradientText()
        }
    }

    fun setupGradientText() {
        TransitionManager.beginDelayedTransition(root)
        gradient_text_container.visibility = View.INVISIBLE

        gradient_text_container.removeAllViews()

        val gradientText = GradientTextView(requireContext())

        gradientText.apply {
            setOrientation(gradientOrientation)

            addGradientColor("#FFFFFF")
            addGradientColor("#91276C")

            addGradientPosition(firstColorPos, secondColorPos)
            addGradientPosition(0.9f, 1f)

            if (isAnimationEnabled) {
                setAnimationOnStart(isAnimationEnabled)
                setLoopAnimation(isAnimationLoop)
                setAnimationStepDuration(900L)
            }

            text = gradientTextContent
            typeface = Typeface.MONOSPACE
            textSize = 20f
            layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            gravity = Gravity.CENTER_HORIZONTAL

            initView()
            gradient_text_container.addView(gradientText, 0)
            TransitionManager.beginDelayedTransition(root)
            gradient_text_container.visibility = View.VISIBLE
        }
    }
}