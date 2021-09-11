package com.abrselmantutorials.abrrecyclerviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private lateinit var dataRecyclerView: RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataRecyclerView = findViewById(R.id.recycler_view)
        dataRecyclerView.adapter = DataAdapter(generateDataModelForRecyclerViewItemList(500))
        dataRecyclerView.layoutManager = LinearLayoutManager(this)
        dataRecyclerView.setHasFixedSize(true)
    }

    private fun generateDataModelForRecyclerViewItemList(size: Int): List<DataModelForRecyclerViewItem> {
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
}