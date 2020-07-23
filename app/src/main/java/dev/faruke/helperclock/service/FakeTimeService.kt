package dev.faruke.helperclock.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import dev.faruke.helperclock.model.TimeModel
import dev.faruke.helperclock.util.UtilFuns
import dev.faruke.helperclock.view.MainActivity
import dev.faruke.helperclock.viewmodel.MainViewModel


class FakeTimeService : Service() {

    override fun onBind(p0: Intent?): IBinder? { return null }

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    override fun onCreate() {
        super.onCreate()
        startForeground()
        mainFragmentViewModel?.cancelButtonEnable?.value = true
        currentService = this
        mHandler = Handler()
    }

    private fun startForeground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("My Background Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId )
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(android.R.drawable.sym_def_app_icon)
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelName: String): String{
        val chan = NotificationChannel(
            CHANNEL_ID,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return CHANNEL_ID
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        resumeService()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        pauseService()
        currentService = null
    }

    fun pauseService() {
        isRunning = false
        mainFragmentViewModel!!.startButtonEnable.value = true
        mHandler.removeCallbacks(mRunnable)
    }

    fun resumeService() {
        isRunning = true
        var currentClock: TimeModel
        val resumeTime: Long = System.currentTimeMillis()
        //println("resume time: $resumeTime")
        var tickCounter = 0
        mRunnable = Runnable {
            //println("runnable start time: ${System.currentTimeMillis()}")
            if(isRunning) {
                tickCounter++
                mHandler.postDelayed(mRunnable, resumeTime + ((tickCounter+1)*1000) - System.currentTimeMillis() ) //978-985
                currentClock = UtilFuns.nextSecond(mainFragmentViewModel!!.time.value!!)
                mainFragmentViewModel!!.time.value = currentClock
                if (currentClock.minute == nextClock!!.minute && currentClock.hour == nextClock!!.hour) {
                    //todo : ring
                    setNextClock()
                }
            }
            //println("runnable end time: ${System.currentTimeMillis()}")
        }
        mHandler.postDelayed(mRunnable, 1000)

        mainFragmentViewModel!!.pauseButtonEnable.value = true
    }

    companion object {
        var mainFragmentViewModel : MainViewModel? = null
        var currentService : FakeTimeService? = null
        var mainActivity: MainActivity? = null

        var isRunning: Boolean = false

        const val CHANNEL_ID = "notificationChannelID"


        var nextClock: TimeModel? = null
        var startClock: TimeModel? = null
        var endClock: TimeModel? = null
            set(value) {
                field = value
                setNextClock()
            }
        var ringClocks: ArrayList<TimeModel>? = null
            set(value) {
                field = value
                setNextClock()
            }

        private fun setNextClock() {
            if (endClock != null && ringClocks != null && mainFragmentViewModel != null && mainFragmentViewModel!!.time.value != null) {
                val allClockList = ArrayList<TimeModel>()
                allClockList.addAll(ringClocks!!)
                allClockList.add(endClock!!)

                val currentClock = mainFragmentViewModel!!.time.value!!
                for (savedClock in allClockList) {
                    if (currentClock.hour <= savedClock.hour){
                        if (currentClock.hour < savedClock.hour) {
                            nextClock = savedClock
                            return
                        } else if (currentClock.hour == savedClock.hour){
                            if(currentClock.minute < savedClock.minute) {
                                nextClock = savedClock
                                return
                            }
                        }
                    }
                }
            }
        }


    }

}