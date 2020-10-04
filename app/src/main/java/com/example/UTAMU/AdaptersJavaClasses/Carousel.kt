package com.example.UTAMU.AdaptersJavaClasses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.UTAMU.R
import com.squareup.picasso.Picasso

class Carousel(val wt_slides :List<String>,val wt_slideImages: List<Int>):
    RecyclerView.Adapter<Carousel.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textholder: TextView = itemView.findViewById(R.id.walkslide)
        var imageholder: ImageView = itemView.findViewById(R.id.walkslideImage)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val holder =
            LayoutInflater.from(parent.context).inflate(R.layout.carouselfrag1, parent, false)
        return ViewHolder(holder)
    }

    override fun getItemCount(): Int =wt_slides.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textholder.text=wt_slides[position]
        Picasso.get().load(wt_slideImages[position]).fit().into(holder.imageholder)
    }

}