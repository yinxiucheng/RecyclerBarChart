package com.yxc.barchart.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.yxc.barchart.R;
import com.yxc.commonlib.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxc
 * @since 2019/4/28
 */
public class WaterDropScan extends FrameLayout {
    BezierCircle waterScan1;
    BezierCircle waterScan2;
    BezierCircle waterScan3;
    BezierCircle waterScan4;
    BezierCircle waterScan5;
    BezierCircle waterScan6;
    BezierCircle waterScan7;
    BezierCircle waterScan8;
    AnimatorSet mAnimatorScanSet;

    int radius = DisplayUtil.dip2px(24);
    Context mContext;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    // 移除所有的msg.what为0等消息，保证只有一个循环消息队列再跑
                    handler.removeMessages(0);
                    mAnimatorScanSet.start();
                    // app的功能逻辑处理
                    // 再次发出msg，循环更新
                    handler.sendEmptyMessageDelayed(0, 2000);
                    break;
                case 1:
                    // 直接移除，定时器停止
                    handler.removeMessages(0);
                    break;

                default:
                    break;
            }
        }

        ;
    };

    public WaterDropScan(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public WaterDropScan(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public WaterDropScan(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        initWaterScanView();
        initAnimationScanSet();
        startScanAnimator();
    }

    public void initWaterScanView() {
        waterScan1 = createWaterDrop(1, R.color.water_drop1);
        waterScan2 = createWaterDrop(2, R.color.water_drop2);
        waterScan3 = createWaterDrop(3, R.color.water_drop3);
        waterScan4 = createWaterDrop(4, R.color.water_drop4);
        waterScan5 = createWaterDrop(5, R.color.water_drop5);
        waterScan6 = createWaterDrop(6, R.color.water_drop6);
        waterScan7 = createWaterDrop(7, R.color.water_drop7);
        waterScan8 = createWaterDrop(8, R.color.water_drop8);
    }


    public void resetWaterScan() {
        removeView(waterScan1);
        removeView(waterScan2);
        removeView(waterScan3);
        removeView(waterScan4);
        removeView(waterScan5);
        removeView(waterScan6);
        removeView(waterScan7);
        removeView(waterScan8);
    }

    //层变动画
    public void startScanAnimator() {
        waterScan1.setAlpha(0.f);
        addView(waterScan1);
        waterScan2.setAlpha(0.f);
        addView(waterScan2);
        waterScan3.setAlpha(0.f);
        addView(waterScan3);
        waterScan4.setAlpha(0.f);
        addView(waterScan4);
        waterScan5.setAlpha(0.f);
        addView(waterScan5);
        waterScan6.setAlpha(0.f);
        addView(waterScan6);
        waterScan7.setAlpha(0.f);
        addView(waterScan7);
        waterScan8.setAlpha(0.f);
        addView(waterScan8);
        handler.sendEmptyMessageDelayed(0, 2000);
    }


    //层变动画
    private ObjectAnimator createScanAnimator(BezierCircle water1, long delay, long duration, float start, float middle, float end) {
        LinearInterpolator timeInterpolator = new LinearInterpolator();
        ObjectAnimator water1Alpha = ObjectAnimator.ofFloat(water1, "alpha", start, middle, end);
        water1Alpha.setDuration(duration);
        water1Alpha.setStartDelay(delay);
        water1Alpha.setInterpolator(timeInterpolator);
        return water1Alpha;
    }


    //扫光动画
    public void initAnimationScanSet() {
        mAnimatorScanSet = new AnimatorSet();
        ObjectAnimator water1Alpha = createScanAnimator(waterScan2, 0 , 700, 0f, 1.0f, 0f);
        ObjectAnimator water2Alpha = createScanAnimator(waterScan3, 233, 630, 0f, 0.8f, 0f);
        ObjectAnimator water3Alpha = createScanAnimator(waterScan4, 383, 630, 0f, 0.55f, 0f);
        ObjectAnimator water4Alpha = createScanAnimator(waterScan5, 533, 650, 0f, 0.5f, 0f);
        ObjectAnimator water5Alpha = createScanAnimator(waterScan6, 667, 650, 0f, 0.45f, 0f);
        ObjectAnimator water6Alpha = createScanAnimator(waterScan7, 816, 567, 0f, 0.35f, 0f);
        ObjectAnimator water7Alpha = createScanAnimator(waterScan8, 983, 433, 0f, 0.30f, 0f);

        List<Animator> list = new ArrayList<>();
        list.add(water1Alpha);
        list.add(water2Alpha);
        list.add(water3Alpha);
        list.add(water4Alpha);
        list.add(water5Alpha);
        list.add(water6Alpha);
        list.add(water7Alpha);

        mAnimatorScanSet.playTogether(list);
    }

    public BezierCircle createWaterDrop(int number, int colorResource) {
        BezierCircle bezierCircle = new BezierCircle(mContext, radius, number, colorResource);
        return bezierCircle;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
