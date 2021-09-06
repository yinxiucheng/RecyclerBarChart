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
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        drawThreeCircle(canvas, width, height);
    }

    private void drawThreeCircle(Canvas canvas, int width, int height) {
        float itemWidth = width / 7f;
        float spaceWidth = itemWidth / 8f;

        //int[] colors = new int[]{firstColor, secondColor, thirdColor, secondColor, firstColor};
//        SweepGradient sweepGradient = new SweepGradient(width / 2, width / 2, colors, null);
//        circlePaint.setShader(sweepGradient);
        drawFirstCircle(canvas, width, height, itemWidth, spaceWidth);
        drawSecondCircle(canvas, width, height, itemWidth, spaceWidth);
        drawThirdCircle(canvas, width, height, itemWidth, spaceWidth);
    }

    private void drawThirdCircle(Canvas canvas, int width, int height, float itemWidth, float spaceWidth) {
        canvas.save();
        RectF rectFFirst = new RectF(2 * (itemWidth + spaceWidth), 2 * (itemWidth + spaceWidth),
                width - 2 * (itemWidth + spaceWidth), height - 2 * (itemWidth + spaceWidth));
        canvas.translate(rectFFirst.left, rectFFirst.top);
        reSize = DisplayUtil.dip2px(0.14285712689f);
        circlePaint.setColor(thirdColor);
        circlePaint.setAlpha(transParentValue);
        spaceWidth = spaceWidth * 2 / 3;
        ThreeTargetModel targetModel = new ThreeTargetModel(reSize, rectFFirst, itemWidth, spaceWidth,
                180 + 3.8f, 180 - 7.6f, 180,
                180 + 15f, 180 - 30f);
        targetModel.drawComponents(canvas, circlePaint);
        canvas.restore();
    }

    private void drawSecondCircle(Canvas canvas, int width, int height, float itemWidth, float spaceWidth) {
        canvas.save();
        RectF rectFFirst = new RectF(itemWidth + spaceWidth, itemWidth + spaceWidth,
                width - (itemWidth + spaceWidth), height - (itemWidth + spaceWidth));
        canvas.translate(rectFFirst.left, rectFFirst.top);
        circlePaint.setColor(secondColor);
        circlePaint.setAlpha(transParentValue);
        ThreeTargetModel targetModel = new ThreeTargetModel(reSize, rectFFirst, itemWidth, spaceWidth,
                180 + 3f, 180 - 6f, 180,
                180 + 4.7f, 180 - 9.4f);
        targetModel.drawComponents(canvas, circlePaint);
        canvas.restore();
    }

    private void drawFirstCircle(Canvas canvas, int width, int height, float itemWidth, float spaceWidth) {
        canvas.save();
        circlePaint.setColor(firstColor);
        circlePaint.setAlpha(transParentValue);
        RectF rectFFirst = new RectF(0, 0, width, height);
        ThreeTargetModel targetModel = new ThreeTargetModel(reSize, rectFFirst, itemWidth, spaceWidth,
                180 + 2.05f, 180 - 4.1f, 180,
                180 + 2.7f, 180 - 5.4f);
        targetModel.drawComponents(canvas, circlePaint);
        canvas.restore();
    }

    private float reSize = DisplayUtil.dip2px(0.14285712688f);
//    private float reSize = DisplayUtil.dip2px(0.1429f);
}
