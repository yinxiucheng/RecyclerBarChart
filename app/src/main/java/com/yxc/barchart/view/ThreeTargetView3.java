package com.yxc.barchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.yxc.barchart.R;
import com.yxc.chartlib.util.CanvasUtil;
import com.yxc.chartlib.util.RoundRectType;
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
//        float paintWidth = DisplayUtil.dip2px(2);
//        drawCircleBg(canvas, width, height, padding, itemWidth, paintWidth, spaceWidth);
        drawCircle(canvas, width, height, itemWidth, spaceWidth);
    }

    private float firstPercent = 0;
    private float secondPercent = 0;
    private float thirdPercent = 0;
    private int firstColor = -1;
    private int secondColor = -1;
    private int thirdColor = -1;
    private float centerX;
    private float centerY;

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setStrokeCap(Paint.Cap.BUTT);
        transParentValue = (int) (255 * 0.4);
        firstColor = ColorUtil.getResourcesColor(mContext, R.color.rainbow_color1);
        secondColor = ColorUtil.getResourcesColor(mContext, R.color.rainbow_color2);
        thirdColor = ColorUtil.getResourcesColor(mContext, R.color.rainbow_color3);
    }

    private void drawCircle(Canvas canvas, int width, int height, float itemWidth, float spaceWidth) {

//        int[] colors = new int[]{firstColor, secondColor, thirdColor, secondColor, firstColor};
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
        spaceWidth = spaceWidth * 2/3;
        drawWrapperCircle(canvas, rectFFirst, spaceWidth, 180 + 3.8f, 180 - 7.6f);
        drawCenterCircle(canvas, rectFFirst, itemWidth, spaceWidth, 180, 180);
        drawInnerCircle(canvas, rectFFirst, itemWidth, spaceWidth, 180 + 15f, 180 - 30f);
        drawWrapperRoundRect(canvas, rectFFirst, itemWidth, spaceWidth);
        drawInnerRoundRect(canvas, rectFFirst, itemWidth, spaceWidth);
        canvas.restore();
    }

    private void drawSecondCircle(Canvas canvas, int width, int height, float itemWidth, float spaceWidth) {
        canvas.save();
        RectF rectFFirst = new RectF(itemWidth + spaceWidth, itemWidth + spaceWidth,
                width - (itemWidth + spaceWidth), height - (itemWidth + spaceWidth));
        canvas.translate(rectFFirst.left, rectFFirst.top);
        circlePaint.setColor(secondColor);
        circlePaint.setAlpha(transParentValue);
        drawWrapperCircle(canvas, rectFFirst, spaceWidth, 180 + 3f, 180 - 6f);
        drawCenterCircle(canvas, rectFFirst, itemWidth, spaceWidth, 180, 180);
        drawInnerCircle(canvas, rectFFirst, itemWidth, spaceWidth, 180 + 4.7f, 180 - 9.4f);
        drawWrapperRoundRect(canvas, rectFFirst, itemWidth, spaceWidth);
        drawInnerRoundRect(canvas, rectFFirst, itemWidth, spaceWidth);
        canvas.restore();
    }

    private void drawFirstCircle(Canvas canvas, int width, int height, float itemWidth, float spaceWidth) {
        canvas.save();
        circlePaint.setColor(firstColor);
        circlePaint.setAlpha(transParentValue);
        RectF rectFFirst = new RectF(0, 0, width, height);
        drawWrapperCircle(canvas, rectFFirst, spaceWidth, 180 + 2.05f, 180 - 4.1f);
        drawCenterCircle(canvas, rectFFirst, itemWidth, spaceWidth, 180, 180);
        drawInnerCircle(canvas, rectFFirst, itemWidth, spaceWidth, 180 + 2.7f, 180 - 5.4f);
        drawWrapperRoundRect(canvas, rectFFirst, itemWidth, spaceWidth);
        drawInnerRoundRect(canvas, rectFFirst, itemWidth, spaceWidth);
        canvas.restore();
    }

    private float reSize = DisplayUtil.dip2px(0.14285712688f);
