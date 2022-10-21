package com.yxc.barchart.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream

class ScrollCapture(var recyclerView: RecyclerView, var fullPath: String) : RecyclerView.OnScrollListener() {

    private var scrollDistanceX = 0
    private var scrollDistanceY = 0
    private var endCaptureHeight = 0
    private var captureFlag = false
    private var bitmapCaches = mutableListOf<Bitmap>()

    fun start() {
        if (captureFlag) return

        captureFlag = true
        scrollDistanceX = 0
        scrollDistanceY = 0
        endCaptureHeight = 0
        bitmapCaches.clear()

        recyclerView.addOnScrollListener(this)

        endCaptureHeight = scrollDistanceY + getHeight()
        //开始截屏，截取完整的recyclerView
        capture(scrollDistanceY, endCaptureHeight)
    }

    fun stop() {
        if (!captureFlag) return

        recyclerView.removeOnScrollListener(this)
        captureFlag = false

        //停止截屏，截取底部的部分位置
        capture(endCaptureHeight, scrollDistanceY + getHeight())
        //拼接bitmap并保存到本地
        saveToLocal()
    }

    fun toggle() {
        if (captureFlag) {
            stop()
        } else {
            start()
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        scrollDistanceX += dx
        scrollDistanceY += dy

        if (captureFlag && scrollDistanceY + getHeight() - endCaptureHeight > getHeight() / 2) {
            //截半屏
            capture(endCaptureHeight, endCaptureHeight + getHeight() / 2)
            endCaptureHeight += getHeight() / 2
        }
    }

    private fun getWidth() = recyclerView.width

    private fun getHeight() = recyclerView.height

    private fun capture(startHeight: Int, endHeight: Int) {
        if (endHeight <= startHeight) return

        recyclerView.isDrawingCacheEnabled = true
        recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        recyclerView.buildDrawingCache()
        val bitmap = recyclerView.drawingCache
        val topHeight = scrollDistanceY
        Bitmap.createBitmap(bitmap, 0, startHeight - topHeight, getWidth(), endHeight - startHeight).run {
            bitmapCaches.add(this)
        }
        recyclerView.destroyDrawingCache()
        recyclerView.isDrawingCacheEnabled = false
    }

    private fun saveToLocal() {
        val tmpFile = File(fullPath)
        if (!tmpFile.parentFile.exists() && !tmpFile.parentFile.mkdirs()) {
            //the path is error
            Log.e(javaClass.simpleName, "can't create the full path: $fullPath")
            return
        }

        var distHeight = 0
        var distWight = 0
        bitmapCaches.forEach {
            distHeight += it.height
            distWight = it.width
        }
        val distBitmap = Bitmap.createBitmap(distWight, distHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(distBitmap)

        var tmpHeight = 0F
        bitmapCaches.forEach {
            canvas.drawBitmap(it, 0F, tmpHeight, null)
            tmpHeight += it.height
            it.recycle()
        }
        FileOutputStream(fullPath).let {
            distBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            it.flush()
            it.close()
            distBitmap.recycle()
        }
    }
}