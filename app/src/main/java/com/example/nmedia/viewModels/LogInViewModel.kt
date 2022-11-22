package com.example.nmedia.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nmedia.ERROR_SIGN_IN
import com.example.nmedia.ERROR_UNKNOWN
import com.example.nmedia.auth.AppAuth
import com.example.nmedia.auth.AuthState
import com.example.nmedia.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LogInViewModel @Inject constructor(
    private val repository: PostRepository,
    private val auth: AppAuth
) : ViewModel() {
    var error: String? = null
        set(value) {
            field = value
            errorLiveData.value = value
        }
    val errorLiveData = MutableLiveData(error)


    private var state = false
        set(value) {
            field = value
            authState.value = field
        }
    val authState = MutableLiveData(state)

    fun signIn(login: String, pass: String) {
        viewModelScope.launch {
            try {
                val authState = repository.updateUser(login, pass)
                if (authState == AuthState()) {
                    error = ERROR_SIGN_IN
                } else {
                    auth.setAuth(authState.id, authState.token!!)
                    state = true
                }
            } catch (e: Exception) {
                error = ERROR_UNKNOWN
            }
        }
    }

    fun register(login: String, pass: String, name: String) {
        viewModelScope.launch {
            try {
                val authState = repository.registerUser(login, pass, name)
                auth.setAuth(authState.id, authState.token!!)
                state = true
            } catch (e: Exception) {
                error = ERROR_UNKNOWN
            }

        }

    }
}