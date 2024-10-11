package com.example.animaeapp.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Webtoon::class], version = 2, exportSchema = false)
abstract class WebtoonDatabase : RoomDatabase() {
    abstract fun webtoonDao(): WebtoonDao

    companion object {
        @Volatile
        private var INSTANCE: WebtoonDatabase? = null

        fun getDatabase(context: Context): WebtoonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WebtoonDatabase::class.java,
                    "webtoon_database"
                ).addMigrations(MIGRATION_1_2) // Add migration here
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Migration logic from version 1 to version 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table with additional fields, including detailedDescription
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `webtoon_new` (
                        `id` INTEGER PRIMARY KEY NOT NULL,
                        `title` TEXT NOT NULL,
                        `imageUrl` TEXT NOT NULL,
                        `averageRating` REAL NOT NULL DEFAULT 0.0,
                        `ratingCount` INTEGER NOT NULL DEFAULT 0,
                        `isFavorite` INTEGER NOT NULL DEFAULT 0, 
                        `description` TEXT,
                        `detailedDescription` TEXT // Add this line for detailedDescription
                    )
                """.trimIndent())

                // Copy data from the old table to the new table
                database.execSQL("""
                    INSERT INTO webtoon_new (id, title, imageUrl, averageRating, ratingCount, description, detailedDescription)
                    SELECT id, title, imageUrl, averageRating, ratingCount, description, detailedDescription FROM webtoon
                """.trimIndent())

                // Drop the old table
                database.execSQL("DROP TABLE webtoon")

                // Rename the new table to the original table name
                database.execSQL("ALTER TABLE webtoon_new RENAME TO webtoon")
            }
        }
    }
}
