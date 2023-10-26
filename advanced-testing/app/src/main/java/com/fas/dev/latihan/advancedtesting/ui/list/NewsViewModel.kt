package com.fas.dev.latihan.advancedtesting.ui.list

import androidx.lifecycle.ViewModel
import com.fas.dev.latihan.advancedtesting.data.NewsRepository

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    fun getHeadlineNews() = newsRepository.getHeadlineNews()

    fun getBookmarkedNews() = newsRepository.getBookmarkedNews()
}