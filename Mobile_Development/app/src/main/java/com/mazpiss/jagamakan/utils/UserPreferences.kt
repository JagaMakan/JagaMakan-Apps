package com.mazpiss.jagamakan.utils

import android.content.Context
import com.mazpiss.jagamakan.data.local.Calculator

internal class UserPreferences(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val GENDER = "gender"
        private const val WEIGHT = "weight"
        private const val HEIGHT = "height"
        private const val AGE = "age"
        private const val ACTIVITY = "activity"
        private const val CALORY = "calory"
        private const val KEY_BMR = "key_bmr"
        private const val KEY_ACTIVITY = "key_activity"
        private const val KEY_USER = "key_user"
        private const val KEY_GLOBAL_BMR = "key_global_bmr"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: Calculator) {
        val editor = preferences.edit()
        editor.putBoolean(GENDER, value.gender)
        editor.putInt(WEIGHT, value.weight)
        editor.putInt(HEIGHT, value.height)
        editor.putInt(AGE, value.age)
        editor.putBoolean(ACTIVITY, value.activity)
        editor.putString(CALORY,value.calory)
        editor.apply()
    }

    fun getUser(): Calculator {
        val model = Calculator()
        model.gender = preferences.getBoolean(GENDER, false)
        model.weight = preferences.getInt(WEIGHT, 0)
        model.height = preferences.getInt(HEIGHT, 0)
        model.age = preferences.getInt(AGE, 0)
        model.activity = preferences.getBoolean(ACTIVITY, false)
        model.calory = preferences.getString(CALORY,"")

        return model
    }

    fun setBMR(bmr: Double) {
        preferences.edit().putFloat(KEY_BMR, bmr.toFloat()).apply()
    }

    fun setActivity(activity: Boolean) {
        preferences.edit().putBoolean(KEY_ACTIVITY, activity).apply()
    }
    fun setGlobalBMR(globalBMR: Double) {
        preferences.edit().putFloat(KEY_GLOBAL_BMR, globalBMR.toFloat()).apply()
    }

    fun getGlobalBMR(): Double {
        return preferences.getFloat(KEY_GLOBAL_BMR, 0.0f).toDouble()
    }
    fun clearUser() {
        preferences.edit()
            .remove(KEY_USER)
            .remove(WEIGHT)
            .remove(HEIGHT)
            .remove(AGE)
            .remove(CALORY)
            .apply()
    }


    fun clearGlobalBMR() {
        preferences.edit().remove(KEY_GLOBAL_BMR).apply()
    }

    fun clearActivity() {
        preferences.edit().remove(KEY_ACTIVITY).apply()
    }

}
