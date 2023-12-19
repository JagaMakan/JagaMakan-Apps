package com.mazpiss.jagamakan.ui.onboarding

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mazpiss.jagamakan.R
import com.mazpiss.jagamakan.ui.onboarding.fragment.OnBoardingFragment

class OnBoardingAdapter (
    fragmentActivity: FragmentActivity,
    private val context: Context
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnBoardingFragment.newInstance(
                context.resources.getString(R.string.onboarding_tittle_deteksi),
                context.resources.getString(R.string.onboarding_sub_deteksi),
                R.drawable.kalorionborading
            )
            1 -> OnBoardingFragment.newInstance(
                context.resources.getString(R.string.onboarding_tittle_kalkulator),
                context.resources.getString(R.string.onboarding_sub_kalkulator),
                R.drawable.bmr
            )
            else -> OnBoardingFragment.newInstance(
                context.resources.getString(R.string.onboarding_tittle_artikel),
                context.resources.getString(R.string.onboarding_sub_artikel),
                R.drawable.aricleonboarding
            )
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}