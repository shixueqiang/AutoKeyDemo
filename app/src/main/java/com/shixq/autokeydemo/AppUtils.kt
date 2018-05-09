package com.shixq.autokeydemo

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent


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
    }
}