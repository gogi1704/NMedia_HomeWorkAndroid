package com.example.nmedia.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.nmedia.auth.AppAuth
import com.example.nmedia.auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(private val appAuth: AppAuth) : ViewModel() {
    val data: LiveData<AuthState> =appAuth.authStateFlow
        .asLiveData(Dispatchers.Default)
    val authenticated:Boolean
    get()  = appAuth.isAuth()

}