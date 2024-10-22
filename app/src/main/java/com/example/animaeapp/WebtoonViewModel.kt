package com.example.animaeapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animaeapp.data.Webtoon
import com.example.animaeapp.repository.WebtoonRepository
import kotlinx.coroutines.launch

class WebtoonViewModel(private val repository: WebtoonRepository) : ViewModel() {


    val allWebtoons: LiveData<List<Webtoon>> = repository.allWebtoons
    val favoriteWebtoons: LiveData<List<Webtoon>> = repository.favoriteWebtoons


    fun insert(webtoon: Webtoon) {
        viewModelScope.launch {
            repository.insert(webtoon)
        }
    }


    fun update(webtoon: Webtoon) {
        viewModelScope.launch {
            repository.update(webtoon)
        }
    }


    fun getWebtoonById(id: Int): LiveData<Webtoon?> {
        val result = MutableLiveData<Webtoon?>()
        viewModelScope.launch {
            result.value = repository.getWebtoonById(id)
        }
        return result
    }
}
