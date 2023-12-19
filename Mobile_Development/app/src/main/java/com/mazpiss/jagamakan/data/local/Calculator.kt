package com.mazpiss.jagamakan.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

    @Parcelize
    data class Calculator(
        var gender:Boolean = false,
        var weight:Int = 0,
        var height:Int = 0,
        var age:Int = 0,
        var activity:Boolean = false,
        var calory:String? = null,

        var globalBMR: Double = 0.0
    ) :Parcelable