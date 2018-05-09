package com.shixq.autokeydemo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    val TAG = "MainAty"
    lateinit var mIntent: Intent
    lateinit var mImageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mIntent = Intent(this, FloatBallService::class.java)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()
        sample_text.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this@MainActivity)) {
                    startService(mIntent)
                } else {
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                    startActivityForResult(intent, 100)
                }
            } else {
                startService(mIntent)
            }

            val mBitmap = BitmapFactory.decodeStream(assets.open("dakafail.png"))
            val tessCV = TessCV(mBitmap)
            val grayBitmap = tessCV.imageToGray()
            mImageView = findViewById(R.id.image)
            mImageView.setImageBitmap(grayBitmap)
        }

        val file = File(TessCV.TESS_DATA_PATH)
        if (!file.exists()) {
            file.mkdirs()
        }
        copyBigDataToSD(TessCV.CHI_SIM_FILE, TessCV.TESS_DATA_PATH + File.separator + TessCV.CHI_SIM_FILE)
        copyBigDataToSD(TessCV.CHI_SIM_VERT_FILE, TessCV.TESS_DATA_PATH + File.separator + TessCV.CHI_SIM_VERT_FILE)
    }

    fun copyBigDataToSD(asset: String, strOutFileName: String) {
        val file = File(strOutFileName)
        if (file.exists()) {
            return
        }
        val myOutput = FileOutputStream(strOutFileName)
        val myInput = assets.open(asset)
        val buffer = ByteArray(1024)
        var length = myInput.read(buffer)
        while (length > 0) {
            myOutput.write(buffer, 0, length)
            length = myInput.read(buffer)
        }
        myOutput.flush()
        myInput.close()
        myOutput.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    startService(mIntent)
                } else {
                    Toast.makeText(this, "not granted", Toast.LENGTH_SHORT);
                }
            }
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
