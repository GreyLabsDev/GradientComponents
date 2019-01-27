package com.greylabs.gradientcomponents.extensions

import android.content.Context

fun Context.getPixelsFromDp(dpValue: Float): Float {
    return dpValue * this.resources.displayMetrics.density
}