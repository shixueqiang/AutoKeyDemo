package com.shixq.autokeydemo

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams


/**
 * Created with shixq.
 * Description:
 * Date: 2018-05-08
 * Time: 下午5:12
 */
class ViewManager private constructor(private val context: Context) {
    val TAG = "ViewManager"
    private var floatBall: FloatBall? = null

    private var windowManager: WindowManager? = null

    private var floatBallParams: LayoutParams? = null

    private var floatMenuParams: LayoutParams? = null

    //获取屏幕宽度
    val screenWidth: Int
        get() {
            val point = Point()
            windowManager!!.defaultDisplay.getSize(point)
            return point.x
        }

    //获取屏幕高度
    val screenHeight: Int
        get() {
            val point = Point()
            windowManager!!.defaultDisplay.getSize(point)
            return point.y
        }

    //获取状态栏高度
    val statusHeight: Int
        get() {
            try {
                val c = Class.forName("com.android.internal.R\$dimen")
                val `object` = c.newInstance()
                val field = c.getField("status_bar_height")
                val x = field.get(`object`) as Int
                return context.getResources().getDimensionPixelSize(x)
            } catch (e: Exception) {
                return 0
            }

        }

    init {
        init()
    }

    fun init() {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        floatBall = FloatBall(context)
        val touchListener = object : View.OnTouchListener {
            internal var startX: Float = 0.toFloat()
            internal var startY: Float = 0.toFloat()
            internal var tempX: Float = 0.toFloat()
            internal var tempY: Float = 0.toFloat()

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = event.rawX
                        startY = event.rawY

                        tempX = event.rawX
                        tempY = event.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val x = event.rawX - startX
                        val y = event.rawY - startY
                        //计算偏移量，刷新视图
                        floatBallParams!!.x += x.toInt()
                        floatBallParams!!.y += y.toInt()
                        floatBall!!.setDragState(true)
                        windowManager!!.updateViewLayout(floatBall, floatBallParams)
                        startX = event.rawX
                        startY = event.rawY
                    }
                    MotionEvent.ACTION_UP -> {
                        //判断松手时View的横坐标是靠近屏幕哪一侧，将View移动到依靠屏幕
                        var endX = event.rawX
                        val endY = event.rawY
                        if (endX < screenWidth / 2) {
                            endX = 0f
                        } else {
                            endX = (screenWidth - floatBall!!.mWidth).toFloat()
                        }
                        floatBallParams!!.x = endX.toInt()
                        floatBall!!.setDragState(false)
                        windowManager!!.updateViewLayout(floatBall, floatBallParams)
                        //如果初始落点与松手落点的坐标差值超过6个像素，则拦截该点击事件
                        //否则继续传递，将事件交给OnClickListener函数处理
                        if (Math.abs(endX - tempX) > 6 && Math.abs(endY - tempY) > 6) {
                            return true
                        }
                    }
                }
                return false
            }
        }
        val clickListener = object : View.OnClickListener {

            override fun onClick(v: View) {
                Log.d(TAG, "开始打卡...")
                Log.d(TAG, "启动和飞信首页")
                AppUtils.startThirdActivity(context, "com.chinasofti.rcs", "com.chinasofti.rcs.action.RcsMessageActivity")
                DakaManager.startDaka()
            }
        }
        floatBall!!.setOnTouchListener(touchListener)
        floatBall!!.setOnClickListener(clickListener)
    }

    //显示浮动小球
    fun showFloatBall() {
        if (floatBallParams == null) {
            floatBallParams = LayoutParams()
            floatBallParams!!.width = floatBall!!.mWidth
            floatBallParams!!.height = floatBall!!.mHeight - statusHeight
            floatBallParams!!.gravity = Gravity.TOP or Gravity.LEFT
            val mPoint = Point()
            windowManager!!.defaultDisplay.getSize(mPoint)
            floatBallParams!!.x = mPoint.x - floatBall!!.mWidth
            floatBallParams!!.y = mPoint.y  shr 1
            if (Build.VERSION.SDK_INT >= 26) {
                floatBallParams!!.type = 2038     // WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                floatBallParams!!.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
            floatBallParams!!.flags = LayoutParams.FLAG_NOT_FOCUSABLE or LayoutParams.FLAG_NOT_TOUCH_MODAL
            floatBallParams!!.format = PixelFormat.RGBA_8888
            windowManager!!.addView(floatBall, floatBallParams)
        }
    }

    companion object {

        private var manager: ViewManager? = null

        //获取ViewManager实例
        fun getInstance(context: Context): ViewManager {
            if (manager == null) {
                manager = ViewManager(context)
            }
            return manager as ViewManager
        }
    }
}