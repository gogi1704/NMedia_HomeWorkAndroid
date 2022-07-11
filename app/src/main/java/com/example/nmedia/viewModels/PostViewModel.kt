package com.example.nmedia.viewModels

import androidx.lifecycle.ViewModel
import com.example.nmedia.repository.InMemoryPostRepositoryImpl

class PostViewModel:ViewModel() {
    private val repository = InMemoryPostRepositoryImpl()
    var data = repository.getData()

    fun like(id:Int) = repository.like(id)
    fun share(id:Int) = repository.share(id)


}

