package com.fas.dev.latihan.advancedtesting.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.fas.dev.latihan.advancedtesting.data.NewsRepository
import com.fas.dev.latihan.advancedtesting.data.ResultStatus
import com.fas.dev.latihan.advancedtesting.data.local.entity.NewsEntity
import com.fas.dev.latihan.advancedtesting.utils.DataDummy
import com.fas.dev.latihan.advancedtesting.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NewsViewModelTest {

    @get:Rule
    val instanceExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var newsRepository: NewsRepository
    private lateinit var newsViewModel: NewsViewModel
    private val dummyNews = DataDummy.generateDummyNewsEntity()

    @Before
    fun setUp() {
        newsViewModel = NewsViewModel(newsRepository)
    }

    @Test
    fun `when get headline shouldn't null & return success`() {
        val expectedNews =
            MutableLiveData<ResultStatus<List<NewsEntity>>>() //Result diambil dari package data yang sudah disiapkan
        expectedNews.value = ResultStatus.Success(dummyNews)
        `when`(newsRepository.getHeadlineNews()).thenReturn(expectedNews)
        val actualNews = newsViewModel.getHeadlineNews().getOrAwaitValue()
        Assert.assertNotNull(actualNews)
        Assert.assertTrue(actualNews is ResultStatus.Success)
        Assert.assertEquals(dummyNews.size, (actualNews as ResultStatus.Success).data.size)

    }

    @Test
    fun `when network error should return error`() {
        val headlineNews = MutableLiveData<ResultStatus<List<NewsEntity>>>()
        headlineNews.value = ResultStatus.Error("Error")
        `when`(newsRepository.getHeadlineNews()).thenReturn(headlineNews)
        val actualNews = newsViewModel.getHeadlineNews().getOrAwaitValue()
        Mockito.verify(newsRepository).getHeadlineNews()
        Assert.assertNotNull(actualNews)
        Assert.assertTrue(actualNews is ResultStatus.Error)
    }

    @Test
    fun getBookmarkTest() {
        val expectedBookmarkNews = MutableLiveData<List<NewsEntity>>()
        expectedBookmarkNews.value = dummyNews
        `when`(newsRepository.getBookmarkedNews()).thenReturn(expectedBookmarkNews)
        val actualBookmark = newsViewModel.getBookmarkedNews().getOrAwaitValue()
        Assert.assertNotNull(actualBookmark)
        Assert.assertEquals(dummyNews.size, actualBookmark.size)

    }
}