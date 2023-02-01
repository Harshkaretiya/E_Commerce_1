package com.example.e_commerce1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso

class ImageSlider(var images : ArrayList<String>) : SliderViewAdapter<ImageSlider.MyHolder>() {
    inner class MyHolder(itemView: View?) : ViewHolder(itemView) {
        var imageView: ImageView = itemView!!.findViewById(R.id.imageView)
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): MyHolder? {
        var view = LayoutInflater.from(parent!!.context).inflate(R.layout.slider_item,parent,false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyHolder?, position: Int) {
        if (images[position] != null) {
            Picasso.get().load(images[position]).into(viewHolder!!.imageView)
        }
    }

}