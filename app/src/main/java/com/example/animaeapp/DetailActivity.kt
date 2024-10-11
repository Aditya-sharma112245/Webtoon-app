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
    private lateinit var favButton: Button // Button to navigate to Favorites Activity
    private lateinit var addToFavButton: Button // Button to add to favorites
    private lateinit var db: WebtoonDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        titleTextView = findViewById(R.id.title_text)
        descriptionTextView = findViewById(R.id.description_text)
        imageView = findViewById(R.id.webtoon_image)
        ratingBar = findViewById(R.id.ratingBar)
        favButton = findViewById(R.id.open_fav_button) // Initialize the Favorites button
        addToFavButton = findViewById(R.id.add_button_favorite) // Initialize the Add to Favorites button

        // Initialize the database
        db = WebtoonDatabase.getDatabase(applicationContext)

        // Get the Webtoon object from the intent
        val webtoon = intent.getParcelableExtra<Webtoon>("webtoon")

        webtoon?.let {
            titleTextView.text = it.title
            descriptionTextView.text = it.detailedDescription // Display detailed description here
            Glide.with(this).load(it.imageUrl).into(imageView)

            // Load and display the rating
            loadRating(it.id)

            // Set up a listener for rating changes
            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                // Save the rating to the database
                saveRating(it.id, rating.toInt())
            }
        }

        // Set up the button click listener for adding to favorites
        addToFavButton.setOnClickListener {
            webtoon?.let { webtoonToAdd ->
                addToFavorites(webtoonToAdd) // Call the function to add to favorites
            }
        }

        // Set up the Favorites button to navigate to the FavoritesActivity
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
                // Set the rating bar to the average rating
                ratingBar.rating = webtoon.averageRating.toFloat()
            } else {
                ratingBar.rating = 0f // Default to 0 if no rating found
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

                // Update the database with the new average rating and count
                withContext(Dispatchers.IO) {
                    db.webtoonDao().updateRating(webtoonId, averageRating, totalRatings)
                }
            }
        }
    }

    private fun addToFavorites(webtoon: Webtoon) {
        lifecycleScope.launch {
            val favoriteWebtoon = webtoon.copy(isFavorite = true) // Ensure isFavorite is true
            withContext(Dispatchers.IO) {
                db.webtoonDao().insert(favoriteWebtoon) // Insert the webtoon into the favorites
                Log.d("DetailActivity", "Added to favorites: $favoriteWebtoon") // Debugging log
            }
        }
    }
}
