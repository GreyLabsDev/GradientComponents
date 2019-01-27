package com.greylabs.grc.screens.examples_view_bottom.example_code.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class StyledTextArrayAdapter(context: Context,
                            id: Int,
                            items: Array<String>, val adapterItemsFont: Typeface) : ArrayAdapter<String>(context, id, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.typeface = adapterItemsFont
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.typeface = adapterItemsFont
        return view
    }
}