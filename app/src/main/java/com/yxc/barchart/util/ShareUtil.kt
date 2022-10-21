package com.yxc.barchart.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.util.LruCache
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.yxc.barchart.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * @author yxc
 * @since 2019-11-19
 */
object ShareUtil {
    internal const val SHARE_DIR = "share"
    internal const val TEMP_DIR = "temp"
    fun getScrollViewHeight(scrollView: NestedScrollView): Int {
        var height = 0
        for (i in 0 until scrollView.childCount) {
            height += scrollView.getChildAt(i).height
        }
        //height 必须大于0
        return height
    }

    @JvmOverloads
    @JvmStatic
    fun loadBitmapFromView(
        v: View,
        @ColorInt bgColor: Int = Color.TRANSPARENT,
        extraHeight: Int = 0
    ): Bitmap {
        val b = Bitmap.createBitmap(
            v.measuredWidth,
            v.measuredHeight + extraHeight,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        c.drawColor(bgColor)
        c.translate(0f, extraHeight.toFloat())
        v.draw(c)
        return b
    }

    fun shotScrollView(scrollView: NestedScrollView): Bitmap? {
        var height = 0
        for (i in 0 until scrollView.childCount) {
            height += scrollView.getChildAt(i).height
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"))
        }
        //height 必须大于0
        val bitmap: Bitmap = Bitmap.createBitmap(scrollView.width, height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)
        return bitmap
    }

    fun shotView(scrollView: ViewGroup, color: Int): Bitmap? {
        var height = 0
        var bitmap: Bitmap? = null
        for (i in 0 until scrollView.childCount) {
            height += scrollView.getChildAt(i).height
            scrollView.getChildAt(i).setBackgroundColor(color)
        }
        //height 必须大于0
        bitmap = Bitmap.createBitmap(scrollView.width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)
        return bitmap
    }

    fun shotRecyclerViewLinear(activity: Activity, recyclerView: RecyclerView) {
        val bitmap = createHorizontalRecyclerView(activity, recyclerView)
//        saveImageToGallery(activity, bitmap)
    }

    private fun createVerticalRecyclerView(context: Context?, recyclerView: RecyclerView): Bitmap? {
        //获取设置的adapter
        val adapter = recyclerView.adapter
        //创建保存截图的bitmap
        var bigBitmap: Bitmap? = null
        if (adapter != null) {
            //获取item的数量
            val size = adapter.itemCount
            //recycler的完整高度 用于创建bitmap时使用
            var height = 0
            //获取最大可用内存
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

            // 使用1/8的缓存
            val cacheSize = maxMemory / 8
            //把每个item的绘图缓存存储在LruCache中
            val bitmapCache = LruCache<String, Bitmap>(cacheSize)
            for (i in 0 until size) {
                //手动调用创建和绑定ViewHolder方法，
                val holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
                adapter.onBindViewHolder(holder, i)

                //测量
                holder.itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(
                        recyclerView.width,
                        View.MeasureSpec.EXACTLY
                    ), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                //布局
                holder.itemView.layout(
                    0,
                    0,
                    holder.itemView.measuredWidth,
                    holder.itemView.measuredHeight
                )
                //开启绘图缓存
                val drawingCache = Bitmap.createBitmap(
                    holder.itemView.measuredWidth,
                    holder.itemView.measuredHeight,
                    Bitmap.Config.RGB_565
                )
                val canvas = Canvas(drawingCache!!)
                canvas.drawColor(ContextCompat.getColor(context!!, R.color.white))
                holder.itemView.draw(canvas)
                if (drawingCache != null) {
                    bitmapCache.put(i.toString(), drawingCache)
                }
                //获取itemView的实际高度并累加
                height += holder.itemView.measuredHeight
            }

            //根据计算出的recyclerView高度创建bitmap
            bigBitmap = Bitmap.createBitmap(recyclerView.measuredWidth, height, Bitmap.Config.RGB_565)
            //创建一个canvas画板
            val canvas = Canvas(bigBitmap)
            //当前bitmap的高度
            var top = 0
            var left = 0
            //画笔
            val paint = Paint()
            for (i in 0 until size) {
                val bitmap = bitmapCache[i.toString()]
                canvas.drawBitmap(bitmap, left.toFloat(), top.toFloat(), paint)
                left = 0
                top += bitmap.height
            }
        }
        return bigBitmap
    }


    @JvmStatic
    fun createHorizontalRecyclerView(context: Context?, recyclerView: RecyclerView): Bitmap? {
        //获取设置的adapter
        val adapter = recyclerView.adapter
        //创建保存截图的bitmap
        var bigBitmap: Bitmap? = null
        if (adapter != null) {
            //获取item的数量
            val size = adapter.itemCount
            //recycler的完整高度 用于创建bitmap时使用
            var width = 0
            //获取最大可用内存
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
            // 使用1/8的缓存
            val cacheSize = maxMemory / 8
            //把每个item的绘图缓存存储在LruCache中
            val bitmapCache = LruCache<String, Bitmap>(cacheSize)
            for (i in 0 until size) {
                //手动调用创建和绑定ViewHolder方法，
                val holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
                adapter.onBindViewHolder(holder, i)

                //测量
                holder.itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(
                        recyclerView.height,
                        View.MeasureSpec.EXACTLY
                    )
                )
                //布局
                holder.itemView.layout(
                    0,
                    0,
                    holder.itemView.measuredWidth,
                    holder.itemView.measuredHeight
                )
                //开启绘图缓存
                val drawingCache = Bitmap.createBitmap(
                    holder.itemView.measuredWidth,
                    holder.itemView.measuredHeight,
                    Bitmap.Config.RGB_565
                )
                val canvas = Canvas(drawingCache)
                canvas.drawColor(ContextCompat.getColor(context!!, R.color.white))
                holder.itemView.draw(canvas)
                bitmapCache.put(i.toString(), drawingCache)
                //获取itemView的实际高度并累加
                width += holder.itemView.measuredWidth
            }
            //根据计算出的recyclerView高度创建bitmap
            bigBitmap = Bitmap.createBitmap(width, recyclerView.measuredHeight, Bitmap.Config.RGB_565)
            //创建一个canvas画板
            val canvas = Canvas(bigBitmap)
            //当前bitmap的高度
            var top = 0
            var left = 0
            //画笔
            val paint = Paint()
            for (i in 0 until size) {
                val bitmap = bitmapCache[i.toString()]
                canvas.drawBitmap(bitmap, left.toFloat(), top.toFloat(), paint)
                top = 0
                left += bitmap.width
            }
        }
        return bigBitmap
    }

