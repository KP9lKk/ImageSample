package com.example.imagesample

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(val repository: Repository): ViewModel() {
    private val _uris = MutableStateFlow(mutableListOf<Uri>())
    val uris = _uris.asStateFlow()

    fun getImages(){
        val listUri = repository.getImages()
        viewModelScope.launch {
            _uris.emit(listUri.toMutableList())
        }
    }

    fun addImage(bitmap: Bitmap){
        val resultUri = repository.saveImage(bitmap)
        if (resultUri != null) {
            getImages()
        }
    }

}