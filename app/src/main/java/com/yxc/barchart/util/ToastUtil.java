/**
 * 
 */
package com.yxc.barchart.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapException;


public class ToastUtil {
	
	private static Toast mToast;

	private static Handler mHandler = new Handler();
	private static Runnable r = new Runnable() {
		public void run() {
			if(mToast != null) {
				mToast.cancel();
				mToast = null;// toast隐藏后，将其置为null
			}
		}
	};

	public static void showShortToast(Context context, String message) {
		TextView text = new TextView(context);// 显示的提示文字
		text.setText(message);
		text.setBackgroundColor(Color.BLACK);
		text.setPadding(10, 10, 10, 10);

		if (mToast != null) {// 
			mHandler.postDelayed(r, 0);//隐藏toast
		} else {
			mToast = new Toast(context);
			mToast.setDuration(Toast.LENGTH_SHORT);
			mToast.setGravity(Gravity.BOTTOM, 0, 150);
			mToast.setView(text);
		}
		
		mHandler.postDelayed(r, 1000);// 延迟1秒隐藏toast
		mToast.show();
	}

	public static void show(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}

	public static void show(Context context, int info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}
	
    private static void logError(String info, int errorCode) {
        print(LINE);//start
        print("                                   错误信息                                     ");
        print(LINE);//title
        print(info);
        print("错误码: " + errorCode);
        print("                                                                               ");
        print("如果需要更多信息，请根据错误码到以下地址进行查询");
        print("  http://lbs.amap.com/api/android-sdk/guide/map-tools/error-code/");
        print("如若仍无法解决问题，请将全部log信息提交到工单系统，多谢合作");
        print(LINE);//end
    }

    //log
    public static final String TAG = "AMAP_ERROR";
    static final String LINE_CHAR="=";
    static final String BOARD_CHAR="|";
    static final int LENGTH = 80;
    static  String LINE;
    static{
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<LENGTH;i++){
            sb .append(LINE_CHAR);
        }
        LINE = sb.toString();
    }


    private static void printLog(String s){
        if(s.length()<LENGTH-2){
            StringBuilder sb = new StringBuilder();
            sb.append(BOARD_CHAR).append(s);
            for(int i = 0 ;i <LENGTH-2-s.length();i++){
                sb.append(" ");
            }
            sb.append(BOARD_CHAR);
            print(sb.toString());
        }else{
            String line = s.substring(0,LENGTH-2);
            print(BOARD_CHAR+line+BOARD_CHAR);
            printLog(s.substring(LENGTH-2));
        }
    }

    private static void print(String s) {
        Log.i(TAG,s);
    }
}
