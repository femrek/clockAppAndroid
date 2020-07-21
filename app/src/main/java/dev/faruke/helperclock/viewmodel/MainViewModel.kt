package dev.faruke.helperclock.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import dev.faruke.helperclock.model.PatternModel
import dev.faruke.helperclock.model.TimeModel
import dev.faruke.helperclock.service.FakeTimeService
import dev.faruke.helperclock.service.FakeTimeService.Companion.currentService
import dev.faruke.helperclock.service.FakeTimeService.Companion.isRunning
import dev.faruke.helperclock.service.FakeTimeService.Companion.isStarted
import dev.faruke.helperclock.service.PatternDatabase
import dev.faruke.helperclock.util.UtilFuns
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : BaseViewModel(application) {

    val time = MutableLiveData<TimeModel>()
    val pauseButtonEnable = MutableLiveData<Boolean>()
    val startButtonEnable = MutableLiveData<Boolean>()
    val cancelButtonEnable = MutableLiveData<Boolean>()


    fun startButtonClick(context: Context?) {
        startButtonEnable.value = false

        if (currentService != null) {
            resumeService()
        } else {
            val intent = Intent(context, FakeTimeService::class.java)
            time.value = TimeModel(10,15,0)
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
        currentService = null
        if (context != null) {
            if (isStarted) {
                val intent = Intent(context, FakeTimeService::class.java)
                stopService(intent, context)
            } else {
                Toast.makeText(context, "saat zaten çalışmıyor", Toast.LENGTH_LONG).show()
            }
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
        time.value = TimeModel(0,0,0)
        context.stopService(intent)
    }




    fun savePatternToDB(sessionTitle: String, startHour: Int, startMinute: Int, endHour: Int, endMinute: Int, ringsList: ArrayList<ArrayList<Int>>) {
        launch {
            val pattern = PatternModel(sessionTitle, startHour, startMinute, endHour, endMinute, UtilFuns.convertRingsListToString(ringsList))
            val dao = PatternDatabase(getApplication()).patternDao()
            val id = dao.insertPattern(pattern)
        }
    }
















}