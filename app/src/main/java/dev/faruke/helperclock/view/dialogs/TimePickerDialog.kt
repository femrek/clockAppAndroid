package dev.faruke.helperclock.view.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.Window
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import dev.faruke.helperclock.view.adapters.AddPatternDialogRingsRecyclerViewAdapter
import dev.faruke.helperclock.view.customViews.ClockEditView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

class TimePickerDialog(private val clockEditView: ClockEditView?, private val adapter: AddPatternDialogRingsRecyclerViewAdapter? = null) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, is24HourFormat(activity))
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        if (clockEditView != null) {
            clockEditView.valueHour = p1
            clockEditView.valueMinute = p2
        } else if (adapter != null){
            hour = p1
            minute = p2
            val list = ArrayList<Int>()
            list.add(hour)
            list.add(minute)
            adapter.addItem(list)
        }
    }


    companion object {
        var hour : Int = 0
        var minute : Int = 0
    }


}

