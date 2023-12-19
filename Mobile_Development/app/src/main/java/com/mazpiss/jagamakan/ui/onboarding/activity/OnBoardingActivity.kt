package com.mazpiss.jagamakan.ui.onboarding.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.mazpiss.jagamakan.R
import com.mazpiss.jagamakan.databinding.ActivityOnBoardingBinding
import com.mazpiss.jagamakan.ui.main.MainActivity
import com.mazpiss.jagamakan.ui.onboarding.Animation
import com.mazpiss.jagamakan.ui.onboarding.OnBoardingAdapter
import com.mazpiss.jagamakan.ui.welcome.WelcomeActivity

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager2
    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        mViewPager = binding.viewPager
        mViewPager.adapter = OnBoardingAdapter(this, this)
        TabLayoutMediator(binding.indicator, mViewPager) { _, _ -> }.attach()

        binding.skip.setOnClickListener {
            finishAndStartWelcomeActivity()
        }

        binding.next.setOnClickListener {
            if (getItem() == mViewPager.adapter?.itemCount?.minus(1)) {
                finishAndStartWelcomeActivity()
            } else {
                mViewPager.setCurrentItem(getItem() + 1, true)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun getItem() = mViewPager.currentItem

    private fun finishAndStartWelcomeActivity() {
        finish()
        startActivity(Intent(applicationContext, WelcomeActivity::class.java))
        Animation.animateFragment(this)
    }
}
