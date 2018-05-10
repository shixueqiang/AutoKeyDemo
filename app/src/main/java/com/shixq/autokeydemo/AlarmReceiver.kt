package com.shixq.autokeydemo

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log

/**
 * Created with shixq.
 * Description: 定时器接收广播
 * Date: 2018-05-10
 * Time: 上午10:43
 */
class AlarmReceiver : BroadcastReceiver() {
    val TAG = "AlarmReceiver"

    companion object {
        val ALARM_ACTION = "com.shixq.autokey.alarm.action"
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onReceive(context: Context?, intent: Intent?) {
        val mAction = intent!!.action
        when (mAction) {
            ALARM_ACTION -> {
                Log.d(TAG, "receive action " + ALARM_ACTION)
                if( DakaManager.status) {
                    AppUtils.wakeUpAndUnlock(context!!)
                    val mIntent = Intent(context, FloatBallService::class.java)
                    context!!.startService(mIntent)
                    Log.d(TAG, "开始打卡...")
                    AppUtils.startThirdActivity(context!!, "com.chinasofti.rcs", "com.chinasofti.rcs.action.RcsMessageActivity")
                    DakaManager.startDaka()
                }
            }
            else -> {

            }
        }
    }
}