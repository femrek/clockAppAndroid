package dev.faruke.helperclock.view.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.faruke.helperclock.R
import dev.faruke.helperclock.service.FakeTimeService.Companion.mainFragmentViewModel
import dev.faruke.helperclock.view.adapters.AddPatternDialogRingsRecyclerViewAdapter
import dev.faruke.helperclock.view.customViews.ClockEditView
import kotlinx.android.synthetic.main.dialog_add_pattern.*


class AddPatternDialog(context: Context, private val activity: AppCompatActivity) : Dialog(context) {

    private var adapter: AddPatternDialogRingsRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        window!!.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)
        setContentView(R.layout.dialog_add_pattern)
        dialogAddPattern_startTimeView.setOnClickListener { startTimeClick(it as ClockEditView) }
        dialogAddPattern_endTimeView.setOnClickListener { endTimeClick(it as ClockEditView) }
        dialogAddPattern_cancelButton.setOnClickListener { dialogAddPatternCancelClick() }
        dialogAddPattern_saveButton.setOnClickListener { dialogAddPatternSaveClick() }
        dialogAddPattern_addRing.setOnClickListener{ addRingTimeClick() }

        adapter = AddPatternDialogRingsRecyclerViewAdapter()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        dialogAddPattern_ringsRecyclerView.layoutManager = layoutManager
        dialogAddPattern_ringsRecyclerView.adapter = adapter

        window?.let {
            val size = Point()
            val w: WindowManager = activity.windowManager
            w.defaultDisplay.getSize(size);
            val lp = it.attributes
            lp.width = (size.x * 0.85).toInt()
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        }

        val list = ArrayList<Int>()
        list.add(TimePickerDialog.hour)
        list.add(TimePickerDialog.minute)
        adapter?.addItem(list)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_OUTSIDE) {
            return true
        }
        return super.onTouchEvent(event)
    }


    private fun startTimeClick(view: ClockEditView) {
        val dialog = TimePickerDialog(view)
        dialog.show(activity.supportFragmentManager, "picker")
    }

    private fun endTimeClick(view: ClockEditView) {
        val dialog = TimePickerDialog(view)
        dialog.show(activity.supportFragmentManager, "picker")
    }

    private fun addRingTimeClick() {
        val dialog = TimePickerDialog(null, adapter = adapter)
        dialog.show(activity.supportFragmentManager, "picker")
    }




    private fun dialogAddPatternSaveClick() {
        val sessionTitle = dialogAddPattern_titleEditText.text.toString()
        val startHour = dialogAddPattern_startTimeView.valueHour
        val startMinute = dialogAddPattern_startTimeView.valueMinute
        val endHour = dialogAddPattern_endTimeView.valueHour
        val endMinute = dialogAddPattern_endTimeView.valueMinute
        val ringClockList = ArrayList<ArrayList<Int>>()
        for (view : View in dialogAddPattern_ringsRecyclerView.children) {
            val clockEditView = view as ClockEditView
            val simpleClockDataList = ArrayList<Int>()
            simpleClockDataList.add(clockEditView.valueHour)
            simpleClockDataList.add(clockEditView.valueMinute)
            ringClockList.add(simpleClockDataList)
        }

        mainFragmentViewModel!!.savePatternToDB(sessionTitle, startHour, startMinute, endHour, endMinute, ringClockList)

    }

    private fun dialogAddPatternCancelClick() {
        dismiss()
    }
}