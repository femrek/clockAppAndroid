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
import dev.faruke.helperclock.R
import dev.faruke.helperclock.model.TimeModel
import dev.faruke.helperclock.util.UtilFuns
import dev.faruke.helperclock.view.MainActivity
import dev.faruke.helperclock.viewmodel.MainViewModel


class FakeTimeService : Service() {

    override fun onBind(p0: Intent?): IBinder? { return null }

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    private var notification: Notification? = null
    private var notificationBuilder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null

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

        notificationBuilder = NotificationCompat.Builder(this, channelId )
        val intent = Intent(this, MainActivity::class.java)
        notification = notificationBuilder!!.setOngoing(true)
            .setContentIntent(PendingIntent.getActivity(mainActivity,0, intent, 0))
            .setContentTitle(resources.getString(R.string.app_name))
            .setContentText(mainFragmentViewModel?.time?.value.toString())
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
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager!!.createNotificationChannel(chan)
        return CHANNEL_ID
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        resumeService()
        return START_STICKY
    }

    override fun onDestroy() {
        currentService = null
        super.onDestroy()
        pauseService()
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
                mHandler.postDelayed(mRunnable, resumeTime + ((tickCounter+1)*1000) - System.currentTimeMillis() )
                currentClock = UtilFuns.nextSecond(mainFragmentViewModel!!.time.value!!)
                mainFragmentViewModel!!.time.value = currentClock
                if (notificationBuilder != null && notificationManager != null) {
                    notificationBuilder!!.setContentText(currentClock.toString())
                    notificationManager!!.notify(101, notificationBuilder!!.build())
                }
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


        var currentPatternTitle: String? = null
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
                mutedRings.clear()
                setNextClock()
            }
        val mutedRings: ArrayList<TimeModel> = ArrayList()

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