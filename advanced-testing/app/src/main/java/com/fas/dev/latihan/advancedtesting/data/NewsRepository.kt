package com.fas.dev.latihan.advancedtesting.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.fas.dev.latihan.advancedtesting.BuildConfig
import com.fas.dev.latihan.advancedtesting.data.local.entity.NewsEntity
import com.fas.dev.latihan.advancedtesting.data.local.room.NewsDao
import com.fas.dev.latihan.advancedtesting.data.remote.retrofit.ApiService

class NewsRepository(
    private val apiService: ApiService,
    private val newsDao: NewsDao
) {
    fun getHeadlineNews(): LiveData<ResultStatus<List<NewsEntity>>> = liveData {
        emit(ResultStatus.Loading)
        try {
            val response = apiService.getNews(BuildConfig.API_KEY)
            val articles = response.articles
            val newsList = articles.map { article ->
                NewsEntity(
                    article.title,
                    article.publishedAt,
                    article.urlToImage,
                    article.url
                )
            }
            emit(ResultStatus.Success(newsList))
        } catch (e: Exception) {
            Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
            emit(ResultStatus.Error(e.message.toString()))
        }
    }

    fun getBookmarkedNews(): LiveData<List<NewsEntity>> {
        return newsDao.getBookmarkedNews()
    }

    suspend fun saveNews(news: NewsEntity) {
        newsDao.saveNews(news)
    }

    suspend fun deleteNews(title: String) {
        newsDao.deleteNews(title)
    }

    fun isNewsBookmarked(title: String): LiveData<Boolean> {
        return newsDao.isNewsBookmarked(title)
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null
        fun getInstance(
            apiService: ApiService,
            newsDao: NewsDao
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService, newsDao)
            }.also { instance = it }
    }
}