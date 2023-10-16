package com.dicoding.picodiploma.mycamera.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.mycamera.data.UploadRepository
import com.dicoding.picodiploma.mycamera.data.di.Injection
import com.dicoding.picodiploma.mycamera.data.view_model.MainViewModel

class ViewModelFactory private constructor(private val uploadRepository: UploadRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(uploadRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository())
        }.also { instance = it }
    }
}