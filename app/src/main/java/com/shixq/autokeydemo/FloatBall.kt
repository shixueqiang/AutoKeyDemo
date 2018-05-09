package com.shixq.autokeydemo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


/**
 * Created with shixq.
 * Description:
 * Date: 2018-05-08
 * Time: 下午5:14
 */
class FloatBall : View {

    var mWidth = 150

    var mHeight = 150
    //默认显示的文本
    private val text = "打卡"
    //是否在拖动
    private var isDrag: Boolean = false

    private var ballPaint: Paint? = null

    private var textPaint: Paint? = null

    private var bitmap: Bitmap? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context) {
        ballPaint = Paint()
        ballPaint!!.setColor(Color.GRAY)
        ballPaint!!.setAntiAlias(true)

        textPaint = Paint()
        textPaint!!.setTextSize(25.0f)
        textPaint!!.setColor(Color.WHITE)
        textPaint!!.setAntiAlias(true)
        textPaint!!.setFakeBoldText(true)

        val src = BitmapFactory.decodeStream(context.assets.open("ic_launcher_round.png"))
        //将图片裁剪到指定大小
        bitmap = Bitmap.createScaledBitmap(src!!, mWidth, mHeight, true)
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mWidth, mHeight)
    }

    protected override fun onDraw(canvas: Canvas) {
        if (!isDrag) {
            canvas.drawCircle(mWidth / 2.0f, mHeight / 2.0f, mWidth / 2.0f, ballPaint)
            val textWidth = textPaint!!.measureText(text)
            val fontMetrics = textPaint!!.getFontMetrics()
            val dy = -(fontMetrics.descent + fontMetrics.ascent) / 2
            canvas.drawText(text, mWidth / 2 - textWidth / 2, mHeight / 2 + dy, textPaint)
        } else {
            //正在被拖动时则显示指定图片
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, null)
        }
    }

    //设置当前移动状态
    fun setDragState(isDrag: Boolean) {
        this.isDrag = isDrag
        invalidate()
    }
}