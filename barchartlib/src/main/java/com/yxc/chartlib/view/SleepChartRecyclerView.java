package com.yxc.chartlib.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yxc.chartlib.attrs.ChartAttrsUtil;
import com.yxc.chartlib.attrs.SleepChartAttrs;
import com.yxc.chartlib.util.Utils;
import com.yxc.commonlib.util.DisplayUtil;

/**
 * @author yxc
 * @since 2019/4/26
 */
public class SleepChartRecyclerView extends BaseChartRecyclerView {

    public final static int LONG_PRESS = 1;

    public SleepChartAttrs mAttrs;

    private int mLastMotionX;
    private int mLastMotionY;
    boolean isLongPress;
    boolean isMoved;
    Runnable mLongPressRunnable;
    private Handler handler;

    public SleepChartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mAttrs = ChartAttrsUtil.getSleepChartAttrs(context, attrs);
        mLongPressRunnable = new Runnable() {
            @Override
            public void run() {
                handler.sendMessage(handler.obtainMessage(LONG_PRESS));
            }
        };

        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case LONG_PRESS:
                        Log.d("SleepChartRecyclerView", "receive Message and isLongPress true!");
                        isLongPress = true;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }


    /**
     * 当父类需要处理点击事件时，需要重写父类的 OnTouch方法，拦截 ACTION_UP 事件
     * <p>
     * requestDisallowInterceptTouchEvent 解决跟父类的滑动冲突。
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("SleepChartRecyclerView", Utils.getActionName(event.getAction()));

        int x = (int) event.getX(0);
        int y = (int) event.getY(0);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                isLongPress = false;
                isMoved = false;
                mLastMotionX = x;
                mLastMotionY = y;
                Log.d("SleepChartRecyclerView", "Down And send Message!!");
                handler.postDelayed(mLongPressRunnable, 400);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isLongPress) {
                    Log.d("SleepChartRecyclerView", "parent get Action!");
                    getParent().requestDisallowInterceptTouchEvent(false);
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                if (isMoved)
                    break;
                if (Math.abs(mLastMotionX - x) > DisplayUtil.dip2px(3)
                        || Math.abs(mLastMotionY - y) > DisplayUtil.dip2px(3)) {
                    isMoved = true;
                    handler.removeCallbacks(mLongPressRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isLongPress = false;
                Log.d("", "ACTION_UP");
                handler.removeCallbacks(mLongPressRunnable);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}
