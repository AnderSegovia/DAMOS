package com.example.imagepractice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jsibbold.zoomage.ZoomageView

class ImagePagerAdapter(
    private val images: List<Pair<Int, String>>

) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val zoomageView: ZoomageView = itemView.findViewById(R.id.zoomageView)
        val description: TextView = itemView.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val (imageResId, descriptionText) = images[position]
        holder.zoomageView.setImageResource(imageResId)
        holder.description.text = descriptionText
    }

    override fun getItemCount(): Int = images.size
}