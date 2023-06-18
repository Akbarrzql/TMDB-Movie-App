package com.example.campaigntask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campaigntask.R
import com.example.campaigntask.model.AllCategory

class MainRecyclerViewAdapter(
    private val context: Context,
    private val allCategory: List<AllCategory>,
    private val itemClickListener: CategoryItemAdapter.OnItemClickListener
) : RecyclerView.Adapter<MainRecyclerViewAdapter.MainViewHolder>() {

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryTitle: TextView = itemView.findViewById(R.id.tv_tittle)
        var itemRecyclerView: RecyclerView = itemView.findViewById(R.id.item_rv_category)

        fun bind(category: AllCategory, itemClickListener: CategoryItemAdapter.OnItemClickListener) {
            categoryTitle.text = category.categoryTitle
            val layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            itemRecyclerView.layoutManager = layoutManager
            val itemAdapter = CategoryItemAdapter(itemView.context, category.categoryItem, itemClickListener)
            itemRecyclerView.adapter = itemAdapter
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row_item, parent, false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return allCategory.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val category = allCategory[position]
        holder.bind(category, itemClickListener)
    }
}

