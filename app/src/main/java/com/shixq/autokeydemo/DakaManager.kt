package com.shixq.autokeydemo

import android.util.Log
import java.util.concurrent.Executors

/**
 * Created with shixq.
 * Description:
 * Date: 2018-05-09
 * Time: 上午10:55
 */
class DakaManager {
    companion object {
        val TAG = "DakaManager"
        var status = false
        private val fixedThreadPool = Executors.newFixedThreadPool(3)
        fun startDaka() {
            fixedThreadPool.run {
                val exeCommand = ExeCommand()
                Thread.sleep(5000)
                Log.d(TAG, "点击左上角进入负一屏")
                exeCommand.run("input tap 91 100", 1000)
                Thread.sleep(2000)
                Log.d(TAG, "点击考勤打卡")
                exeCommand.run("input tap 544 888", 1000)
                Thread.sleep(5000)
                Log.d(TAG, "点击打卡")
                exeCommand.run("input tap 716 2068", 1000)
                Log.d(TAG, "打卡完成")
            }
        }
    }
}