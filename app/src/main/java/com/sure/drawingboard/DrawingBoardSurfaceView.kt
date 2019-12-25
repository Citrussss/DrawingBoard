package com.sure.drawingboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.*


/**
 * author pisa
 * date  2019/12/25
 * version 1.0
 * effect :
 */
class DrawingBoardSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseSurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {
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
            return true
        }
    })

    protected override fun draw() {
//        super.draw()
        synchronized(mSurfaceHolder!!) {
            try {
                println("============draw========${paths.size}")
                mCanvas = mSurfaceHolder!!.lockCanvas()
                for (path in ArrayList(paths)) {
                    mCanvas?.drawPath(path, paint)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (mCanvas != null)
                    mSurfaceHolder!!.unlockCanvasAndPost(mCanvas)
            }
        }
    }

    var preX = 0f
    var preY = 0f
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        gestureDetector.onTouchEvent(event)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> path = Path().apply {
                this.moveTo(event.x, event.y)
                paths.add(this)
//                preX = event.x
//                preY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(event.x, event.y)
//                Path().apply {
//                    this.moveTo(preX, preY)
//                    this.lineTo(event.x, event.y)
//                    preX = event.x
//                    preY = event.y
//                    paths.add(this)
//                }
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return true
    }

}