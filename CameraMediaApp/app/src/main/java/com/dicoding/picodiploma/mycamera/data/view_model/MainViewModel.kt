package com.dicoding.picodiploma.mycamera.data.view_model

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.mycamera.data.UploadRepository
import java.io.File

class MainViewModel(private val uploadRepository: UploadRepository) : ViewModel(){
    fun uploadImage(image: File, description:String) = uploadRepository.uploadImage(image, description)
}