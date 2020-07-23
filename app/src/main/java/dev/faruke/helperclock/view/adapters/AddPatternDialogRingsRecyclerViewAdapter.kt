package dev.faruke.helperclock.view.adapters

import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import dev.faruke.helperclock.util.UtilFuns.Companion.dpToPx
import dev.faruke.helperclock.view.customViews.ClockEditView


class AddPatternDialogRingsRecyclerViewAdapter(private var context: Context) : RecyclerView.Adapter<AddPatternDialogRingsRecyclerViewAdapter.AddPatternDialogRingsRecyclerViewHolder>() {

    var recyclerView: RecyclerView? = null

    val itemList = ArrayList<ArrayList<Int>>()
    get()  {
        return field
    }

    class AddPatternDialogRingsRecyclerViewHolder(var view: ClockEditView) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddPatternDialogRingsRecyclerViewHolder {
        val view = ClockEditView(parent.context)
        return AddPatternDialogRingsRecyclerViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: AddPatternDialogRingsRecyclerViewHolder, position: Int) {
        println("onBindViewHolder is running")
        holder.view.valueHour = itemList[position][0]
        holder.view.valueMinute = itemList[position][1]
        holder.view.type = ClockEditView.TYPE_DELETE
        holder.view.setOnClickListener {
            removeViewWhereTimeIs(itemList[position])
        }

        val layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, dpToPx(context, 8f).toInt(), dpToPx(context, 8f).toInt(), dpToPx(context, 8f).toInt())
        holder.view.layoutParams = layoutParams
    }


    fun addItem(valueList: ArrayList<Int>) : Int {
        var addedIndex = -1
        if (itemList.size == 0) {
            itemList.add(valueList)
            addedIndex = 0
        } else for ((index, row) in itemList.withIndex()) {
            if (row[0] >= valueList[0]) {
                if (row[0] == valueList[0]) {
                    if(row[1] > valueList[1]) {
                        itemList.add(index, valueList)
                        addedIndex = index
                        break
                    } else if (row[1] == valueList[1]) {
                        addedIndex = -2
                        break
                    }
                } else {
                    itemList.add(index, valueList)
                    addedIndex = index
                    break
                }
            }
        }

        if (addedIndex == -1) {
            addedIndex = itemList.size
            itemList.add(valueList)
        }

        if (addedIndex < 0) {
            if (addedIndex == -2) Toast.makeText(context, "Bu saat daha önce ayarlanmış", Toast.LENGTH_LONG).show()
        } else {
            notifyDataSetChanged()
            recyclerView?.scrollToPosition(addedIndex)
        }
        return addedIndex
    }

    fun removeViewAt(index: Int) {
        itemList.removeAt(index)
        notifyDataSetChanged()
    }

    fun removeViewWhereTimeIs(time: ArrayList<Int>) : Boolean {
        for ((index, row) in itemList.withIndex()) {
            if (row[0] == time[0] && row [1] == time[1]) {
                removeViewAt(index)
                return true
            }
        }
        return false
    }

    fun removeViewWhereTimeIs(hour: Int, minute: Int) : Boolean {
        for ((index, row) in itemList.withIndex()) {
            if (row[0] == hour && row [1] == minute) {
                removeViewAt(index)
                return true
            }
        }
        return false
    }

}
