package com.mazpiss.jagamakan.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.mazpiss.jagamakan.R
import com.mazpiss.jagamakan.databinding.ActivityMainBinding
import com.mazpiss.jagamakan.ui.nav.article.ArticleFragment
import com.mazpiss.jagamakan.ui.nav.home.HomeFragment
import com.mazpiss.jagamakan.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_notification -> {
                    replaceFragment(ArticleFragment())
                    title = "Notification"
                }

                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    title = "Home"
                }

            }
            true
        }

        // default bottom tab selected
        replaceFragment(HomeFragment())
        title = "Home"
        bottomNavigationView.selectedItemId = R.id.nav_home


        val addFab = findViewById<FloatingActionButton>(R.id.addFab)
        addFab.setOnClickListener {
            Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}

