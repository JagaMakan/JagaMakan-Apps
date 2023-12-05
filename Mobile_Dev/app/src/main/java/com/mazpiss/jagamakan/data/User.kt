package com.mazpiss.jagamakan.data

data class User(
    val name: String,
    val email: String,
    val imagePath: String = ""
) {
    constructor(): this("", "", "")
}