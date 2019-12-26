package com.sure.drawingboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceHolder
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange


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
    private val paths: ArrayList<PathBody> = ArrayList()
    private var path: Path = Path();
    private var paint: Paint = Paint().apply {
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
//                        paths.add(this)
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
                for (body in ArrayList(paths)) {
                    mCanvas?.drawPath(body.path, body.paint)
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
//                paths.add(this)
                paths.add(PathBody(paint, this))
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

    data class PathBody(val paint: Paint, val path: Path)

    /**
     * 设置粗细
     */
    public fun setThickness(@FloatRange(from = 0.0, to = 10.0) thickness: Float) {
        paint = Paint().apply {
            this.isAntiAlias = paint.isAntiAlias;
            this.strokeWidth = thickness;
            this.color = paint.color
            this.strokeCap = paint.strokeCap       // 线帽，即画的线条两端是否带有圆角，ROUND，圆角
            this.style = paint.style
        }
    }

    /**
     * 设置颜色
     */
    public fun setColor(@ColorInt color: Int) {
        paint = Paint().apply {
            this.isAntiAlias = paint.isAntiAlias;
            this.strokeWidth = paint.strokeWidth;
            this.color = color
            this.strokeCap = paint.strokeCap       // 线帽，即画的线条两端是否带有圆角，ROUND，圆角
            this.style = paint.style
        }
    }

    /**
     * 清空
     */
    public fun clear() {
        paths.clear()
    }

    /**
     * 撤销
     */
    public fun last() {
        if (paths.isNotEmpty()) {
            paths.removeAt(paths.size - 1)
        }
    }
}