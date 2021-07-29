package com.yxc.barchart.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.yxc.barchart.R;
import com.yxc.commonlib.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxc
 * @since 2019/4/28
 */
public class Rainbow extends FrameLayout {

    RainbowCircle rainbowCircle1;
    RainbowCircle rainbowCircle2;
    RainbowCircle rainbowCircle3;
    RainbowCircle rainbowCircle4;
    AnimatorSet mAnimatorLevelSet;
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

    public Rainbow(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public Rainbow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public Rainbow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        initWaterDropView();
        initAnimationLevelSet();
        startLevelAnimator();
    }

    public void initWaterDropView() {
        int itemWidth = DisplayUtil.dip2px(40);
        int spaceWidth = DisplayUtil.dip2px(8);
        rainbowCircle1 = createWaterDrop(1, DisplayUtil.dip2px(10), 0, R.color.transparent);
        rainbowCircle2 = createWaterDrop(2,  spaceWidth + itemWidth, spaceWidth, R.color.rainbow_color3);
        rainbowCircle3 = createWaterDrop(3, 2 * (spaceWidth + itemWidth), 2 * spaceWidth + itemWidth, R.color.rainbow_color2);
        rainbowCircle4 = createWaterDrop(4, 3 * (spaceWidth + itemWidth), 2 * (spaceWidth + itemWidth) + spaceWidth, R.color.rainbow_color1);
    }

    public void resetWaterDrop() {
        removeView(rainbowCircle1);
        removeView(rainbowCircle2);
        removeView(rainbowCircle3);
        removeView(rainbowCircle4);
    }

    //层变动画
    public void startLevelAnimator() {
//        water1.setAlpha(0.f);
        addView(rainbowCircle1);
        rainbowCircle2.setAlpha(0.f);
        addView(rainbowCircle2);
        rainbowCircle3.setAlpha(0.f);
        addView(rainbowCircle3);
        rainbowCircle4.setAlpha(0.f);
        addView(rainbowCircle4);
        mAnimatorLevelSet.start();
    }

    private ObjectAnimator createLevelAnimator(final RainbowCircle water1, long delay) {
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
        ObjectAnimator water2Alpha = createLevelAnimator(rainbowCircle2, 0 * 2000);
        ObjectAnimator water3Alpha = createLevelAnimator(rainbowCircle3, 1 * 2000);
        ObjectAnimator water4Alpha = createLevelAnimator(rainbowCircle4, 2 * 2000);
        List<Animator> list = new ArrayList<>();

        list.add(water2Alpha);
        list.add(water3Alpha);
        list.add(water4Alpha);
        mAnimatorLevelSet.playTogether(list);
        mAnimatorLevelSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                handler.sendEmptyMessage(1);
            }
        });
    }

    public RainbowCircle createWaterDrop(int number,int radius,int clipRadius, int colorResource) {
        RainbowCircle bezierCircle = new RainbowCircle(mContext, radius, clipRadius, number, colorResource);
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
