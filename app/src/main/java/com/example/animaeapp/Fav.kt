package com.example.animaeapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animaeapp.data.WebtoonDatabase
import com.example.animaeapp.data.Webtoon
import com.example.animaeapp.ui.FavWebtoonAdapter

class Fav : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var webtoonAdapter: FavWebtoonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)

        // Initialize the RecyclerView and Adapter
        recyclerView = findViewById(R.id.recycler_view_favorites)

        // Initialize adapter with click handling
        webtoonAdapter = FavWebtoonAdapter(emptyList()) { webtoon ->
            onWebtoonClick(webtoon)
        }

        recyclerView.adapter = webtoonAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load only favorite webtoons from the database
        loadFavorites()
    }

    // Method that will handle what happens when a webtoon is clicked
    private fun onWebtoonClick(webtoon: Webtoon) {
        // Define what happens when a favorite webtoon is clicked
        // For example, navigate to details screen or show a Toast
        // Example: Toast.makeText(this, "Clicked on ${webtoon.title}", Toast.LENGTH_SHORT).show()
    }

    private fun loadFavorites() {
        val db = WebtoonDatabase.getDatabase(applicationContext)

        Log.d("FavActivity", "Loading favorite webtoons from database")
        db.webtoonDao().getFavoriteWebtoons().observe(this, Observer { favoriteWebtoons ->
            Log.d("FavActivity", "Favorite webtoons loaded: $favoriteWebtoons") // Log the favorites retrieved
            if (favoriteWebtoons != null) {
                webtoonAdapter.submitList(favoriteWebtoons)
            } else {
                Log.d("FavActivity", "No favorite webtoons found") // Log if the list is null
            }
        })
    }

}
