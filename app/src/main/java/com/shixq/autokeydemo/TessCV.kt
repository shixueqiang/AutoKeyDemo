package com.shixq.autokeydemo

import android.graphics.Bitmap
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import org.opencv.core.CvType
import android.opengl.ETC1.getWidth
import android.opengl.ETC1.getHeight
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import org.opencv.core.CvType.channels


/**
 * Created with shixq.
 * Description:
 * Date: 2018-05-09
 * Time: 下午2:39
 */
class TessCV(private val mBitmap: Bitmap) {

    companion object {
        val TESS_BASE_PATH = "data/data/com.shixq.autokeydemo"
        val TESS_DATA_PATH: String = TESS_BASE_PATH + "/tessdata"
        val CHI_SIM_FILE: String = "chi_sim.traineddata"
        val CHI_SIM_VERT_FILE: String = "chi_sim_vert.traineddata"
        val CHI_SIM_LANGEUAGE = "chi_sim"
        val CHI_SIM_VERT_LANGEUAGE = "chi_sim_vert"
    }

    lateinit var tessApi: TessBaseAPI

    init {
//        tessApi = TessBaseAPI()
//        tessApi.init(TESS_BASE_PATH, CHI_SIM_LANGEUAGE)
        System.loadLibrary("opencv_java3")
    }

    fun imageToGray(): Bitmap {
        //1 通道为灰度图；CV_8UC1
        // 2 通道的图像是RGB555和RGB565。2通道图在程序处理中会用到，如傅里叶变换，可能会用到，一个通道为实数，一个通道为虚数，主要是编程方便。RGB555是16位的，2个字节，5+6+5，第一字节的前5位是R，后三位+第二字节是G，第二字节后5位是B，可见对原图像进行压缩了
        //3 通道为彩色图（RGB）；CV_8UC3
        //4 通道为 RGBA ，是RGB加上一个A通道，也叫alpha通道，表示透明度，PNG图像是一种典型的4通道图像。alpha通道可以赋值0到1，或者0到255，表示透明到不透明
        val imgBgra = Mat(mBitmap.getHeight(), mBitmap.getWidth(), CvType.CV_8UC4)
        Utils.bitmapToMat(mBitmap, imgBgra)
        val imgBgr = Mat()
        Imgproc.cvtColor(imgBgra, imgBgr, Imgproc.COLOR_RGBA2BGR)
        //灰度处理
        if (imgBgr.channels() == 3) {
            Imgproc.cvtColor(imgBgr, imgBgr, Imgproc.COLOR_BGR2GRAY);
        }
//        val bytes = ByteArray((imgBgr.total() * imgBgr.channels()) as Int)
//        imgBgr.get(0, 0, bytes)
        val resultBitmap = Bitmap.createBitmap(mBitmap.width, mBitmap.height, Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(imgBgr, resultBitmap)
        return resultBitmap
    }

    fun getImageText(): String? {
//        tessApi.setImage(bytes, imgBgr.cols(), imgBgr.rows(), 1, imgBgr.cols())
        tessApi.setImage(imageToGray())
        val text = tessApi.getUTF8Text()
        return text
    }
}