package com.sure.drawingboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * author pisa
 * date  2019/12/25
 * version 1.0
 * effect :
 */
class DrawingBoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val tag = "DrawingBoardView"
    val gestureDetector: GestureDetector = GestureDetector(this.context, object :
        GestureDetector.SimpleOnGestureListener() {
        var lastX = 0f
        var lastY = 0f
        override fun onDown(e: MotionEvent): Boolean {
            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    path = Path().apply {
                        this.moveTo(e.x, e.y)
                        paths.add(this)
                    }
                }
            }
            return false
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            path.lineTo(e2!!.x, e2.y)
            postInvalidate()
            return true
        }
    })

    init {
    }

    private val paths: ArrayList<Path> = ArrayList()
    private var path: Path = Path();
    private val paint: Paint = Paint().apply {
        this.isAntiAlias = true;
        this.strokeWidth = 10f;
        this.color = Color.BLUE
        this.strokeCap = Paint.Cap.BUTT;       // 线帽，即画的线条两端是否带有圆角，ROUND，圆角
        this.style = Paint.Style.STROKE
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    };

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        /*canvas?.drawLine(100f, 300f, 400f, 300f, paint);
        for (index in 1..100) {
            for (path in paths) {
                canvas?.drawPath(path, paint)
            }
        }*/
        for (path in paths) {
            canvas?.drawPath(path, paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
//        when (event?.action) {
//            MotionEvent.ACTION_DOWN -> path = Path().apply {
//                this.moveTo(event.x, event.y)
//                paths.add(path)
//            }
//            MotionEvent.ACTION_MOVE -> {
//                path.lineTo(event.x, event.y)
//            }
//            MotionEvent.ACTION_UP -> {
//            }
//        }
//        postInvalidate()
        return true
    }
}