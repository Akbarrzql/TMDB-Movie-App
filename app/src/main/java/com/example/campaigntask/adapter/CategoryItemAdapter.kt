package com.example.campaigntask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.campaigntask.R
import com.example.campaigntask.model.CategoryItem
import com.squareup.picasso.Picasso

class CategoryItemAdapter(
    private val context: Context,
    private val categoryItemList: List<CategoryItem>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CategoryItemAdapter.CategoryItemViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryItem: CategoryItem)
    }

    inner class CategoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryTitle: TextView = itemView.findViewById(R.id.tv_tittle_movie)
        var categoryImage: ImageView = itemView.findViewById(R.id.imageView)

        init {

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val categoryItem = categoryItemList[position]
                    itemClickListener.onItemClick(categoryItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_row_item, parent, false)
        return CategoryItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoryItemList.size
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val categoryItem = categoryItemList[position]
        holder.categoryTitle.text = categoryItem.categoryTittle
        val imageUrl = categoryItem.categoryImage
        Picasso.get().setIndicatorsEnabled(false)
        Picasso.get().load(imageUrl).into(holder.categoryImage)
    }
}

