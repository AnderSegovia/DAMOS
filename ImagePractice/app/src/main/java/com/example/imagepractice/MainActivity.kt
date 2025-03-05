package com.example.imagepractice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Lista de imágenes y descripciones
        val images = listOf(
            R.drawable.img0 to "Bugatti Chiron",
            R.drawable.img1 to "barchetta",
            R.drawable.img2 to "Huayra"
        )
        // Configurar ViewPager2
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val adapter = ImagePagerAdapter(images)
        viewPager.adapter = adapter
    }
}