package com.greylabs.grc.screens.examples_view_bottom.welcome

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.greylabs.grc.R
import kotlinx.android.synthetic.main.fragment_top_welcome.*

class FragmentWelcome : Fragment() {

    var orderNumber = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
    }

    private fun initViews() {
        val githubButtonAnimator = ViewAnimator(image_button_github)
        githubButtonAnimator.startAnimator()
    }

    private fun initListeners() {
        image_button_github.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse(getString(R.string.welcome_link_github))
            context?.startActivity(browserIntent)
        }
    }
}