package com.shixq.autokeydemo

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log

/**
 * Created with shixq.
 * Description:
 * Date: 2018-05-08
 * Time: 下午6:10
 */
class FloatBallService : Service() {
    val TAG = "FloatBallService"
    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        val manager = ViewManager.getInstance(this)
        manager.showFloatBall()
        startAlarm()
        return START_STICKY
    }

    fun startAlarm() {
        val mAlarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val mIntent = Intent(AlarmReceiver.ALARM_ACTION)
        val mPendingIntent = PendingIntent.getBroadcast(this, 0, mIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, 10 * 1000, mPendingIntent)
        } else {
            mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 10 * 1000, mPendingIntent)
        }
    }
}