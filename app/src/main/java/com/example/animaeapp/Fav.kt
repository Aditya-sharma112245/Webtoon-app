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


        recyclerView = findViewById(R.id.recycler_view_favorites)


        webtoonAdapter = FavWebtoonAdapter(emptyList()) { webtoon ->
            onWebtoonClick(webtoon)
        }

        recyclerView.adapter = webtoonAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        loadFavorites()
    }


    private fun onWebtoonClick(webtoon: Webtoon) {

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
