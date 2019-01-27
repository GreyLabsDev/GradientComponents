package com.greylabs.grc

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.greylabs.grc.screens.backdrop_fragments_controller.*
import kotlinx.android.synthetic.main.activity_main.*

const val CONST_ANIMATION_DURATION = 250L

class MainActivity : AppCompatActivity(), BackdropStatusListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)

        BackdropFragmentsController.init(
            this@MainActivity, supportFragmentManager,
            backdrop_bottom_container.id
        )
        BackdropFragmentsController.backdropStatusListener = this@MainActivity

        initViews()
        initListeners()
        setupToolbar()
    }

    private fun setupToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBar.outlineProvider = null
        }
    }

    private fun initViews() {
        BackdropFragmentsController.showFragmentWithOrdering(
            FragmentTypeOrdered.BottomExamplesView(),
            FragmentContainerType.BottomFragment
        )

        button_text_stroke.scaleX = 0f
        button_text_stroke.scaleY = 0f
    }

    private fun initListeners() {
        button_view.setOnClickListener {
            button_view_stroke.animate().scaleX(1f).setDuration(CONST_ANIMATION_DURATION).start()
            button_view_stroke.animate().scaleY(1f).setDuration(CONST_ANIMATION_DURATION).start()
            button_text_stroke.animate().scaleX(0f).setDuration(CONST_ANIMATION_DURATION).start()
            button_text_stroke.animate().scaleY(0f).setDuration(CONST_ANIMATION_DURATION).start()

            if (BackdropFragmentsController.showFragmentWithOrdering(
                    FragmentTypeOrdered.BottomExamplesView(),
                    FragmentContainerType.BottomFragment
                )
            ) {
                moveBackdrop()
            }
        }
        button_text.setOnClickListener {
            button_view_stroke.animate().scaleX(0f).setDuration(CONST_ANIMATION_DURATION).start()
            button_view_stroke.animate().scaleY(0f).setDuration(CONST_ANIMATION_DURATION).start()
            button_text_stroke.animate().scaleX(1f).setDuration(CONST_ANIMATION_DURATION).start()
            button_text_stroke.animate().scaleY(1f).setDuration(CONST_ANIMATION_DURATION).start()

            if (BackdropFragmentsController.showFragmentWithOrdering(
                    FragmentTypeOrdered.BottomExamplesText(),
                    FragmentContainerType.BottomFragment
                )
            ) {
                moveBackdrop()
            }
        }
        image_button_menu.setOnClickListener {
            moveBackdrop()
        }
    }

    private fun moveBackdrop() {
        BackdropFragmentsController.currentBackdropBottomFragment?.moveBackdrop()
    }

    override fun onBackdropStatusChanged(isOpened: Boolean) {
        gradient_decor_panel.animate().rotation(if (isOpened) 90f else 0f).setDuration(CONST_ANIMATION_DURATION).start()
    }
}
