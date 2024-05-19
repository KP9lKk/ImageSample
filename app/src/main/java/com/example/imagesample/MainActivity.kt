package com.example.imagesample

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.logging.StreamHandler
import javax.xml.transform.stream.StreamSource

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val repository:Repository = Repository(baseContext)
        val viewModel: MainViewModel = MainViewModel(repository)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewImages)
        val button = findViewById<Button>(R.id.addImageButton)
        val arrayAdapter = ImageViewAdapter()
        viewModel.getImages()
        recyclerView.adapter = arrayAdapter
        lifecycleScope.launch {
            viewModel.uris.collect{
                arrayAdapter.submitList(it)
            }
        }
        val photoPicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            val bitmap = getBitmap(contentResolver, it!!)
            if (bitmap != null) {
                viewModel.addImage(bitmap)
            }
        }

        button.setOnClickListener {
            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }
}