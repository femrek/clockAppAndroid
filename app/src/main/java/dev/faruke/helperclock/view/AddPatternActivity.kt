package dev.faruke.helperclock.view

import android.os.Build
import android.os.Bundle
import android.view.View
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

        val errorMessage = validateForm(sessionTitle, startHour, startMinute, endHour, endMinute, ringClockList)
        if (errorMessage == null){
            FakeTimeService.mainFragmentViewModel!!.savePatternToDB(sessionTitle, startHour, startMinute, endHour, endMinute, ringClockList)
            finish()
        }
        else {
            addPatternActivity_errorMessage.text = errorMessage
            addPatternActivity_errorMessage.visibility = View.VISIBLE
        }

    }

    private fun validateForm(sessionTitle: String, startHour: Int, startMinute: Int, endHour: Int, endMinute: Int, ringClockList: ArrayList<ArrayList<Int>>) : String? {
        if (sessionTitle.isEmpty()) return "Sınav ismi boş bırakılamaz"

        if (startHour == endHour && startMinute == endMinute) return "Başlangıç ve bitiş saati aynı olmaz"
        val isHaveMidnight = (startHour > endHour || (startHour == endHour && startMinute > endMinute))

        val ringTimeEqualStartOrEndTimeErrorMessage = "Bir uyarı saati başlangıç veya bitiş saati ile aynı olamaz"
        val ringTimeOutOfStartAndEndErrorMessage = "Uyarı saatleri başlangıç saati ile bitiş saati arasında olmalıdır"
        for ((index, row) in ringClockList.withIndex()) {
            if (isHaveMidnight) {
                var startValidate: Boolean
                var endValidate: Boolean

                if (row[0] >= startHour) {
                    if (row[0] == startHour) {
                        if (row[1] == startMinute) return ringTimeEqualStartOrEndTimeErrorMessage
                        else startValidate = row[1] > startMinute
                    } else startValidate = true
                } else startValidate = false
                if (row[0] <= endHour) {
                    if (row[0] == endHour){
                        if (row[1] == endMinute) return ringTimeEqualStartOrEndTimeErrorMessage
                        else endValidate = row[1] < endMinute
                    } else endValidate = true
                } else endValidate = false

                if (endValidate == startValidate) return ringTimeOutOfStartAndEndErrorMessage

            } else {
                if (row[0] >= startHour) {
                    if (row[0] == startHour) {
                        if (row[1] == startMinute) return ringTimeEqualStartOrEndTimeErrorMessage
                        if (row[1] < startMinute) return ringTimeOutOfStartAndEndErrorMessage
                    }
                } else return ringTimeOutOfStartAndEndErrorMessage
                if (row[0] <= endHour) {
                    if (row[0] == endHour){
                        if (row[1] == endMinute) return ringTimeEqualStartOrEndTimeErrorMessage
                        if (row[1] > endMinute) return ringTimeOutOfStartAndEndErrorMessage
                    }
                } else return ringTimeOutOfStartAndEndErrorMessage
            }
        }

        return null
    }

    private fun dialogAddPatternCancelClick() {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}