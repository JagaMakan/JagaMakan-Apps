package com.mazpiss.jagamakan.ui.nav.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mazpiss.jagamakan.R
import com.mazpiss.jagamakan.adapter.ListArtikelAdapter
import com.mazpiss.jagamakan.data.local.Artikel
import com.mazpiss.jagamakan.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private val list: ArrayList<Artikel> by lazy { getListArticle() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)

        binding.rvArticle.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ListArtikelAdapter(list)
        }

        return binding.root
    }

    private fun getListArticle(): ArrayList<Artikel> {
        val id = resources.obtainTypedArray(R.array.img)
        val dataName = resources.getStringArray(R.array.data_judul)
        val dataDescription = resources.getStringArray(R.array.data_pengertian)

        return ArrayList<Artikel>().apply {
            for (i in dataName.indices) {
                add(Artikel(id.getResourceId(i, 0), dataName[i], dataDescription[i]))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
