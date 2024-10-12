package com.example.animaeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "webtoon")
data class Webtoon(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val detailedDescription: String,
    val imageUrl: String,
    val averageRating: Double = 0.0,
    val ratingCount: Int = 0,
    val isFavorite: Boolean = false
) : Parcelable {

    fun isValidRating(rating: Double): Boolean {
        return rating in 0.0..5.0
    }


    fun updateRating(newRating: Double): Webtoon {
        return if (isValidRating(newRating)) {
            val totalRating = averageRating * ratingCount + newRating
            val newCount = ratingCount + 1
            val newAverageRating = totalRating / newCount
            this.copy(averageRating = newAverageRating, ratingCount = newCount)
        } else {
            this
        }
    }
}
