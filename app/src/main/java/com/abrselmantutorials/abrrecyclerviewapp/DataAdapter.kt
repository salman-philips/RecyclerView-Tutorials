package com.abrselmantutorials.abrrecyclerviewapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DataAdapter(
    private val modelList: List<DataModelForRecyclerViewItem>,
    private val dataItemOnClickListener: DataItemOnClickListener
) :
    RecyclerView.Adapter<DataAdapter.DataViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentItem = modelList[position]
        holder.imageView.setImageResource(currentItem.imageResource)
        holder.heading.text = currentItem.heading
        holder.description.text = currentItem.description
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener,View.OnLongClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val heading: TextView = itemView.findViewById(R.id.heading)
        val description: TextView = itemView.findViewById(R.id.description)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                dataItemOnClickListener.onDataItemClick(position)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                dataItemOnClickListener.onDataItemLongClick(position)
            }
            return true
        }

    }

    interface DataItemOnClickListener {
        fun onDataItemClick(position: Int)
        fun onDataItemLongClick(position: Int)
    }
}