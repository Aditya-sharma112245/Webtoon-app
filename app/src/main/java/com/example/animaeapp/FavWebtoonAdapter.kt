
package com.example.animaeapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.animaeapp.R
import com.example.animaeapp.data.Webtoon

class FavWebtoonAdapter(
    private var webtoons: List<Webtoon>,
    private val itemClick: (Webtoon) -> Unit
) : RecyclerView.Adapter<FavWebtoonAdapter.FavWebtoonViewHolder>() {

    inner class FavWebtoonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.webtoon_title)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.webtoon_short_description)
        private val imageView: ImageView = itemView.findViewById(R.id.webtoon_image)

        fun bind(webtoon: Webtoon) {
            titleTextView.text = webtoon.title
            descriptionTextView.text = webtoon.description
            Glide.with(itemView.context).load(webtoon.imageUrl).into(imageView)

            itemView.setOnClickListener { itemClick(webtoon) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavWebtoonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.webtoon_item, parent, false)
        return FavWebtoonViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavWebtoonViewHolder, position: Int) {
        holder.bind(webtoons[position])
    }

    override fun getItemCount(): Int {
        return webtoons.size
    }

    fun submitList(newWebtoons: List<Webtoon>) {
        webtoons = newWebtoons
        notifyDataSetChanged()
    }
}
