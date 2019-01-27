package com.greylabs.grc.screens.examples_text_bottom

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import com.greylabs.grc.CONST_ANIMATION_DURATION
import com.greylabs.grc.R
import com.greylabs.grc.screens.backdrop_fragments_controller.*
import kotlinx.android.synthetic.main.fragment_bottom_text.*

class FragmentExamplesTextBottom : BackdropFragment() {

    var orderNumber = 0

    private var isBackdropOpened = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
    }

    private fun initViews() {
        BackdropFragmentsController.showFragmentWithOrdering(
            FragmentTypeOrdered.TopGradientTextViewOnlyXml(),
            FragmentContainerType.TopFragment
        )
        text_button_show_example_one.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_selected_text_button)
        moveBackdrop()
    }

    private fun initListeners() {
        backdrop_top_container_overlay.setOnClickListener {
            if (isBackdropOpened.not()) {
                moveBackdrop()
            }
        }
        text_button_show_example_one.setOnClickListener {
            BackdropFragmentsController.showFragmentWithOrdering(
                FragmentTypeOrdered.TopGradientTextViewOnlyXml(),
                FragmentContainerType.TopFragment
            )

            text_button_show_example_one.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_selected_text_button)
            text_button_show_example_two.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_ripple_shape)
            moveBackdrop()
        }
        text_button_show_example_two.setOnClickListener {
            BackdropFragmentsController.showFragmentWithOrdering(
                FragmentTypeOrdered.TopGradientTextViewOnlyProgram(),
                FragmentContainerType.TopFragment
            )

            text_button_show_example_two.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_selected_text_button)
            text_button_show_example_one.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_ripple_shape)
            moveBackdrop()
        }
    }

    override fun moveBackdrop() {
        backdrop_top_container_overlay.isClickable = isBackdropOpened

        backdrop_top_container_overlay.animate()
            .alpha(if (isBackdropOpened) 0.75f else 0f)
            .setDuration(CONST_ANIMATION_DURATION)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        ObjectAnimator.ofFloat(backdrop_top, "translationY", if (isBackdropOpened) 300f else 0f).apply {
            duration = CONST_ANIMATION_DURATION
            start()
        }
        BackdropFragmentsController.setBackdropStatus(isBackdropOpened)
        isBackdropOpened = !isBackdropOpened
    }

}