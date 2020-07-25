package dev.faruke.helperclock.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.MutableLiveData
import dev.faruke.helperclock.model.PatternModel
import dev.faruke.helperclock.model.TimeModel
import dev.faruke.helperclock.service.FakeTimeService
import dev.faruke.helperclock.service.FakeTimeService.Companion.currentService
import dev.faruke.helperclock.service.FakeTimeService.Companion.isRunning
import dev.faruke.helperclock.service.FakeTimeService.Companion.mainActivity
import dev.faruke.helperclock.service.FakeTimeService.Companion.startClock
import dev.faruke.helperclock.service.PatternDatabase
import dev.faruke.helperclock.util.GlobalVariables
import dev.faruke.helperclock.util.UtilFuns
import dev.faruke.helperclock.view.AddPatternActivity
import dev.faruke.helperclock.view.MainFragment
import dev.faruke.helperclock.view.customViews.ClockPatternCheckbox
import dev.faruke.helperclock.view.customViews.ClockPatternCheckboxWithActions
import dev.faruke.helperclock.view.dialogs.ConfirmDeletePattern
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : BaseViewModel(application) {

    val time = MutableLiveData<TimeModel>()
    val pauseButtonEnable = MutableLiveData<Boolean>()
    val startButtonEnable = MutableLiveData<Boolean>()
    val cancelButtonEnable = MutableLiveData<Boolean>()
    val refreshPatternsCheckboxes = MutableLiveData<Boolean>()
    val titleText = MutableLiveData<String>()


    fun startButtonClick(context: Context?, mainFragment: MainFragment) {
        startButtonEnable.value = false

        if (currentService != null) {
            resumeService()
        } else {
            val intent = Intent(context, FakeTimeService::class.java)
            val pattern = mainFragment.selectedPatternView!!.pattern
            time.value = TimeModel(pattern?.startHour ?: 1, pattern?.startMinute ?: 30, 0)
            if (context != null) {
                if (!isRunning) {
                    startService(intent, context)
                }
            }
        }
    }

    fun pauseButtonClick() {
        pauseButtonEnable.value = false
        if (currentService != null) {
            if (isRunning) {
                pauseService()
            }
        }
    }

    fun terminateButtonClick(context: Context?) {
        pauseButtonEnable.value = false
        cancelButtonEnable.value = false
        if (context != null) {
            val intent = Intent(context, FakeTimeService::class.java)
            stopService(intent, context)
        }
    }


    fun startService(intent: Intent, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(intent)
        else context.startService(intent)
    }

    fun resumeService() {
        currentService?.resumeService()
    }

    fun pauseService() {
        currentService?.pauseService()
    }

    fun stopService(intent: Intent, context: Context) {
        time.value = startClock ?: TimeModel(0, 0, 0)
        context.stopService(intent)
    }


    fun savePatternToDB(
        sessionTitle: String,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        ringsList: ArrayList<ArrayList<Int>>
    ) {
        launch {
            val pattern = PatternModel(
                sessionTitle,
                startHour,
                startMinute,
                endHour,
                endMinute,
                UtilFuns.convertRingsListToString(ringsList)
            )
            val dao = PatternDatabase(getApplication()).patternDao()
            val id = dao.insertPattern(pattern)
            refreshPatternsCheckboxes.value = true
        }
    }

    fun readPatternsFromDBAndShowIn(parentView: ViewGroup, mainFragment: MainFragment) {
        if (isReadingPatterns) return
        isReadingPatterns = true
        println("reading data from db")
        launch {
            println("reading data from db in launch")
            val dao = PatternDatabase(getApplication()).patternDao()
            val lp = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            lp.setMargins(0, UtilFuns.dpToPx(mainFragment.requireContext(), 8f).toInt(), 0, 0)
            for ((index, pattern) in dao.getAllPatterns().withIndex()) {
                val clockPatternCheckboxWithActions =
                    ClockPatternCheckboxWithActions(mainFragment.requireContext())
                clockPatternCheckboxWithActions.clockPatternCheckbox!!.pattern = pattern
                clockPatternCheckboxWithActions.clockPatternCheckbox!!.setOnClickListener(
                    mainFragment.patternCheckboxClickListener
                )
                clockPatternCheckboxWithActions.setReplaceClickListener(View.OnClickListener {
                    GlobalVariables.replacePattern =
                        clockPatternCheckboxWithActions.clockPatternCheckbox!!.pattern
                    mainActivity?.startActivity(
                        Intent(
                            mainFragment.requireContext(),
                            AddPatternActivity::class.java
                        )
                    )
                })
                clockPatternCheckboxWithActions.setDeleteClickListener(View.OnClickListener {
                    //deletePatternOf(clockPatternCheckboxWithActions.clockPatternCheckbox!!, mainFragment)
                    if (clockPatternCheckboxWithActions.clockPatternCheckbox!!.pattern != null) {
                        val dialog = ConfirmDeletePattern(
                            clockPatternCheckboxWithActions.clockPatternCheckbox!!.pattern!!.id,
                            mainFragment
                        )
                        dialog.show()
                    }
                })
                clockPatternCheckboxWithActions.layoutParams = lp
                parentView.addView(clockPatternCheckboxWithActions)
            }
            isReadingPatterns = false
        }
    }

    fun deletePatternOf(clockPatternCheckbox: ClockPatternCheckbox, mainFragment: MainFragment) {
        launch {
            if (clockPatternCheckbox.pattern != null) {
                deletePattern(clockPatternCheckbox.pattern!!.id, mainFragment)
            }
        }
    }

    fun deletePattern(patternId: Int, mainFragment: MainFragment) {
        launch {
            val dao = PatternDatabase(getApplication()).patternDao()
            dao.deletePatternAt(patternId)
            mainFragment.addPatterns()
        }
    }

    fun replacePatternAt(id: Int, pattern: PatternModel) {
        launch {
            val dao = PatternDatabase(getApplication()).patternDao()
            dao.replacePatternAt(
                id,
                pattern.title,
                pattern.startHour,
                pattern.startMinute,
                pattern.endHour,
                pattern.endMinute,
                pattern.ringsList
            )
        }
    }


    companion object {
        var isReadingPatterns = false
    }

}