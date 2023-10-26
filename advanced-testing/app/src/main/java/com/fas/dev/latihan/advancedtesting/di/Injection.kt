package com.fas.dev.latihan.advancedtesting.di

import android.content.Context
import com.fas.dev.latihan.advancedtesting.data.NewsRepository
import com.fas.dev.latihan.advancedtesting.data.local.room.NewsDatabase
import com.fas.dev.latihan.advancedtesting.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        val database = NewsDatabase.getInstance(context)
        val dao = database.newsDao()
        return NewsRepository.getInstance(apiService, dao)
    }
}