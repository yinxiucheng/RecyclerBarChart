package com.yxc.barchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.yxc.barchart.R;
import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.DisplayUtil;

/**
 * @author yxc
 * @date 2019-09-27
 */
public class ThreeTargetView3 extends View {

    public static final int ANGLE = 0;
    private Context mContext;
    private Paint circlePaint;
    private int transParentValue;

    public ThreeTargetView3(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public ThreeTargetView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public ThreeTargetView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private float firstPercent = 0;
    private float secondPercent = 0;
    private float thirdPercent = 0;
    private int firstColor = -1;
    private int secondColor = -1;
    private int thirdColor = -1;

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setStrokeCap(Paint.Cap.BUTT);
        transParentValue = (int) (255 * 0.4);
        firstColor = ColorUtil.getResourcesColor(mContext, R.color.rainbow_color1);
        secondColor = ColorUtil.getResourcesColor(mContext, R.color.rainbow_color2);
        thirdColor = ColorUtil.getResourcesColor(mContext, R.color.rainbow_color3);
    }

    //固定宽高。
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, measuredWidth / 2);
    }

    int width;
    int height;
    float itemWidth;
    float spaceWidth;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = 2 * getHeight();
        itemWidth = width / 7f;
        spaceWidth = itemWidth / 8f;
        drawThreeCircle(canvas);
    }

    private void drawThreeCircle(Canvas canvas) {
        //int[] colors = new int[]{firstColor, secondColor, thirdColor, secondColor, firstColor};
//        SweepGradient sweepGradient = new SweepGradient(width / 2, width / 2, colors, null);
//        circlePaint.setShader(sweepGradient);
        drawFirstCircle(canvas);
        drawSecondCircle(canvas);
        drawThirdCircle(canvas);
    }

    private void drawThirdCircle(Canvas canvas) {
        canvas.save();
        RectF rectFFirst = new RectF(2 * (itemWidth + spaceWidth), 2 * (itemWidth + spaceWidth),
                width - 2 * (itemWidth + spaceWidth), height - 2 * (itemWidth + spaceWidth));
        canvas.translate(rectFFirst.left, rectFFirst.top);
        reSize = DisplayUtil.dip2px(0.14285712689f);
        circlePaint.setColor(thirdColor);
        circlePaint.setAlpha(transParentValue);
        spaceWidth = spaceWidth * 2 / 3;
        ThreeTargetModel targetModel = new ThreeTargetModel(reSize, rectFFirst, itemWidth, spaceWidth,
                3.8f, 15f, 180);
        targetModel.drawComponents(canvas, circlePaint);
        canvas.restore();
    }

    private void drawSecondCircle(Canvas canvas) {
        canvas.save();
        RectF rectF = new RectF(itemWidth + spaceWidth, itemWidth + spaceWidth,
                width - (itemWidth + spaceWidth), height - (itemWidth + spaceWidth));
        canvas.translate(rectF.left, rectF.top);
        circlePaint.setColor(secondColor);
        circlePaint.setAlpha(transParentValue);
        ThreeTargetModel targetModel = new ThreeTargetModel(reSize, rectF, itemWidth, spaceWidth,
                3f, 4.7f, 180);
        targetModel.drawComponents(canvas, circlePaint);
        canvas.restore();
    }

    private void drawFirstCircle(Canvas canvas) {
        canvas.save();
        circlePaint.setColor(firstColor);
        circlePaint.setAlpha(transParentValue);
        RectF rectF = new RectF(0, 0, width, height);
        ThreeTargetModel targetModel = new ThreeTargetModel(reSize, rectF, itemWidth, spaceWidth,
                2.05f, 2.7f, 180);
        targetModel.drawComponents(canvas, circlePaint);
        canvas.restore();
    }

    private float reSize = DisplayUtil.dip2px(0.14285712688f);
//    private float reSize = DisplayUtil.dip2px(0.1429f);
}
