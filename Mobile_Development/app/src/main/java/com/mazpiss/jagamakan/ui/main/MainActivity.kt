package com.mazpiss.jagamakan.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mazpiss.jagamakan.R
import com.mazpiss.jagamakan.databinding.ActivityMainBinding
import com.mazpiss.jagamakan.ui.camera.DetectActivity
import com.mazpiss.jagamakan.ui.nav.article.ArticleFragment
import com.mazpiss.jagamakan.ui.nav.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigationView()
        setupFab()

        replaceFragment(HomeFragment())
        title = "Home"
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_artikel -> {
                    replaceFragment(ArticleFragment())
                    title = "Artikel"
                }

                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    title = "Home"
                }
            }
            true
        }

        binding.bottomNavigationView.selectedItemId = R.id.nav_home
    }

    private fun setupFab() {
        binding.addFab.setOnClickListener {
            Intent(this@MainActivity, DetectActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}
