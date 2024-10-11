package com.example.animaeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "webtoon")
data class Webtoon(
    @PrimaryKey val id: Int, // Unique identifier for the webtoon
    val title: String, // Title of the webtoon
    val description: String, // Brief description of the webtoon
    val detailedDescription: String,
    val imageUrl: String, // URL to the webtoon's image
    val averageRating: Double = 0.0, // Average rating (0.0 to 5.0)
    val ratingCount: Int = 0, // Number of ratings received
    val isFavorite: Boolean = false // Indicates if the webtoon is marked as favorite
) : Parcelable {
    // Function to validate the rating
    fun isValidRating(rating: Double): Boolean {
        return rating in 0.0..5.0
    }

    // Function to update the average rating based on a new rating
    fun updateRating(newRating: Double): Webtoon {
        return if (isValidRating(newRating)) {
            val totalRating = averageRating * ratingCount + newRating
            val newCount = ratingCount + 1
            val newAverageRating = totalRating / newCount
            this.copy(averageRating = newAverageRating, ratingCount = newCount)
        } else {
            this // Return the original object if the rating is invalid
        }
    }
}
