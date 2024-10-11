package com.example.animaeapp

import android.os.Bundle
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

        // Observe the LiveData from your DAO for favorite webtoons
        db.webtoonDao().getFavoriteWebtoons().observe(this, Observer { favoriteWebtoons ->
            // Pass the favorite webtoons to the adapter if the list isn't null
            if (favoriteWebtoons != null) {
                webtoonAdapter.submitList(favoriteWebtoons)
            }
        })
    }
}
