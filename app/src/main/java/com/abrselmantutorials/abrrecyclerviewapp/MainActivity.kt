package com.abrselmantutorials.abrrecyclerviewapp

import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class MainActivity : AppCompatActivity(), DataAdapter.DataItemOnClickListener {
    private lateinit var dataRecyclerView: RecyclerView
    private val dataModelForRecyclerViewItemList = generateDataModelForRecyclerViewItemList(10)
    private val tempList=ArrayList<DataModelForRecyclerViewItem>(dataModelForRecyclerViewItemList)

    private val dataAdapter: DataAdapter =
        DataAdapter(tempList, this)


    private lateinit var deletedItem: DataModelForRecyclerViewItem
    private lateinit var archivedItem: DataModelForRecyclerViewItem


    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataRecyclerView = findViewById(R.id.recycler_view)
        dataRecyclerView.adapter = dataAdapter
        dataRecyclerView.layoutManager = LinearLayoutManager(this)

        dataRecyclerView.setHasFixedSize(true)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {

            val dataModelForRecyclerViewItemListTemp = ArrayList<DataModelForRecyclerViewItem>()
            for (i in 0 until 8) {
                val (randomInsertIndex, newItem) = createItem()
                dataModelForRecyclerViewItemListTemp.add(newItem)
            }
            dataModelForRecyclerViewItemList.addAll(dataModelForRecyclerViewItemListTemp)
            tempList.clear()
            tempList.addAll(dataModelForRecyclerViewItemList)
            dataAdapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
        val itemTouchHelperSimpleCallBack = object :
            ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                Collections.swap(dataModelForRecyclerViewItemList, fromPosition, toPosition)
                tempList.clear()
                tempList.addAll(dataModelForRecyclerViewItemList)
                dataAdapter.notifyItemMoved(fromPosition, toPosition)
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.purple_500
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.design_default_color_error
                        )
                    )
                    .addSwipeRightActionIcon(R.drawable.archive_icon)
                    .create().decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        deletedItem = dataModelForRecyclerViewItemList[position]
                        dataModelForRecyclerViewItemList.removeAt(position)
                        tempList.clear()
                        tempList.addAll(dataModelForRecyclerViewItemList)
                        dataAdapter.notifyItemRemoved(position)
                        Snackbar.make(
                            dataRecyclerView,
                            deletedItem.heading + " Deleted",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction(
                                "Undo"
                            ) {
                                dataModelForRecyclerViewItemList.add(position, deletedItem)
                                tempList.clear()
                                tempList.addAll(dataModelForRecyclerViewItemList)
                                dataAdapter.notifyItemInserted(position)
                            }.show()
                    }
                    ItemTouchHelper.RIGHT -> {
                        archivedItem = dataModelForRecyclerViewItemList[position]
                        dataModelForRecyclerViewItemList.removeAt(position)
                        tempList.clear()
                        tempList.addAll(dataModelForRecyclerViewItemList)
                        dataAdapter.notifyItemRemoved(position)
                        Snackbar.make(
                            dataRecyclerView,
                            archivedItem.heading + " Archived",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction(
                                "Undo"
                            ) {
                                dataModelForRecyclerViewItemList.add(position, archivedItem)
                                tempList.clear()
                                tempList.addAll(dataModelForRecyclerViewItemList)
                                dataAdapter.notifyItemInserted(position)
                            }.show()
                    }
                }

            }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperSimpleCallBack)
        itemTouchHelper.attachToRecyclerView(dataRecyclerView)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val menuItem = menu?.findItem(R.id.searchIconMenu)
        val searchView = menuItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempList.clear()
                val searchText = newText.toString().toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    dataModelForRecyclerViewItemList.forEach {
                        if (it.heading.toLowerCase(Locale.getDefault()).contains(searchText)) {
                            tempList.add(it)
                        }
                    }

                    dataAdapter.notifyDataSetChanged()
                }
                else{
                    tempList.addAll(dataModelForRecyclerViewItemList)
                    dataAdapter.notifyDataSetChanged()
                }
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    fun insertItem(view: View) {
        val (randomInsertIndex, newItem) = createItem()
        dataModelForRecyclerViewItemList.add(newItem)
        tempList.clear()
        tempList.addAll(dataModelForRecyclerViewItemList)
        dataAdapter.notifyDataSetChanged()
    }

    private fun createItem(): Pair<Int, DataModelForRecyclerViewItem> {
        val randomInsertIndex = Random.nextInt(8)
        val newItem = DataModelForRecyclerViewItem(
            R.drawable.ic_baseline_ac_unit_24,
            "New Heading ${randomInsertIndex + 1}",
            "New Description ${randomInsertIndex + 1}"
        )
        return Pair(randomInsertIndex, newItem)
    }

    fun removeItem(view: View) {
        val randomInsertIndex = Random.nextInt(8)
        if (dataModelForRecyclerViewItemList.size > randomInsertIndex) {
            dataModelForRecyclerViewItemList.removeAt(randomInsertIndex)
            tempList.clear()
            tempList.addAll(dataModelForRecyclerViewItemList)
            dataAdapter.notifyItemRemoved(randomInsertIndex)
        }
    }

    override fun onDataItemClick(position: Int) {
        Toast.makeText(this, "Item clicked at position ${position + 1}", Toast.LENGTH_SHORT).show()
        val itemClicked = dataModelForRecyclerViewItemList[position]
        itemClicked.heading = "Item Clicked"
        tempList.clear()
        tempList.addAll(dataModelForRecyclerViewItemList)
        dataAdapter.notifyItemChanged(position)
    }

    override fun onDataItemLongClick(position: Int) {
        Toast.makeText(this, "Item long clicked at position ${position + 1}", Toast.LENGTH_SHORT)
            .show()
        val itemClicked = dataModelForRecyclerViewItemList[position]
        itemClicked.heading = "Item Long Clicked"
        tempList.clear()
        tempList.addAll(dataModelForRecyclerViewItemList)
        dataAdapter.notifyItemChanged(position)
    }

}