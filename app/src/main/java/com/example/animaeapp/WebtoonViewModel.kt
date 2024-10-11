package com.example.animaeapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animaeapp.data.Webtoon
import com.example.animaeapp.repository.WebtoonRepository
import kotlinx.coroutines.launch

class WebtoonViewModel(private val repository: WebtoonRepository) : ViewModel() {

    // LiveData for all webtoons and favorite webtoons
    val allWebtoons: LiveData<List<Webtoon>> = repository.allWebtoons
    val favoriteWebtoons: LiveData<List<Webtoon>> = repository.favoriteWebtoons

    // Function to insert a webtoon
    fun insert(webtoon: Webtoon) {
        viewModelScope.launch {
            repository.insert(webtoon)
        }
    }

    // Function to update a webtoon
    fun update(webtoon: Webtoon) {
        viewModelScope.launch {
            repository.update(webtoon)
        }
    }

    // Function to get a webtoon by ID
    fun getWebtoonById(id: Int): LiveData<Webtoon?> {
        val result = MutableLiveData<Webtoon?>()
        viewModelScope.launch {
            result.value = repository.getWebtoonById(id)
        }
        return result
    }
}
