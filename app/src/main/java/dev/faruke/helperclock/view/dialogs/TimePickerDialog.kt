package dev.faruke.helperclock.view.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import dev.faruke.helperclock.view.adapters.AddPatternDialogRingsRecyclerViewAdapter
import dev.faruke.helperclock.view.customViews.ClockEditView
import java.util.*
import kotlin.collections.ArrayList

class TimePickerDialog(private val clockEditView: ClockEditView? = null, private val parent: ViewGroup? = null, private val adapter: AddPatternDialogRingsRecyclerViewAdapter? = null) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var isFirst = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, is24HourFormat(activity))
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        if (!isFirst) return

        if (clockEditView != null) {
            clockEditView.valueHour = hour
            clockEditView.valueMinute = minute
        } else if (parent!= null){
            val child = ClockEditView(context)
            child.valueHour = hour
            child.valueMinute = minute
            child.type = ClockEditView.TYPE_DELETE

            var addIndex = -1
            for ((index, view) in parent.children.withIndex()) {
                val childClockEditView = view as ClockEditView
                if (childClockEditView.valueHour >= hour) {
                    if (childClockEditView.valueHour == hour) {
                        if(childClockEditView.valueMinute > minute) {
                            addIndex = index
                        } else if (childClockEditView.valueMinute == minute) {
                            addIndex = -2
                        }
                    } else {
                        addIndex = index
                    }
                }
            }

            if (addIndex < 0) {
                if (addIndex == -1) Toast.makeText(context, "Bir hata oluştu", LENGTH_LONG).show()
                if (addIndex == -2) Toast.makeText(context, "Bu saat daha önce ayatlanmış", LENGTH_LONG).show()
            } else {
                parent.addView(child, addIndex)
            }
        } else if (adapter != null) {
            val list = ArrayList<Int>()
            list.add(hour)
            list.add(minute)
            adapter.addItem(list)
        }

        isFirst = false

    }
}

