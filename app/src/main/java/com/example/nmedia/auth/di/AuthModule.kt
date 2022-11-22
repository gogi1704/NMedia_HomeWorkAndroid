package com.example.nmedia.auth.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.example.nmedia.auth.AppAuth
import com.example.nmedia.repository.PostRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

//@InstallIn(SingletonComponent::class)
//@Module
//class AuthModule {
//
//
////
////    @Volatile
////    private var instance: AppAuth? = null
////
////    fun getInstance(): AppAuth = synchronized(this) {
////        instance ?: throw IllegalStateException("App auth is not initialized")
////    }
////
////    fun initApp(context: Context): AppAuth = instance ?: synchronized(this) {
////        instance ?: buildAuth(context).also { instance = it }
////    }
////
////    private fun buildAuth(context: Context): AppAuth = AppAuth(context)
//
//
//}