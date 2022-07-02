package com.example.nmedia.viewModels

import androidx.lifecycle.ViewModel
import com.example.nmedia.repository.InMemoryPostRepositoryImpl

class PostViewModel:ViewModel() {
    private val repository = InMemoryPostRepositoryImpl()
    val data = repository.getData()

    fun like() = repository.like()
    fun share() = repository.share()


}

