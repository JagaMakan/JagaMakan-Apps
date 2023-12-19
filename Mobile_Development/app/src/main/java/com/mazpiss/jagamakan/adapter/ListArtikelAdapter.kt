package com.mazpiss.jagamakan.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mazpiss.jagamakan.R
import com.mazpiss.jagamakan.data.local.Artikel
import com.mazpiss.jagamakan.ui.detaillartikel.DetailActivity

class ListArtikelAdapter(private val listArtikel: List<Artikel>) :
    RecyclerView.Adapter<ListArtikelAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imgList)
        private val title: TextView = itemView.findViewById(R.id.tvAritikel)
        private val description: TextView = itemView.findViewById(R.id.tvNameList)

        fun bind(item: Artikel) {
            imageView.setImageResource(item.photo)
            title.text = item.name
            description.text = item.description

            itemView.setOnClickListener {
                val intentToDetails = Intent(itemView.context, DetailActivity::class.java)
                intentToDetails.putExtra(DetailActivity.KEY_ARTICLE, item)
                itemView.context.startActivity(intentToDetails)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_list, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listArtikel[position])
    }

    override fun getItemCount(): Int {
        return listArtikel.size
    }
}
