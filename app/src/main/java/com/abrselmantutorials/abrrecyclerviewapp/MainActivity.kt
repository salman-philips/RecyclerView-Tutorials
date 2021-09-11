package com.abrselmantutorials.abrrecyclerviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random


class MainActivity : AppCompatActivity(), DataAdapter.DataItemOnClickListener {
    private lateinit var dataRecyclerView: RecyclerView
    private val dataModelForRecyclerViewItemList = generateDataModelForRecyclerViewItemList(500)
    private val dataAdapter: DataAdapter =
        DataAdapter(dataModelForRecyclerViewItemList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataRecyclerView = findViewById(R.id.recycler_view)
//        dataRecyclerView.setOnClickListener {
//            Toast.makeText(
//                this@MainActivity,
//                "item clicked",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
        dataRecyclerView.adapter = dataAdapter
        dataRecyclerView.layoutManager = LinearLayoutManager(this)
        dataRecyclerView.setHasFixedSize(true)
    }

    private fun generateDataModelForRecyclerViewItemList(size: Int): ArrayList<DataModelForRecyclerViewItem> {
        val modelList = ArrayList<DataModelForRecyclerViewItem>()
        for (i in 0 until size) {
            val drawableIntId = when (i % 3) {
                0 -> R.drawable.ic_baseline_360_24
                1 -> R.drawable.ic_baseline_ac_unit_24
                else -> R.drawable.ic_baseline_architecture_24
            }
            modelList.add(
                DataModelForRecyclerViewItem(
                    drawableIntId,
                    "Heading ${i + 1}",
                    "Description ${i + 1}"
                )
            )
        }
        return modelList
    }

    fun insertItem(view: View) {
        val randomInsertIndex = Random.nextInt(8)
        val newItem = DataModelForRecyclerViewItem(
            R.drawable.ic_baseline_ac_unit_24,
            "New Heading ${randomInsertIndex + 1}",
            "New Description ${randomInsertIndex + 1}"
        )
        dataModelForRecyclerViewItemList.add(randomInsertIndex, newItem)
        dataAdapter.notifyItemInserted(randomInsertIndex)
    }

    fun removeItem(view: View) {
        val randomInsertIndex = Random.nextInt(8)
        dataModelForRecyclerViewItemList.removeAt(randomInsertIndex)
        dataAdapter.notifyItemRemoved(randomInsertIndex)
    }

    override fun onDataItemClick(position: Int) {
        Toast.makeText(this, "Item clicked at position ${position + 1}", Toast.LENGTH_SHORT).show()
        val itemClicked = dataModelForRecyclerViewItemList[position]
        itemClicked.heading="Item Clicked"
        dataAdapter.notifyItemChanged(position)
    }
}