package com.yxc.barchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

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

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setStrokeCap(Paint.Cap.BUTT);
        transParentValue = ThreeTargetConstant.TRANSPARENT_VALUE;
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
//       SweepGradient sweepGradient = new SweepGradient(width / 2, width / 2, colors, null);
//       circlePaint.setShader(sweepGradient);
        drawThreeCircleBg(canvas);
        drawCircle(canvas, ThreeTargetConstant.TARGET_FIRST_TYPE, 180 * 0.5f, spaceWidth);
        drawCircle(canvas, ThreeTargetConstant.TARGET_SECOND_TYPE, 180 * 0.3f, spaceWidth);
        drawCircle(canvas, ThreeTargetConstant.TARGET_THIRD_TYPE, 180 * 0.7f, spaceWidth / 2.0f);
    }

    private void drawThreeCircleBg(Canvas canvas) {
        drawCircle(canvas, ThreeTargetConstant.TARGET_FIRST_TYPE, 180, spaceWidth, true);
        drawCircle(canvas, ThreeTargetConstant.TARGET_SECOND_TYPE, 180, spaceWidth, true);
        drawCircle(canvas, ThreeTargetConstant.TARGET_THIRD_TYPE, 180, spaceWidth / 2.0f, true);
    }
    private void drawCircle(Canvas canvas, int type, float sweepAngel, float spaceWidth) {
        drawCircle(canvas, type, sweepAngel, spaceWidth, false);
    }

    private void drawCircle(Canvas canvas, int type, float sweepAngel, float spaceWidth, boolean isBg) {
        canvas.save();
        RectF rectF = createTargetRectF(type);
        canvas.translate(rectF.left, rectF.top);
        ThreeTargetModel targetModel = ThreeTargetModel.createTargetModel(type, rectF, itemWidth, spaceWidth, sweepAngel);
        circlePaint.setColor(targetModel.getColor(mContext, type));
        if (isBg) {
            circlePaint.setAlpha(transParentValue);
        } else {
            circlePaint.setAlpha(255);
        }
        targetModel.drawComponents(canvas, circlePaint);
        canvas.restore();
    }

    private RectF createTargetRectF(int type) {
        int times = 0;
        if (type == ThreeTargetConstant.TARGET_THIRD_TYPE) {
            times = 2;
        } else if (type == ThreeTargetConstant.TARGET_SECOND_TYPE) {
            times = 1;
        }
        return new RectF(times * (itemWidth + spaceWidth), times * (itemWidth + spaceWidth),
                width - times * (itemWidth + spaceWidth), height - times * (itemWidth + spaceWidth));
    }
}
