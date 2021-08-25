package com.yxc.barchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
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
public class RainbowView extends View {

    public static final int ANGLE = 0;
    private Context mContext;
    private Paint circlePaint;
    private int transParentValue;

    public RainbowView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public RainbowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public RainbowView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        int height = getHeight();
        int width = getWidth();
        drawThreeCircle(canvas, width, height);
    }

    private void drawThreeCircle(Canvas canvas, int width, int height){
        float itemWidth = width / 6.5f;
        float padding = DisplayUtil.dip2px(2);
        float spaceWidth = itemWidth / 7.5f;
//        float paintWidth = itemWidth - spaceWidth;
        float paintWidth = DisplayUtil.dip2px(2);
        drawCircleBg(canvas, width, height, padding, itemWidth, paintWidth, spaceWidth);
//        drawCircle(canvas, width, height, padding, itemWidth, paintWidth, spaceWidth);
    }

    private float firstPercent = 0;
    private float secondPercent = 0;
    private float thirdPercent = 0;
    private int firstColor = -1;
    private int secondColor = -1;
    private int thirdColor = -1;

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeCap(Paint.Cap.BUTT);
        transParentValue = (int) (255 * 0.8);
        firstColor = ColorUtil.getResourcesColor(mContext, R.color.rainbow_color1);
        secondColor = ColorUtil.getResourcesColor(mContext, R.color.rainbow_color2);
        thirdColor = ColorUtil.getResourcesColor(mContext, R.color.rainbow_color3);
    }

    private void drawCircleBg(Canvas canvas, int width, int height, float padding,
                            float itemWidth, float paintWidth, float spaceWidth) {
        canvas.save();
        int originalColor = circlePaint.getColor();
        RectF bgRectF = new RectF(padding, padding, width - padding, height - padding);
        RectF innerRectF = new RectF(padding + itemWidth, padding + itemWidth, width - padding - itemWidth, height - padding - itemWidth);
        RainbowModel rainbowModel = new RainbowModel();
        rainbowModel.generalCommonModelBg(rainbowModel, bgRectF, innerRectF, 0.015f, 0.015f);
        Path path = new Path();
        path.moveTo(rainbowModel.point8.x, rainbowModel.point8.y);
        path.lineTo(rainbowModel.point1.x, rainbowModel.point1.y);
        path.addPath(rainbowModel.pathArc1);
        path.addPath(rainbowModel.pathArc2);
        path.addPath(rainbowModel.pathArc3);
        path.lineTo(rainbowModel.point5.x, rainbowModel.point5.y);
        path.addPath(rainbowModel.pathArc4);
        path.addPath(rainbowModel.pathArc5);
        path.addPath(rainbowModel.pathArc6);
        circlePaint.setColor(firstColor);
        circlePaint.setAlpha(transParentValue);
        circlePaint.setStrokeWidth(paintWidth);
        canvas.drawPath(path, circlePaint);
        circlePaint.setColor(originalColor);
        canvas.restore();
    }

}
