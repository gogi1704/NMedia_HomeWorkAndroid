package com.example.nmedia.auth

import android.content.Context
import android.content.SharedPreferences
import com.example.nmedia.repository.PostApiServiceHolder
import com.example.nmedia.service.PushToken
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AppAuth private constructor(context: Context) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val idKey = "id"
    private val tokenKey = "token"


    private val _authStateFlow: MutableStateFlow<AuthState>

    init {
        val id = prefs.getLong(idKey, 0)
        val token = prefs.getString(tokenKey, null)

        if (id == 0L || token == null) {
            _authStateFlow = MutableStateFlow(AuthState())
            with(prefs.edit()) {
                clear()
                apply()
            }

        } else {
            _authStateFlow = MutableStateFlow(AuthState(id, token))
        }
        sendPushToken()

    }

    val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()


    @Synchronized
    fun setAuth(id: Long, token: String) {
        _authStateFlow.value = AuthState(id, token)
        with(prefs.edit()) {
            putLong(idKey, id)
            putString(tokenKey, token)
            apply()
        }
        sendPushToken()
    }

    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = AuthState()
        with(prefs.edit()) {
            clear()
            commit()
        }
        sendPushToken()
    }

    fun isAuth(): Boolean {
        return _authStateFlow.value != AuthState()
    }

    fun sendPushToken(token: String? = null) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val pushToken = PushToken(token ?: Firebase.messaging.token.await())
                PostApiServiceHolder.service.sendPushToken(pushToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

    }

    companion object {
        @Volatile
        private var instance: AppAuth? = null

        fun getInstance(): AppAuth = synchronized(this) {
            instance ?: throw IllegalStateException("App auth is not initialized")
        }

        fun initApp(context: Context): AppAuth = instance ?: synchronized(this) {
            instance ?: buildAuth(context).also { instance = it }
        }

        private fun buildAuth(context: Context): AppAuth = AppAuth(context)
    }
}

data class AuthState(val id: Long = -1L, val token: String? = null)

const val OPEN_REGISTER_FRAGMENT_KEY = "OPEN_REGISTER_FRAGMENT"
const val OPEN_REGISTER_FRAGMENT_VALUE = "REGISTER"