//    private float reSize = DisplayUtil.dip2px(0.1429f);

    private void drawWrapperCircle(Canvas canvas, RectF rectF, float spaceWidth, float startAngle, float sweepAngle) {
        float width = rectF.width();
        float height = rectF.height();
        RectF rectFWrapper = new RectF(0, 0, width, height);
        RectF rectFInner = new RectF(spaceWidth + reSize, spaceWidth + reSize,
                width - spaceWidth - reSize, height - spaceWidth - reSize);
        Path path = createCircle(rectFWrapper, rectFInner, startAngle, sweepAngle, width/2, height/2);
        canvas.drawPath(path, circlePaint);
    }

    private void drawCenterCircle(Canvas canvas, RectF rectF, float itemWidth, float spaceWidth, float startAngle, float sweepAngle) {
        float width = rectF.width();
        float height = rectF.height();
        RectF rectFWrapper = new RectF(spaceWidth, spaceWidth, width - spaceWidth, height - spaceWidth);
        RectF rectFInner = new RectF(itemWidth - spaceWidth, itemWidth - spaceWidth, width - itemWidth + spaceWidth,
                height - itemWidth + spaceWidth);
        Path path = createCircle(rectFWrapper, rectFInner, startAngle, sweepAngle, width/2, height/2);
        canvas.drawPath(path, circlePaint);
    }

    private void drawInnerCircle(Canvas canvas, RectF rectF, float itemWidth, float spaceWidth, float startAngle, float sweepAngle) {
        float width = rectF.width();
        float height = rectF.height();
        RectF rectFWrapper = new RectF(itemWidth - spaceWidth - reSize, itemWidth - spaceWidth - reSize,
                width - itemWidth + spaceWidth + reSize, height - itemWidth + spaceWidth + reSize);
        RectF rectFInner = new RectF(itemWidth, itemWidth, width - itemWidth, height - itemWidth);
        Path path = createCircle(rectFWrapper, rectFInner, startAngle, sweepAngle, width/2, height/2);
        canvas.drawPath(path, circlePaint);
    }

    private void drawInnerRoundRect(Canvas canvas, RectF rectF, float itemWidth, float spaceWidth) {
        float width = rectF.width();
        float height = rectF.height();
        RectF leftRectF = new RectF(itemWidth - spaceWidth - reSize, height / 2 - spaceWidth - reSize,
                itemWidth + DisplayUtil.dip2px(0.13f), height / 2);
        Path roundLeftRectF = CanvasUtil.createRectRoundPath(leftRectF, spaceWidth, RoundRectType.TYPE_RIGHT_BOTTOM);
        canvas.drawPath(roundLeftRectF, circlePaint);

        RectF rightRectF = new RectF(width - itemWidth, height / 2 - spaceWidth, width - itemWidth + spaceWidth + reSize, height / 2);
        Path roundRightRectF = CanvasUtil.createRectRoundPath(rightRectF, spaceWidth, RoundRectType.TYPE_LEFT_BOTTOM);
        canvas.drawPath(roundRightRectF, circlePaint);
    }

    private void drawWrapperRoundRect(Canvas canvas, RectF rectF, float itemWidth, float spaceWidth) {
        float width = rectF.width();
        float height = rectF.height();
        RectF leftRectF = new RectF(0, height / 2 - spaceWidth - reSize,
                spaceWidth + reSize, height / 2);
        Path roundLeftRectF = CanvasUtil.createRectRoundPath(leftRectF, spaceWidth, RoundRectType.TYPE_LEFT_BOTTOM);
        canvas.drawPath(roundLeftRectF, circlePaint);

        RectF rightRectF = new RectF(width - spaceWidth - reSize, height / 2 - spaceWidth, width, height / 2);
        Path roundRightRectF = CanvasUtil.createRectRoundPath(rightRectF, spaceWidth, RoundRectType.TYPE_RIGHT_BOTTOM);
        canvas.drawPath(roundRightRectF, circlePaint);
    }


    private Path createCircle(RectF rectFWrapper, RectF rectFInner, float startAngle, float sweepAngle, float centerX, float centerY) {
        Path path = new Path();
        path.addArc(rectFWrapper, startAngle, sweepAngle);
        path.lineTo(centerX, centerY);
        path.close();
        Path clipPath = new Path();
        clipPath.addArc(rectFInner, startAngle, sweepAngle);
        clipPath.lineTo(centerX, centerY);
        clipPath.close();
        path.op(clipPath, Path.Op.DIFFERENCE);
        return path;
    }

}
