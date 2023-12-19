package com.mazpiss.jagamakan.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Artikel(
    val photo: Int,
    val description: String,
    val name: String
):Parcelable