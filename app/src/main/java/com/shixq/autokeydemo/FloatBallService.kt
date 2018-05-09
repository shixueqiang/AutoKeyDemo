package com.shixq.autokeydemo

import android.app.Service
import android.content.Intent
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
        return START_STICKY
    }
}