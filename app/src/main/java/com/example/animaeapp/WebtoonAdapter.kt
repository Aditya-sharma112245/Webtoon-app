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
//import kotlinx.android.synthetic.main.webtoon_item.view.* // Keep this import for synthetic access if needed

class WebtoonAdapter(
    private val webtoons: List<Webtoon>,
    private val onClick: (Webtoon) -> Unit
) : RecyclerView.Adapter<WebtoonAdapter.WebtoonViewHolder>() {

    class WebtoonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(webtoon: Webtoon, onClick: (Webtoon) -> Unit) {
            // Access views using findViewById if you prefer not to use synthetic imports
            itemView.findViewById<TextView>(R.id.webtoon_title).text = webtoon.title
            itemView.findViewById<TextView>(R.id.webtoon_description).text = webtoon.description

            // Load the image using Glide
            Glide.with(itemView.context)
                .load(webtoon.imageUrl)
                .into(itemView.findViewById<ImageView>(R.id.webtoon_image))

            // Set the click listener
            itemView.setOnClickListener { onClick(webtoon) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebtoonViewHolder {
        // Inflate the layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_webtoon, parent, false)
        return WebtoonViewHolder(view)
    }

    override fun onBindViewHolder(holder: WebtoonViewHolder, position: Int) {
        holder.bind(webtoons[position], onClick)
    }

    override fun getItemCount(): Int = webtoons.size
}
