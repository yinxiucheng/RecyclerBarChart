package com.yxc.barchart.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.util.logging.Logger

class ScrollHorizontalCapture(var recyclerView: RecyclerView, var fullPath: String) : RecyclerView.OnScrollListener() {
    private var scrollDistanceX = 0
    private var scrollDistanceY = 0
    private var endCaptureWidth = 0
    private var captureFlag = false
    private var bitmapCaches = mutableListOf<Bitmap>()

    fun start() {
        if (captureFlag) return

        captureFlag = true
        scrollDistanceX = 0
        scrollDistanceY = 0
        endCaptureWidth = 0
        bitmapCaches.clear()

        recyclerView.addOnScrollListener(this)

        endCaptureWidth = scrollDistanceX + getWidth()
        //开始截屏，截取完整的recyclerView
        capture(scrollDistanceX, endCaptureWidth)
    }

    fun stop() {
        if (!captureFlag) return

        recyclerView.removeOnScrollListener(this)
        captureFlag = false

        //停止截屏，截取底部的部分位置
        capture(endCaptureWidth, scrollDistanceX + getWidth())
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

        if (captureFlag && scrollDistanceX + getWidth() - endCaptureWidth > getWidth() / 2) {
            //截半屏
            capture(endCaptureWidth, endCaptureWidth + getWidth() / 2)
            endCaptureWidth += getWidth() / 2
        }
    }

    private fun getWidth() = recyclerView.width

    private fun getHeight() = recyclerView.height

    private fun capture(startWidth: Int, endWidth: Int) {
        if (endWidth <= startWidth) return

        recyclerView.isDrawingCacheEnabled = true
        recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        recyclerView.buildDrawingCache()
        val bitmap = recyclerView.drawingCache
        val leftWidth = scrollDistanceX
        Bitmap.createBitmap(bitmap, startWidth - leftWidth,0, endWidth - startWidth, getHeight()).run {
            bitmapCaches.add(this)
        }
        recyclerView.destroyDrawingCache()
        recyclerView.isDrawingCacheEnabled = false
    }

    private fun saveToLocal() {
        val tmpFile = File(fullPath)
        if (!tmpFile.parentFile.exists() && !tmpFile.parentFile.mkdirs()) {
            //the path is error
            Log.i("Capture", "can't create the full path: $fullPath")
            return
        }

        var distHeight = 0
        var distWight = 0
        bitmapCaches.forEach {
            distHeight = it.height
            distWight += it.width
        }
        val distBitmap = Bitmap.createBitmap(distWight, distHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(distBitmap)

        var tmpWidth = 0F
        bitmapCaches.forEach {
            canvas.drawBitmap(it, tmpWidth, 0F, null)
            tmpWidth += it.width
            it.recycle()
        }

        Log.i("Capture", "the path is: $fullPath Bitmap $distBitmap")
        FileOutputStream(fullPath).let {
            distBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            it.flush()
            it.close()
            distBitmap.recycle()
        }
    }
}