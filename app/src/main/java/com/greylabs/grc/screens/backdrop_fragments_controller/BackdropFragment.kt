package com.greylabs.grc.screens.backdrop_fragments_controller

import android.support.v4.app.Fragment

interface BackdropStatusListener {
    fun onBackdropStatusChanged(isOpened: Boolean)
}

abstract class BackdropFragment : Fragment() {

    abstract fun moveBackdrop()

    protected fun setBackdropTopContainer(containerId: Int) {
        BackdropFragmentsController.setTopContainer(containerId)
    }


}