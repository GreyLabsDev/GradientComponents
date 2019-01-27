package com.greylabs.grc.screens.backdrop_fragments_controller

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

import com.greylabs.grc.R
import com.greylabs.grc.screens.examples_view_bottom.example_code.FragmentGradientViewExOnlyProgram
import com.greylabs.grc.screens.examples_view_bottom.example_xml.FragmentGradientViewExOnlyXml
import com.greylabs.grc.screens.examples_text_bottom.FragmentExamplesTextBottom
import com.greylabs.grc.screens.examples_text_bottom.example_xml.FragmentGradientTextExOnlyXml
import com.greylabs.grc.screens.examples_text_bottom.example_code.FragmentGradientTextExOnlyProgram
import com.greylabs.grc.screens.examples_view_bottom.FragmentExamplesViewBottom
import com.greylabs.grc.screens.examples_view_bottom.welcome.FragmentWelcome

sealed class FragmentTypeOrdered(var isBottom: Boolean = false, var orderdNumber: Int = 0, var name: String = "") {
    //Bottom backdrop parts
    class BottomExamplesView : FragmentTypeOrdered(true, 0, "BottomExamplesView")
    class BottomExamplesText : FragmentTypeOrdered(true, 1, "BottomExamplesText")

    //Top backdrop pars of GradientView examples screen
    class TopWelcomeScreen : FragmentTypeOrdered(false, 0, "TopWelcomeScreen")
    class TopGradientViewOnlyXml : FragmentTypeOrdered(false, 1, "TopGradientViewOnlyXml")
    class TopGradientViewOnlyProgram : FragmentTypeOrdered(false, 2, "TopGradientViewOnlyProgram")

    //Top backdrop pars of GradientTextView examples screen
    class TopGradientTextViewOnlyXml : FragmentTypeOrdered(false, 0, "TopGradientTextViewOnlyXml")
    class TopGradientTextViewOnlyProgram : FragmentTypeOrdered(false, 1, "TopGradientTextViewOnlyProgram")
}

enum class FragmentContainerType { TopFragment, BottomFragment }

class BackdropFragmentsController {

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null
        private var fragmentManager: FragmentManager? = null

        private var bottomContainer: Int? = null
        private var topContainer: Int? = null

        private var isBottomFragmentSwitchedToView: Boolean = false

        var backdropStatusListener: BackdropStatusListener? = null

        var currentBackdropBottomFragment: BackdropFragment? = null

        var currentBackdropBottomFragmentTypeSealed: FragmentTypeOrdered? = null
        var currentBackdropTopFragmentTypeSealed: FragmentTypeOrdered? = null


        fun init(context: Context, fragmentManager: FragmentManager, bottomContainer: Int? = null, topContainer: Int? = null) {
            this.context = context
            this.fragmentManager = fragmentManager
            bottomContainer?.let {
                this.bottomContainer = it
            }
            topContainer?.let {
                this.topContainer = it
            }
        }

        fun setBackdropStatus(status: Boolean) {
            backdropStatusListener?.onBackdropStatusChanged(status)
        }

        fun setBottomContainer(viewId: Int) {
            bottomContainer = viewId
        }

        fun setTopContainer(viewId: Int) {
            topContainer = viewId
        }

        fun showFragmentWithOrdering(type: FragmentTypeOrdered, fragmentContainerType: FragmentContainerType): Boolean {
            var isFragmentAlreadyOnScreen = false
            var previousFragmentOrderNumber: Int? = -1

            when (fragmentContainerType) {
                FragmentContainerType.TopFragment -> {
                    currentBackdropTopFragmentTypeSealed?.let {topFragmentType ->
                        if (topFragmentType.name == type.name) {
                            isFragmentAlreadyOnScreen = true
                        } else {
                            previousFragmentOrderNumber = topFragmentType.orderdNumber
                            currentBackdropTopFragmentTypeSealed = type
                        }
                    } ?: run {
                        previousFragmentOrderNumber = currentBackdropTopFragmentTypeSealed?.orderdNumber
                        currentBackdropTopFragmentTypeSealed = type
                    }
                }
                FragmentContainerType.BottomFragment -> {
                    currentBackdropBottomFragmentTypeSealed?.let { bottomFragmentType ->
                        if (bottomFragmentType.name == type.name) {
                            isFragmentAlreadyOnScreen = true
                        } else {
                            previousFragmentOrderNumber = bottomFragmentType.orderdNumber
                            currentBackdropBottomFragmentTypeSealed = type
                            currentBackdropTopFragmentTypeSealed = null
                        }
                    } ?: run {
                        previousFragmentOrderNumber = currentBackdropBottomFragmentTypeSealed?.orderdNumber
                        currentBackdropBottomFragmentTypeSealed = type
                    }
                    isBottomFragmentSwitchedToView = type is FragmentTypeOrdered.BottomExamplesView
                }
            }

            if (isFragmentAlreadyOnScreen.not()) {
                val fragmentToAdd: Fragment? = when (type) {
                    is FragmentTypeOrdered.TopWelcomeScreen -> {
                        FragmentWelcome().apply {
                            orderNumber = type.orderdNumber
                        }
                    }
                    is FragmentTypeOrdered.TopGradientViewOnlyXml -> {
                        FragmentGradientViewExOnlyXml().apply {
                            orderNumber = type.orderdNumber
                        }
                    }
                    is FragmentTypeOrdered.TopGradientViewOnlyProgram -> {
                        FragmentGradientViewExOnlyProgram().apply {
                            orderNumber = type.orderdNumber
                        }
                    }
                    is FragmentTypeOrdered.TopGradientTextViewOnlyXml-> {
                        FragmentGradientTextExOnlyXml().apply {
                            orderNumber = type.orderdNumber
                        }
                    }
                    is FragmentTypeOrdered.TopGradientTextViewOnlyProgram-> {
                        FragmentGradientTextExOnlyProgram().apply {
                            orderNumber = type.orderdNumber
                        }
                    }
                    is FragmentTypeOrdered.BottomExamplesView -> {
                        FragmentExamplesViewBottom().apply {
                            orderNumber = type.orderdNumber
                        }
                    }
                    is FragmentTypeOrdered.BottomExamplesText -> {
                        FragmentExamplesTextBottom().apply {
                            orderNumber = type.orderdNumber
                        }
                    }
                }

                if (fragmentToAdd is BackdropFragment) {
                    currentBackdropBottomFragment = fragmentToAdd
                }

                val fragmentContainer = when (fragmentContainerType) {
                    FragmentContainerType.TopFragment -> topContainer
                    FragmentContainerType.BottomFragment -> bottomContainer
                }

                fragmentContainer?.let {container ->
                    fragmentToAdd?.let { fragment ->
                        fragmentManager?.beginTransaction()?.apply {
                            previousFragmentOrderNumber?.let {previousOrderNumber ->
                                when {
                                    previousOrderNumber <= type.orderdNumber -> setCustomAnimations(R.animator.slide_to_left, R.animator.slide_from_right)
                                    else -> setCustomAnimations(R.animator.slide_from_left, R.animator.slide_to_right)
                                }
                            } ?: run {
                                if (isBottomFragmentSwitchedToView) {
                                    setCustomAnimations(R.animator.slide_from_left, R.animator.slide_to_right)
                                } else {
                                    setCustomAnimations(R.animator.slide_to_left, R.animator.slide_from_right)
                                }
                            }
                            replace(container, fragment)
                            commit()
                        }
                    }
                }
            }
            return isFragmentAlreadyOnScreen
        }
    }
}