package com.yxc.barchart.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
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

    AnimatorSet mAnimatorLevelChangedSet;
    int radius = DisplayUtil.dip2px(24);
    Context mContext;

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
        mAnimatorLevelChangedSet.start();
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

    public void initWaterDropView(){
        water1 = createWaterDrop(1, R.color.water_drop1);
        water2 = createWaterDrop(2, R.color.water_drop2);
        water3 = createWaterDrop(3, R.color.water_drop3);
        water4 = createWaterDrop(4, R.color.water_drop4);
        water5 = createWaterDrop(5, R.color.water_drop5);
        water6 = createWaterDrop(6, R.color.water_drop6);
        water7 = createWaterDrop(7, R.color.water_drop7);
        water8 = createWaterDrop(8, R.color.water_drop8);
        addView(water1);
    }

    public void resetWaterDrop(){
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
    public void startAnimator(){
        addView(water1);
        mAnimatorLevelChangedSet.start();
    }

    private ObjectAnimator createLevelObjectAnimator(final BezierCircle water1,
                                                     final BezierCircle water2,
                                                     AccelerateDecelerateInterpolator mTimeInterpolator){
        ObjectAnimator waterAlpha = ObjectAnimator.ofFloat(water1, "alpha", 0f, 0.5f, 0.2f, 1f);
        waterAlpha.setDuration(1000);
        waterAlpha.setInterpolator(mTimeInterpolator);

        if (null != water2){
            waterAlpha.addListener(new SimpleAnimatorListener(){
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    addView(water2);
                }

            });
        }
        return waterAlpha;
    }

    //层变动画
    public void initAnimationLevelSet(){
        AccelerateDecelerateInterpolator mTimeInterpolator = new AccelerateDecelerateInterpolator();
        mAnimatorLevelChangedSet = new AnimatorSet();
        ObjectAnimator water1Alpha = createLevelObjectAnimator(water1, water2, mTimeInterpolator);
        ObjectAnimator water2Alpha = createLevelObjectAnimator(water2, water3, mTimeInterpolator);
        ObjectAnimator water3Alpha = createLevelObjectAnimator(water3, water4, mTimeInterpolator);
        ObjectAnimator water4Alpha = createLevelObjectAnimator(water4, water5, mTimeInterpolator);
        ObjectAnimator water5Alpha = createLevelObjectAnimator(water5, water6, mTimeInterpolator);
        ObjectAnimator water6Alpha = createLevelObjectAnimator(water6, water7, mTimeInterpolator);
        ObjectAnimator water7Alpha = createLevelObjectAnimator(water7, water8, mTimeInterpolator);
        ObjectAnimator water8Alpha = createLevelObjectAnimator(water8, null, mTimeInterpolator);
        List<Animator> list = new ArrayList<>();
        list.add(water1Alpha);
        list.add(water2Alpha);
        list.add(water3Alpha);
        list.add(water4Alpha);
        list.add(water5Alpha);
        list.add(water6Alpha);
        list.add(water7Alpha);
        list.add(water8Alpha);
        mAnimatorLevelChangedSet.playSequentially(list);
    }


    //扫光动画
    public void initScanAnimationSet(){

        AccelerateDecelerateInterpolator mTimeInterpolator = new AccelerateDecelerateInterpolator();
        mAnimatorLevelChangedSet = new AnimatorSet();

        ObjectAnimator water1Alpha = ObjectAnimator.ofFloat(water1, "alpha", 0f, 1f, 0f);
        water1Alpha.setDuration(250);
        water1Alpha.setStartDelay(0);
        water1Alpha.setInterpolator(mTimeInterpolator);

        ObjectAnimator water2Alpha = ObjectAnimator.ofFloat(water2, "alpha", 0f, 1f, 0f);
        water2Alpha.setDuration(250);
        water1Alpha.setStartDelay(1 * 100);
        water2Alpha.setInterpolator(mTimeInterpolator);

        ObjectAnimator water3Alpha = ObjectAnimator.ofFloat(water3, "alpha", 0f, 1f, 0f);
        water3Alpha.setDuration(250);
        water1Alpha.setStartDelay(2 * 100);
        water3Alpha.setInterpolator(mTimeInterpolator);

        ObjectAnimator water4Alpha = ObjectAnimator.ofFloat(water4, "alpha", 0f, 1f, 0f);
        water4Alpha.setDuration(250);
        water1Alpha.setStartDelay(3 * 100);
        water4Alpha.setInterpolator(mTimeInterpolator);

        ObjectAnimator water5Alpha = ObjectAnimator.ofFloat(water5, "alpha", 0f, 1f, 0f);
        water5Alpha.setDuration(250);
        water1Alpha.setStartDelay(4 * 100);
        water5Alpha.setInterpolator(mTimeInterpolator);

        ObjectAnimator water6Alpha = ObjectAnimator.ofFloat(water6, "alpha", 0f, 1f, 0f);
        water6Alpha.setDuration(250);
        water1Alpha.setStartDelay(5 * 100);
        water6Alpha.setInterpolator(mTimeInterpolator);

        ObjectAnimator water7Alpha = ObjectAnimator.ofFloat(water7, "alpha", 0f, 1f, 0f);
        water7Alpha.setDuration(250);
        water1Alpha.setStartDelay(6 * 100);
        water7Alpha.setInterpolator(mTimeInterpolator);

        ObjectAnimator water8Alpha = ObjectAnimator.ofFloat(water8, "alpha", 0f, 1f, 0f);
        water8Alpha.setDuration(250);
        water1Alpha.setStartDelay(7 * 100);
        water8Alpha.setInterpolator(mTimeInterpolator);

        List<Animator> list = new ArrayList<>();
        list.add(water1Alpha);
        list.add(water2Alpha);
        list.add(water3Alpha);
        list.add(water4Alpha);
        list.add(water5Alpha);
        list.add(water6Alpha);
        list.add(water7Alpha);
        list.add(water8Alpha);

        mAnimatorLevelChangedSet.playSequentially(list);
    }

    public void addWaterColorful(int number, int colorResource){
        addView(createWaterDrop(number, colorResource));
    }

    public BezierCircle createWaterDrop(int number, int colorResource){
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
