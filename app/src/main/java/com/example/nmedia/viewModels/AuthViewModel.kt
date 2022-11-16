package com.example.nmedia.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.nmedia.auth.AppAuth
import com.example.nmedia.auth.AuthState
import kotlinx.coroutines.Dispatchers

class AuthViewModel : ViewModel() {
    val data: LiveData<AuthState> = AppAuth.getInstance().authStateFlow
        .asLiveData(Dispatchers.Default)
    val authenticated:Boolean
    get()  = AppAuth.getInstance().isAuth()

}