    //保存文件到指定路径
    @JvmStatic
    fun saveImageToGallery(activity: Activity, bmp: Bitmap?, coroutineScope: LifecycleCoroutineScope?){
        val storePath = BaseFileUtil.getShareDirPath()
        val appDir = File(storePath)
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        coroutineScope?.launch {
            val uriTask = async { saveImage(bmp, appDir, fileName, SHARE_DIR) }
            withContext(Dispatchers.Main){
                val uri = uriTask.await()
                if (uri != null) {
//                   activity.toastShort(R.string.share_save_success)
                    //保存图片后发送广播通知更新数据库
                    activity.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
                }else{
//                    ToastUtil.showShortToast(R.string.share_save_fail)
                }
            }
        }
    }

    @JvmStatic
    fun saveImageToGallery(activity: AppCompatActivity, bmp: Bitmap?){
        if (activity.isDestroyed){
            return
        }
        saveImageToGallery(activity, bmp, activity.lifecycleScope)
    }

    //保存图片到缓存目录

    private fun saveImage(
        bmp: Bitmap?,
        appDir: File?,
        fileName: String?,
        dir: String
    ): Uri? {
        val file = File(appDir, fileName)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            //通过io流的方式来压缩保存图片
            val isSuccess = bmp!!.compress(Bitmap.CompressFormat.JPEG, 80, fos)
            fos.flush()
            fos.close()
            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(activity.getContentResolver(), file.getAbsolutePath(), fileName, null);
            val uri = Uri.fromFile(file)
            return if (isSuccess) uri else null
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (null != fos) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

}