package dev.faruke.helperclock.view

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dev.faruke.helperclock.R
import dev.faruke.helperclock.service.FakeTimeService
import dev.faruke.helperclock.view.adapters.AddPatternDialogRingsRecyclerViewAdapter
import dev.faruke.helperclock.view.customViews.ClockEditView
import dev.faruke.helperclock.view.dialogs.TimePickerDialog
import kotlinx.android.synthetic.main.activity_add_pattern.*

class AddPatternActivity : AppCompatActivity() {

    private var adapter: AddPatternDialogRingsRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pattern)
        setSupportActionBar(addPatternActivity_toolbar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        dialogAddPattern_startTimeView.setOnClickListener { startTimeClick(it as ClockEditView) }
        dialogAddPattern_endTimeView.setOnClickListener { endTimeClick(it as ClockEditView) }
        dialogAddPattern_cancelButton.setOnClickListener { dialogAddPatternCancelClick() }
        dialogAddPattern_saveButton.setOnClickListener { dialogAddPatternSaveClick() }
        dialogAddPattern_addRing.setOnClickListener{ addRingTimeClick() }

        adapter = AddPatternDialogRingsRecyclerViewAdapter(this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        dialogAddPattern_ringsRecyclerView.layoutManager = layoutManager
        dialogAddPattern_ringsRecyclerView.adapter = adapter
    }

    private fun startTimeClick(view: ClockEditView) {
        val dialog = TimePickerDialog(view)
        dialog.show(this .supportFragmentManager, "picker")
    }

    private fun endTimeClick(view: ClockEditView) {
        val dialog = TimePickerDialog(view)
        dialog.show(this.supportFragmentManager, "picker")
    }

    private fun addRingTimeClick() {
        val dialog = TimePickerDialog(adapter = adapter)
        dialog.show(this.supportFragmentManager, "picker")
    }


    private fun dialogAddPatternSaveClick() {
        val sessionTitle = dialogAddPattern_titleEditText.text.toString()
        val startHour = dialogAddPattern_startTimeView.valueHour
        val startMinute = dialogAddPattern_startTimeView.valueMinute
        val endHour = dialogAddPattern_endTimeView.valueHour
        val endMinute = dialogAddPattern_endTimeView.valueMinute
        val ringClockList = ArrayList<ArrayList<Int>>()
        for (row : ArrayList<Int> in adapter!!.itemList) {
            ringClockList.add(row)
        }

        FakeTimeService.mainFragmentViewModel!!.savePatternToDB(sessionTitle, startHour, startMinute, endHour, endMinute, ringClockList)

    }

    private fun dialogAddPatternCancelClick() {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}