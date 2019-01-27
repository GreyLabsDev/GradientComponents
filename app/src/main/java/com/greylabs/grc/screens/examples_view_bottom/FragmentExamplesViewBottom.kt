package com.greylabs.grc.screens.examples_view_bottom

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
import kotlinx.android.synthetic.main.fragment_bottom_view.*

class FragmentExamplesViewBottom: BackdropFragment() {

    var orderNumber = 0

    private var isBackdropOpened = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackdropTopContainer(backdrop_top_container.id)

        initViews()
        initListeners()
        BackdropFragmentsController.setBackdropStatus(false)
    }

    private fun initViews() {
        BackdropFragmentsController.showFragmentWithOrdering(
            FragmentTypeOrdered.TopWelcomeScreen(),
            FragmentContainerType.TopFragment)
        button_show_welcome.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_selected_text_button)
    }

    private fun initListeners() {
        button_show_welcome.setOnClickListener {
            button_show_welcome.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_selected_text_button)
            button_show_example_one.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_ripple_shape)
            button_show_example_two.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_ripple_shape)

            BackdropFragmentsController.showFragmentWithOrdering(
                FragmentTypeOrdered.TopWelcomeScreen(),
                FragmentContainerType.TopFragment
            )
            moveBackdrop()
        }

        button_show_example_one.setOnClickListener {
            button_show_welcome.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_ripple_shape)
            button_show_example_one.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_selected_text_button)
            button_show_example_two.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_ripple_shape)

            BackdropFragmentsController.showFragmentWithOrdering(
                FragmentTypeOrdered.TopGradientViewOnlyXml(),
                FragmentContainerType.TopFragment
            )
            moveBackdrop()
        }

        button_show_example_two.setOnClickListener {
            button_show_welcome.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_ripple_shape)
            button_show_example_one.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_ripple_shape)
            button_show_example_two.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_selected_text_button)

            BackdropFragmentsController.showFragmentWithOrdering(
                FragmentTypeOrdered.TopGradientViewOnlyProgram(),
                FragmentContainerType.TopFragment
            )
            moveBackdrop()
        }

        backdrop_top_container_overlay.setOnClickListener {
            if (isBackdropOpened.not()) {
                moveBackdrop()
            }
        }
    }

    override fun moveBackdrop() {
        backdrop_top_container_overlay.isClickable = isBackdropOpened

        backdrop_top_container_overlay.animate()
            .alpha(if (isBackdropOpened) 0.75f else 0f)
            .setDuration(CONST_ANIMATION_DURATION)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        ObjectAnimator.ofFloat(backdrop_top, "translationY", if (isBackdropOpened) 432f else 0f).apply {
            duration = CONST_ANIMATION_DURATION
            start()
        }
        BackdropFragmentsController.setBackdropStatus(isBackdropOpened)
        isBackdropOpened = !isBackdropOpened
    }
}