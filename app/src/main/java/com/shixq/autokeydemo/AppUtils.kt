package com.shixq.autokeydemo

import android.app.KeyguardManager
import android.content.Context
import android.content.Context.KEYGUARD_SERVICE
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import android.support.annotation.RequiresApi


/**
 * Created with shixq.
 * Description:
 * Date: 2018-05-09
 * Time: 上午10:37
 */
class AppUtils {
    companion object {
        fun checkPackInfo(context: Context, packname: String): Boolean {
            var packageInfo: PackageInfo? = null
            try {
                packageInfo = context.getPackageManager().getPackageInfo(packname, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return packageInfo != null
        }

        fun startThirdActivity(context: Context, packname: String, action: String) {
            if (checkPackInfo(context, packname)) {
                val intent = Intent(action)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }

        @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
        fun wakeUpAndUnlock(context: Context) {
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isInteractive) {
                val wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK, "bright")
                wl.acquire(1000)
                val keyguardManager = context.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
                val keyguardLock = keyguardManager.newKeyguardLock("unLock")
                keyguardLock.disableKeyguard()
            }
        }
    }
}