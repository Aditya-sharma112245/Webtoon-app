package com.example.animaeapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animaeapp.data.Webtoon
import com.example.animaeapp.data.WebtoonDatabase
import com.example.animaeapp.ui.WebtoonAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {
    private lateinit var webtoonRecyclerView: RecyclerView
    private lateinit var webtoonAdapter: WebtoonAdapter
    private lateinit var db: WebtoonDatabase

    private val webtoons = listOf(
        Webtoon(
            1,
            "Solo Leveling",
            "A weak hunter gains powers to become the strongest warrior.",
            "One of the best action fantasy manhwa and the most talked about adaptation in recent times is undoubtedly Solo Leveling. It is set in a world where humans have discovered supernatural skills, while our protagonist, Sung Jinwoo, is a nobody with his E-Rank hunting skills. Things take an interesting turn for him when he becomes the sole survivor of a dungeon raid. Awakened with strange new powers, Sung Jinwoo will level up from being the weakest Hunter and eventually become the most powerful entity in the universe.",
            "https://animemangatoon.com/wp-content/uploads/2024/06/Screenshot-2024-10-01-090334-750x375.webp"
        ),
        Webtoon(
            2,
            "Tower of God",
            "A boy climbs a mysterious tower to find his friend.",
            "Even if you are not a manhwa fan, you must have heard of Tower of God. This action fantasy manhwa became especially popular after its anime adaptation. Tower of God focuses on Twenty-Fifth Bam, the young protagonist of the manhwa, who is determined to climb a mysterious Tower to find his friend Rachel. It is to be noted that the titular tower has different floors, each with unique obstacles. His quest is not going to be an easy one, and whether or not he will be able to meet his friend remains to be seen. Tower of God Season 2 has also been recently released.",
            "https://animemangatoon.com/wp-content/uploads/2024/06/tower-of-god.webp"
        ),
        Webtoon(
            3,
            "Hardcore Leveling Warrior",
            "A top player loses everything and fights to regain it all.",
            "Gong Won-Ho is the top player of Lucid Adventure because he uses his alias, Hardcore Leveling Warrior, to stay on top. However, one day, the unimaginable happens – he gets defeated, and now he has to get back on top from the bottom. The most fascinating aspect of this action fantasy manhwa is how he climbed his way to the top in the first place.",
            "https://animemangatoon.com/wp-content/uploads/2024/06/hard-levelign-warrior-750x375.webp"
        ),
        Webtoon(
            4,
            "Noblesse",
            "A powerful vampire wakes up in the modern world after 800 years.",
            "After being in a slumber for over 800 years, Cadis Etrama Di Raizel, aka Rai, wakes up in an unfamiliar modern world. Fortunately, he meets his loyal servant Frankenstein, who is now the owner of a high school. After attending the school, Rai tries to live an ordinary life, concealing his true identity; however, that won’t happen for long. This supernatural action fantasy manhwa will keep you engaged with its beautiful illustration and unique narrative till the end.",
            "https://animemangatoon.com/wp-content/uploads/2024/06/noblesse-750x375.webp"
        ),
        Webtoon(
            5,
            "The God of High School",
            "A high school student enters a tournament with incredible stakes.",
            "The God of High School is one of the best action fantasy manhwa that has been adapted into an anime. Jin Mori, the protagonist of the story, takes part in a suspicious tournament, the prize of which is whatever the winner wants. As Mori confronts challenges, he begins to understand more about the three realms and his powers.",
            "https://animemangatoon.com/wp-content/uploads/2024/06/Screenshot-2024-10-01-000548-750x375.webp"
        ),
        Webtoon(
            6,
            "Second Life Ranker",
            "A man searches for his brother in an alternate dimension.",
            "Second Life Ranker tells the story of Yeon-Woo, who is searching for his twin brother. He stumbles upon hints that lead to the certainty of his brother’s demise in an alternate dimension. Yeon-Woo embarks on a journey of revenge, unravelling the mysteries of the Tower of the Sun God.",
            "https://animemangatoon.com/wp-content/uploads/2024/06/Second-life-ranker-750x375.webp"
        ),
        Webtoon(
            7,
            "Eleceed",
            "A young man with super speed learns to defend himself.",
            "Eleceed focuses on Jiwoo, a young man with super speed, who thinks he is a monster because of his power. One day, he finds an injured cat, which turns out to be a human named Kayden with supernatural powers. Kayden helps Jiwoo learn to defend himself against powerful opponents.",
            "https://animemangatoon.com/wp-content/uploads/2024/06/elecceed-750x375.webp"
        ),
        Webtoon(
            8,
            "The Advanced Player of the Tutorial Tower",
            "A man is trapped in a tower and must fight monsters.",
            "Several monsters invade Earth, leading to the appearance of the tutorial tower. People are teleported inside the tower and must fight to survive. Hyeonu Kim, trapped inside for 12 years, has to fight his way out while gaining incredible strength.",
            "https://animemangatoon.com/wp-content/uploads/2024/06/Advanced-Player-750x375.webp"
        ),
        Webtoon(
            9,
            "Leveling Up With The Gods",
            "A man travels back in time to prevent the destruction of the gods.",
            "Kim Yuwon witnesses the destruction of gods and humans by The Tower. On the brink of death, he is given another chance to travel back in time and climb the Tower with his newfound knowledge to change the course of history.",
            "https://animemangatoon.com/wp-content/uploads/2024/06/leveling-up-with-the-gods-750x375.webp"
        ),
        Webtoon(
            10,
            "Villain to Kill",
            "A powerful psyker is reborn as a villain after seeking revenge.",
            "Cassian Lee, a powerful psyker, fights to protect the weak. After losing his guardian and dying in the process of seeking revenge, he wakes up in the body of a villain. Now, he must decide whether to follow his villainous instincts or fight evil as before.",
            "https://animemangatoon.com/wp-content/uploads/2024/06/Villain-to-kill-750x375.webp"
        )
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = WebtoonDatabase.getDatabase(applicationContext)
        webtoonRecyclerView = findViewById(R.id.recycler_view)
        webtoonAdapter = WebtoonAdapter(webtoons) { webtoon -> onWebtoonClicked(webtoon) }
        webtoonRecyclerView.layoutManager = LinearLayoutManager(this)
        webtoonRecyclerView.adapter = webtoonAdapter

        // Insert sample data into the database in a coroutine
        lifecycleScope.launch {
            insertWebtoons()
        }
    }

    private suspend fun insertWebtoons() {
        withContext(Dispatchers.IO) { // Switch to IO dispatcher for database operations
            webtoons.forEach { webtoon ->
                db.webtoonDao().insert(webtoon)
            }
        }
    }

    private fun onWebtoonClicked(webtoon: Webtoon) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("webtoon", webtoon)
        startActivity(intent)
    }
}
