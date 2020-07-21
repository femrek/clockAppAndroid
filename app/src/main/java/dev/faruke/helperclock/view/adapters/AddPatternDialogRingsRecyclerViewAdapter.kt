package dev.faruke.helperclock.view.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.faruke.helperclock.view.customViews.ClockEditView


class AddPatternDialogRingsRecyclerViewAdapter : RecyclerView.Adapter<AddPatternDialogRingsRecyclerViewAdapter.AddPatternDialogRingsRecyclerViewHolder>() {

    val itemList = ArrayList<ArrayList<Int>>()
    get()  {
        return field
    }

    class AddPatternDialogRingsRecyclerViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddPatternDialogRingsRecyclerViewHolder {
        val view = ClockEditView(parent.context)
        return AddPatternDialogRingsRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: AddPatternDialogRingsRecyclerViewHolder, position: Int) {
        println("onBindViewHolder is running")
        (holder.view as ClockEditView).valueHour = itemList[position][0]
        (holder.view as ClockEditView).valueMinute = itemList[position][1]
        (holder.view as ClockEditView).type = ClockEditView.TYPE_DELETE
        (holder.view as ClockEditView).setOnClickListener {
            removeViewAt(position)
        }
    }

    fun addItem(valueList: ArrayList<Int>) : Int {
        var addedIndex = -1
        for ((index, row) in itemList.withIndex()) {
            if (row[0] >= valueList[0]) {
                if (row[0] == valueList[0]) {
                    if(row[1] > valueList[1]) {
                        itemList.add(index, valueList)
                        addedIndex = index
                    } else if (row[1] == valueList[1]) {
                        error("clock already exist")
                    }
                } else {
                    itemList.add(index, valueList)
                    addedIndex = index
                }
            }
        }
        notifyDataSetChanged()
        return addedIndex
    }

    fun removeViewAt(index: Int) {
        itemList.removeAt(index)
        notifyDataSetChanged()
    }

}