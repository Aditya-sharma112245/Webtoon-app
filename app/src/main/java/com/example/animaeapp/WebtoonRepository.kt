package com.example.animaeapp.repository

import androidx.lifecycle.LiveData
import com.example.animaeapp.data.Webtoon
import com.example.animaeapp.data.WebtoonDao

class WebtoonRepository(private val webtoonDao: WebtoonDao) {

    val allWebtoons: LiveData<List<Webtoon>> = webtoonDao.getAllWebtoons()
    val favoriteWebtoons: LiveData<List<Webtoon>> = webtoonDao.getFavoriteWebtoons()

    suspend fun insert(webtoon: Webtoon) {
        webtoonDao.insert(webtoon)
    }

    suspend fun update(webtoon: Webtoon) {
        webtoonDao.update(webtoon)
    }

    suspend fun getWebtoonById(id: Int): Webtoon? {
        return webtoonDao.getWebtoonById(id)
    }

    suspend fun getAverageRatingById(id: Int): Double? {
        return webtoonDao.getAverageRatingById(id)
    }

    suspend fun updateRating(id: Int, averageRating: Double, ratingCount: Int) {
        webtoonDao.updateRating(id, averageRating, ratingCount)
    }
}
