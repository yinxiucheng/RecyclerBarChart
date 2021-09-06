package com.yxc.barchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.yxc.barchart.R;
import com.yxc.chartlib.util.CanvasUtil;
import com.yxc.chartlib.util.DecimalUtil;
import com.yxc.chartlib.util.RoundRectType;
import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.DisplayUtil;

/**
 * @author yxc
 * @date 2019-09-27
 */
public class ThreeTargetView extends View {

    public static final int ANGLE = 0;
    private Context mContext;
    private Paint circlePaint;
    private int transParentValue;

    public ThreeTargetView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public ThreeTargetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public ThreeTargetView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        float itemWidth = width / 6.5f;
        float padding = DisplayUtil.dip2px(2);
        float spaceWidth = itemWidth / 7.5f;
//        float paintWidth = itemWidth - spaceWidth;
        float paintWidth = DisplayUtil.dip2px(2);
//        drawCircleBg(canvas, width, height, padding, itemWidth, paintWidth, spaceWidth);
        drawCircle(canvas, width, height, padding, itemWidth, paintWidth, spaceWidth);
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
        transParentValue = (int) (255 * 0.5);
        firstColor = ColorUtil.getResourcesColor(mContext, R.color.rainbow_color1);
        secondColor = ColorUtil.getResourcesColor(mContext, R.color.rainbow_color2);
        thirdColor = ColorUtil.getResourcesColor(mContext, R.color.rainbow_color3);
    }

    private void drawCircle(Canvas canvas, int width, int height, float padding,
                            float itemWidth, float paintWidth, float spaceWidth) {
        canvas.save();
//        int[] colors = new int[]{firstColor, secondColor, thirdColor, secondColor, firstColor};
//        SweepGradient sweepGradient = new SweepGradient(width / 2, width / 2, colors, null);
//        circlePaint.setShader(sweepGradient);
        circlePaint.setColor(firstColor);
//        circlePaint.setAlpha(transParentValue);
//        drawWrapperCircle(canvas, width, height, spaceWidth);
        drawCenterCircle(canvas, width, height, itemWidth, spaceWidth);
//        drawInnerCircle(canvas, width, height, itemWidth, spaceWidth);
//        drawWrapperRoundRect(canvas, width, height, itemWidth, spaceWidth);
//        drawInnerRoundRect(canvas, width, height, itemWidth, spaceWidth);

        canvas.restore();
    }

    private void drawWrapperCircle(Canvas canvas, int width, int height, float spaceWidth){
        RectF rectFWrapper = new RectF(0, 0, width, height);
        RectF rectFInner = new RectF(spaceWidth, spaceWidth,
                width - spaceWidth, height - spaceWidth);
        float centerX = width/2;
        float centerY = height/2;
        Path path = createCircle(rectFWrapper, rectFInner,
                180 + 2.3f, 180 - 4.6f, centerX, centerY);
        canvas.drawPath(path, circlePaint);
    }

    private void drawCenterCircle(Canvas canvas, int width, int height, float itemWidth, float spaceWidth){

        RectF rectFWrapper = new RectF(spaceWidth, spaceWidth, width - spaceWidth, height - spaceWidth);
        RectF rectFInner = new RectF(itemWidth - spaceWidth, itemWidth - spaceWidth, width - itemWidth + spaceWidth,
                height - itemWidth + spaceWidth);

        float centerX = width/2;
        float centerY = height/2;

        Path path = createCircle(rectFWrapper, rectFInner, 180, 90, centerX, centerY);

        int[] colors = new int[]{firstColor, secondColor, thirdColor, secondColor, firstColor};
        SweepGradient sweepGradient = new SweepGradient(width / 2, width / 2, colors, null);
        circlePaint.setShader(sweepGradient);
        canvas.drawPath(path, circlePaint);
//        circlePaint.setColor(thirdColor);
//        canvas.drawArc(rectFInner, 180, 90, true, circlePaint);
//        Path path = createCircle(rectFWrapper, rectFInner, 180, 90);
//        canvas.drawPath(path, circlePaint);
    }

    private void drawInnerCircle(Canvas canvas, int width, int height, float itemWidth, float spaceWidth){
        RectF rectFWrapper = new RectF(itemWidth - spaceWidth, itemWidth - spaceWidth,
                width - itemWidth + spaceWidth, height - itemWidth + spaceWidth);
        float centerX = width/2;
        float centerY = height/2;
        RectF rectFInner = new RectF(itemWidth, itemWidth, width - itemWidth, height - itemWidth);
        Path path = createCircle(rectFWrapper, rectFInner,
                180 + 3.15f, 180 - 6.3f, centerX, centerY);
        canvas.drawPath(path, circlePaint);
    }

    private void drawInnerRoundRect(Canvas canvas, int width, int height, float itemWidth, float spaceWidth){
        RectF leftRectF = new RectF(itemWidth - spaceWidth, height/2 - spaceWidth,
                itemWidth + DisplayUtil.dip2px(0.13f), height/2);
        Path roundLeftRectF = CanvasUtil.createRectRoundPath(leftRectF, spaceWidth, RoundRectType.TYPE_RIGHT_BOTTOM);
        canvas.drawPath(roundLeftRectF, circlePaint);

        RectF rightRectF = new RectF(width - itemWidth, height/2-spaceWidth, width - itemWidth + spaceWidth, height/2);
        Path roundRightRectF = CanvasUtil.createRectRoundPath(rightRectF, spaceWidth, RoundRectType.TYPE_LEFT_BOTTOM);
        canvas.drawPath(roundRightRectF, circlePaint);
    }

    private void drawWrapperRoundRect(Canvas canvas, int width, int height, float itemWidth, float spaceWidth){
        RectF leftRectF = new RectF(0, height/2 - spaceWidth,
                spaceWidth , height/2);
        Path roundLeftRectF = CanvasUtil.createRectRoundPath(leftRectF, spaceWidth, RoundRectType.TYPE_LEFT_BOTTOM);
        canvas.drawPath(roundLeftRectF, circlePaint);

        RectF rightRectF = new RectF(width - spaceWidth, height/2-spaceWidth, width, height/2);
        Path roundRightRectF = CanvasUtil.createRectRoundPath(rightRectF, spaceWidth + 1, RoundRectType.TYPE_RIGHT_BOTTOM);
        canvas.drawPath(roundRightRectF, circlePaint);
    }

    private Path createCircle(RectF rectFWrapper, RectF rectFInner,
                              float startAngle, float sweepAngle, float centerX, float centerY){
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
