package com.greylabs.grc.screens.examples_view_bottom.example_xml


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.greylabs.grc.R

class FragmentGradientViewExOnlyXml : Fragment() {

    var orderNumber = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_ex_gradient_view_xml, container, false)
    }
}