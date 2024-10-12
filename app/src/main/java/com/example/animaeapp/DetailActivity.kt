package com.example.animaeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.animaeapp.data.Webtoon
import com.example.animaeapp.data.WebtoonDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

class DetailActivity : AppCompatActivity() {
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var ratingBar: RatingBar
    private lateinit var favButton: Button
    private lateinit var addToFavButton: Button
    private lateinit var db: WebtoonDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        titleTextView = findViewById(R.id.title_text)
        descriptionTextView = findViewById(R.id.description_text)
        imageView = findViewById(R.id.webtoon_image)
        ratingBar = findViewById(R.id.ratingBar)
        favButton = findViewById(R.id.open_fav_button)
        addToFavButton = findViewById(R.id.add_button_favorite)


        db = WebtoonDatabase.getDatabase(applicationContext)


        val webtoon = intent.getParcelableExtra<Webtoon>("webtoon")

        webtoon?.let {
            titleTextView.text = it.title
            descriptionTextView.text = it.detailedDescription
            Glide.with(this).load(it.imageUrl).into(imageView)


            loadRating(it.id)


            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->

                saveRating(it.id, rating.toInt())
            }
        }


        addToFavButton.setOnClickListener {
            webtoon?.let { webtoonToAdd ->
                addToFavorites(webtoonToAdd)
            }
        }


        favButton.setOnClickListener {
            val intent = Intent(this, Fav::class.java)
            startActivity(intent)
        }
    }

    private fun loadRating(webtoonId: Int) {
        lifecycleScope.launch {
            val webtoon = withContext(Dispatchers.IO) {
                db.webtoonDao().getWebtoonById(webtoonId)
            }
            if (webtoon != null) {

                ratingBar.rating = webtoon.averageRating.toFloat()
            } else {
                ratingBar.rating = 0f
            }
        }
    }

    private fun saveRating(webtoonId: Int, newRating: Int) {
        lifecycleScope.launch {
            val webtoon = withContext(Dispatchers.IO) {
                db.webtoonDao().getWebtoonById(webtoonId)
            }
            webtoon?.let {
                // Calculate new average rating
                val totalRatings = it.ratingCount + 1
                val averageRating = (it.averageRating * it.ratingCount + newRating) / totalRatings


                val updatedWebtoon = it.copy(averageRating = averageRating, ratingCount = totalRatings)


                withContext(Dispatchers.IO) {
                    db.webtoonDao().update(updatedWebtoon)
                }
            }
        }
    }


    private fun addToFavorites(webtoon: Webtoon) {
        lifecycleScope.launch {
            val updatedWebtoon = webtoon.copy(isFavorite = true)
            Log.d("DetailActivity", "Adding to favorites: $updatedWebtoon")
            withContext(Dispatchers.IO) {
                db.webtoonDao().update(updatedWebtoon)
                val checkWebtoon = db.webtoonDao().getWebtoonById(webtoon.id)
                Log.d("DetailActivity", "Webtoon after addition: $checkWebtoon")
            }
        }
    }



}
