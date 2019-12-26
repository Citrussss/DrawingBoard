package com.sure.drawingboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

open class BaseSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {
    protected var mSurfaceHolder: SurfaceHolder? = null
    protected var mCanvas: Canvas? = null
    protected var isRunning = false;

    init {
        init()
    }

    private fun init() {
        mSurfaceHolder = holder
        mSurfaceHolder!!.addCallback(this)
        isFocusable = true
        isFocusableInTouchMode = true
        this.keepScreenOn = true
        //设置下面两句话让黑板变白板
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        println("=========surfaceCreated========")
        Thread(Runnable {
            isRunning = true
            while (isRunning) {
                draw()
                Thread.sleep(10)
            }
        }).start()
    }

    /**
     * 子类重写该方法去绘制
     */
    protected open fun draw() {
        try {
            println("============draw========")
            mCanvas = mSurfaceHolder!!.lockCanvas()
//            mCanvas!!.drawCircle(500f, 500f, 300f, paint!!)
//            mCanvas!!.drawCircle(100f, 100f, 20f, paint!!)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (mCanvas != null)
                mSurfaceHolder!!.unlockCanvasAndPost(mCanvas)
        }

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        println("=========surfaceChanged========")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        println("=========surfaceDestroyed========")
    }
}
