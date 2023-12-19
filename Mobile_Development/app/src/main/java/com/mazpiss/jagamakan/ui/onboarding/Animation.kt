package com.mazpiss.jagamakan.ui.onboarding

import android.app.Activity
import android.content.Context
import com.mazpiss.jagamakan.R

object Animation {
    fun animateFragment(context: Context) {
        (context as Activity).overridePendingTransition(
            R.anim.enter,
            R.anim.exit
        )
    }
}