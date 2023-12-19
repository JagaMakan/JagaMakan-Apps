package com.mazpiss.jagamakan.ui.detaillartikel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mazpiss.jagamakan.data.local.Artikel
import com.mazpiss.jagamakan.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataArticle = intent.getParcelableExtra<Artikel>(KEY_ARTICLE)

        if (dataArticle != null) {
            val img = binding.imgArtikel
            val judul = binding.tvJudul
            val desc = binding.tvDesc

            judul.text = dataArticle.description
            desc.text = dataArticle.name
            img.setImageResource(dataArticle.photo)
        }
    }
    companion object {
        const val KEY_ARTICLE = "article"
    }
}