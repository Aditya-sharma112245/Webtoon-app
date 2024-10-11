package com.example.animaeapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WebtoonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(webtoon: Webtoon)

    @Update
   fun update(webtoon: Webtoon)

    @Query("SELECT * FROM webtoon")
    fun getAllWebtoons(): LiveData<List<Webtoon>>

    @Query("SELECT * FROM webtoon WHERE id = :id")
     fun getWebtoonById(id: Int): Webtoon?

    @Query("SELECT averageRating FROM webtoon WHERE id = :id")
     fun getAverageRatingById(id: Int): Double?

    @Query("UPDATE webtoon SET averageRating = :averageRating, ratingCount = :ratingCount WHERE id = :id")
     fun updateRating(id: Int, averageRating: Double, ratingCount: Int)

    @Query("SELECT * FROM webtoon WHERE isFavorite = 1")
    fun getFavoriteWebtoons(): LiveData<List<Webtoon>>
}
