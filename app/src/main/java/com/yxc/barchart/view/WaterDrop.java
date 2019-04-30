package com.yxc.barchart.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
public class WaterDrop extends FrameLayout {

    BezierCircle water1;
    BezierCircle water2;
    BezierCircle water3;
    BezierCircle water4;
    BezierCircle water5;
    BezierCircle water6;
    BezierCircle water7;
    BezierCircle water8;


    AnimatorSet mAnimatorLevelSet;
    AnimatorSet mAnimatorScanSet;

    Animator wrapAnimator;

    int radius = DisplayUtil.dip2px(24);
    Context mContext;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    // 移除所有的msg.what为0等消息，保证只有一个循环消息队列再跑
                    handler.removeMessages(0);
                    mAnimatorScanSet.start();
                    wrapAnimator.start();
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

    public WaterDrop(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public WaterDrop(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public WaterDrop(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        initBgView();
        initWaterDropView();
        initAnimationLevelSet();
        initWrapAnimator();
        startLevelAnimator();
    }

    public void initBgView() {
        this.addWaterColorful(8, R.color.water_n_drop8);
        this.addWaterColorful(7, R.color.water_n_drop7);
        this.addWaterColorful(6, R.color.water_n_drop6);
        this.addWaterColorful(5, R.color.water_n_drop5);
        this.addWaterColorful(4, R.color.water_n_drop4);
        this.addWaterColorful(3, R.color.water_n_drop3);
        this.addWaterColorful(2, R.color.water_n_drop2);
        this.addWaterColorful(1, R.color.water_n_drop1);
    }

    public void initWaterDropView() {
        water1 = createWaterDrop(1, R.color.water_drop1);
        water2 = createWaterDrop(2, R.color.water_drop2);
        water3 = createWaterDrop(3, R.color.water_drop3);
        water4 = createWaterDrop(4, R.color.water_drop4);
        water5 = createWaterDrop(5, R.color.water_drop5);
        water6 = createWaterDrop(6, R.color.water_drop6);
        water7 = createWaterDrop(7, R.color.water_drop7);
        water8 = createWaterDrop(8, R.color.water_drop8);
    }


    private void initWrapAnimator() {
        LinearInterpolator timeInterpolator = new LinearInterpolator();
        wrapAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        wrapAnimator.setDuration(2000);
        wrapAnimator.setInterpolator(timeInterpolator);
    }


    public void resetWaterDrop() {
        removeView(water1);
        removeView(water2);
        removeView(water3);
        removeView(water4);
        removeView(water5);
        removeView(water6);
        removeView(water7);
        removeView(water8);
    }

    //层变动画
    public void startLevelAnimator() {
//        water1.setAlpha(0.f);
        addView(water1);
        water2.setAlpha(0.f);
        addView(water2);
        water3.setAlpha(0.f);
        addView(water3);
        water4.setAlpha(0.f);
        addView(water4);
        water5.setAlpha(0.f);
        addView(water5);
        water6.setAlpha(0.f);
        addView(water6);
        water7.setAlpha(0.f);
        addView(water7);
        water8.setAlpha(0.f);
        addView(water8);
        mAnimatorLevelSet.start();
    }


    private ObjectAnimator createLevelAnimator(final BezierCircle water1, long delay) {
        LinearInterpolator timeInterpolator = new LinearInterpolator();
        ObjectAnimator waterAlpha = ObjectAnimator.ofFloat(water1, "alpha", 0f, 0.5f, 0.2f, 1f);
        waterAlpha.setDuration(3000);
        waterAlpha.setStartDelay(delay);
        waterAlpha.setInterpolator(timeInterpolator);
        return waterAlpha;
    }

    //层变动画
    public void initAnimationLevelSet() {
        mAnimatorLevelSet = new AnimatorSet();
//        ObjectAnimator water1Alpha = createLevelAnimator(water1, 0);
        ObjectAnimator water2Alpha = createLevelAnimator(water2, 0 * 2000);
        ObjectAnimator water3Alpha = createLevelAnimator(water3, 1 * 2000);
        ObjectAnimator water4Alpha = createLevelAnimator(water4, 2 * 2000);
        ObjectAnimator water5Alpha = createLevelAnimator(water5, 3 * 2000);
        ObjectAnimator water6Alpha = createLevelAnimator(water6, 4 * 2000);
        ObjectAnimator water7Alpha = createLevelAnimator(water7, 5 * 2000);
        ObjectAnimator water8Alpha = createLevelAnimator(water8, 6 * 2000);
        List<Animator> list = new ArrayList<>();
//        list.add(water1Alpha);
        list.add(water2Alpha);
        list.add(water3Alpha);
        list.add(water4Alpha);
        list.add(water5Alpha);
        list.add(water6Alpha);
        list.add(water7Alpha);
        list.add(water8Alpha);
        mAnimatorLevelSet.playTogether(list);
        mAnimatorLevelSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                handler.sendEmptyMessage(1);
            }
        });
    }

    //扫光动画
    public void addWaterColorful(int number, int colorResource) {
        addView(createWaterDrop(number, colorResource));
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
