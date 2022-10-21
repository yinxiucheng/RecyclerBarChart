package com.yxc.barchart.util;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;


/**
 * @author yjt
 * @since 19-4-1
 */
public class BaseFileUtil {

    protected static final String EXTERNAL_STORAGE_ROOT_DIRECTORY = "MiWatch";
    protected static final String EXTERNAL_STORAGE_LOG_PATH = "wearablelog";
    protected static final String SHARE_DIR = "share";
    protected static final String LOG_DIR = "log";
    protected static final String DUMP_DIR = "devicelog";
    protected static final String AMAP_DIR = "amap";
    public static final String EXTERNAL_DOWNLOAD_PATH = "/storage/emulated/0/Download/wearablelog";
    public static final String EXTERNAL_DOWNLOAD_WIWATCH_PATH = "/storage/emulated/0/Download/MiWatch";
    private static final String TAG = "BaseFileUtil";

    public static String getShareDirPath() {
        return getExternalStorageDirPath(SHARE_DIR);
    }

    public static String getExternalStorageDirPath(String dir) {
        return ensureDirectoryExist(getExternalStoragePublicDcimPath() + File.separator + dir);
    }

    private static String ensureDirectoryExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (!file.isDirectory()) {
                file.delete();
                file.mkdirs();
            }
        } else {
            file.mkdirs();
        }
        return path;
    }

    public static String getExternalStoragePublicDcimPath() {
        return ensureDirectoryExist(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()
                + File.separator + EXTERNAL_STORAGE_ROOT_DIRECTORY);
    }

    public static String getExternalStoragePublicVideoPath() {
        return ensureDirectoryExist(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath()
                + File.separator + EXTERNAL_STORAGE_ROOT_DIRECTORY);
    }

    public static String getExternalStorageRootPath(String dir) {
        return ensureDirectoryExist(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
                + File.separator + dir);
    }

//    public static void writeStringToFile(String filePath, String sourceStr, final boolean append) {
//        BufferedWriter bw = null;
//        try {
//            bw = new BufferedWriter(new FileWriter(filePath, append));
//            // 往已有的文件上添加字符串
//            bw.write(sourceStr);
//            bw.flush();
//        } catch (Exception e) {
//            Logger.e(TAG, "FileUtil FileNotFoundException exception!", e);
//        } finally {
//            IOUtils.closeSilently(bw);
//        }
//    }

//    public static void writeStringToFile(String filePath, String sourceStr) {
//        writeStringToFile(filePath, sourceStr, true);
//    }

//    public static boolean copyFile(InputStream input, String path) {
//        if (input == null || TextUtils.isEmpty(path)) return false;
//
//        File file = new File(path);
//
//        File parentFile = file.getParentFile();
//        if (!parentFile.exists() && !parentFile.mkdirs()) return false;
//
//        Logger.d(TAG, "copyFile parent " + parentFile.exists());
//        try (BufferedSource bufferedSource = Okio.buffer(Okio.source(input));
//             BufferedSink bufferedSink = Okio.buffer(Okio.sink(new File(path)))) {
//            bufferedSink.writeAll(bufferedSource);
//            bufferedSink.flush();
//            return true;
//        } catch (Exception e) {
//            Logger.e(TAG, "copyFile " + e.getMessage());
//        }
//        return false;
//    }

    /**
     * 从assets目录下拷贝整个文件夹，不管是文件夹还是文件都能拷贝
     *
     * @param context 上下文
     * @param inPath  文件目录，要拷贝的目录
     * @param outPath 目标文件夹位置如：/sdcrad/mydir
     **/
    public static boolean copyFiles(Context context, String inPath, String outPath) {
        Log.i(TAG, "copyFiles() inPath:" + inPath + ", outPath:" + outPath);
        String[] fileNames;
        try {// 获得Assets一共有几多文件
            fileNames = context.getAssets().list(inPath);
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
        if (fileNames == null) {
            return false;
        }
        if (fileNames.length > 0) {//如果是目录
            File fileOutDir = new File(outPath);
            if (fileOutDir.isFile()) {
                boolean ret = fileOutDir.delete();
                if (!ret) {
                    Log.e(TAG, "delete() FAIL:" + fileOutDir.getAbsolutePath());
                }
            }
            if (!fileOutDir.exists()) { // 如果文件路径不存在
                if (!fileOutDir.mkdirs()) { // 创建文件夹
                    Log.e(TAG, "mkdirs() FAIL:" + fileOutDir.getAbsolutePath());
                    return false;
                }
            }
            for (String fileName : fileNames) { //递归调用复制文件夹
                String inDir = inPath;
                String outDir = outPath + File.separator;
                if (!inPath.equals("")) { //空目录特殊处理下
                    inDir = inDir + File.separator;
                }
                copyFiles(context, inDir + fileName, outDir + fileName);
            }
            return true;
        } else {//如果是文件
            try {
                File fileOut = new File(outPath);
                if (fileOut.exists()) {
                    boolean ret = fileOut.delete();
                    if (!ret) {
                        Log.e(TAG, "delete() FAIL:" + fileOut.getAbsolutePath());
                    }
                }
                boolean ret = fileOut.createNewFile();
                if (!ret) {
                    Log.e(TAG, "createNewFile() FAIL:" + fileOut.getAbsolutePath());
                }
                FileOutputStream fos = new FileOutputStream(fileOut);
                InputStream is = context.getAssets().open(inPath);
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

//    public static String getAmapDirPath() {
//        return getExternalFilesDirPath(AMAP_DIR);
//    }

//    public static String getExternalFilesDirPath(String dir) {
//        File file = AppUtil.getApp().getExternalFilesDir(dir);
//        if (file != null) {
//            return file.getAbsolutePath();
//        }
//        return getFilesDirFile(dir);
//    }
//
//    public static String getFilesDirFile(String file) {
//        return AppUtil.getApp().getFilesDir().getAbsolutePath()
//                + File.separator + file;
//    }

}

