package com.yxc.barchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.joda.time.LocalDate;

import java.util.List;


/**
 * @author yxc
 * @date 2019/4/6
 * <p>
 * 这个ItemDecoration 是BarChartAdapter专用的，里面直接用到了BarChartAdapter
 */
public class BarChartItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "DifWidthDecoration";

    private Context mContext;
    private int mOrientation;

    private Paint mDashPaint;
    private Paint mLinePaint;
    private Paint mTextPaint;
    private Paint mBarChartPaint;

    private BarChartAdapter mAdapter;
    private List<BarEntry> mEntries;
    private int contentPaddingBottom = DisplayUtil.dip2px(15);
    private int maxYAxisPaddingTop = DisplayUtil.dip2px(10);

    private YAxis mYAxis;

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    public BarChartItemDecoration(Context context, int orientation, YAxis yAxis) {
        this.mContext = context;
        this.mOrientation = orientation;
        this.mYAxis = yAxis;
        setOrientation(orientation);
        initPaint();
        initDathPaint();
        initTextPaint();
        initBarChartPaint();
    }

    public YAxis getYAxis() {
        return mYAxis;
    }

    public void setYAxis(YAxis mYAxis) {
        this.mYAxis = mYAxis;
    }

    //设置屏幕方向
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        this.mOrientation = orientation;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        mAdapter = (BarChartAdapter) parent.getAdapter();
        mEntries = mAdapter.getEntries();
        if (mOrientation == HORIZONTAL_LIST) {
            //横向 list 画竖线
            drawVerticalLine(c, parent, state);
            drawGridLine(c, parent, mYAxis);
            drawYAxisLabel(c, parent, mYAxis);
            drawBarChart(c, parent, state);
//            drawGridLine(c, parent, 4);
        } else if (mOrientation == VERTICAL_LIST) {
            //竖向list 画横线
            drawHorizontalLine(c, parent, state);
        }
    }

    //绘制柱状图
    private void drawBarChart(Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int bottom = parent.getHeight() - parent.getPaddingBottom() - contentPaddingBottom;
        int right = parent.getWidth() - parent.getPaddingRight();
        int realYAxisLabelHeight = bottom - maxYAxisPaddingTop;
        final int childCount = parent.getChildCount();

        View child;
        for (int i = 0; i < childCount; i++) {
            child = parent.getChildAt(i);
            BarEntry barEntry = (BarEntry) child.getTag();

            float lastHeight = barEntry.getCurrentHeight();

            int valueInt = (int) barEntry.value;
            ChartRectF rectF = new ChartRectF();
            int width = child.getWidth();
            int start = child.getLeft() + width / 6;
            int end = start + width * 2 / 3;
            int height = (int) (barEntry.value / mYAxis.maxLabel * realYAxisLabelHeight);
            int top = bottom - height;

            mTextPaint.setTextSize(DisplayUtil.sp2px(mContext, 10));
            String valueStr = Integer.toString(valueInt);

            float txtDistance = width - mTextPaint.measureText(valueStr);
            float txtX = child.getLeft();
            float txtY = top - DisplayUtil.dip2px(3);
            if (txtDistance > 0) {
                txtX = txtX + txtDistance / 2;
            }
            int txtStart = 0;
            int txtEnd = valueStr.length();

            int currentHeight = bottom - top;

            if (end < right) {
                rectF.set(start, top, end, bottom);
                canvas.drawRect(rectF, mBarChartPaint);

//                if (lastHeight == 0) {
//                    canvas.drawRect(rectF, mBarChartPaint);
//                } else {
//                    ObjectAnimator animator = ObjectAnimator.ofFloat(rectF, "top", lastHeight, currentHeight);
//                    animator.setDuration(1000);
//                    animator.start();
//
//                }
                canvas.drawText(valueStr, txtStart, txtEnd, txtX, txtY, mTextPaint);
            } else if (start < right) {//部分滑出的时候，处理柱状图，文字的显示
                int distance = (right - start);
                int rightInner = start + distance;
                rectF.set(start, top, rightInner, bottom);
                canvas.drawRect(rectF, mBarChartPaint);
//                if (lastHeight == 0) {
//
//                } else {
//                    ObjectAnimator animator = ObjectAnimator.ofFloat(rectF, "top", lastHeight, currentHeight);
//                    animator.setDuration(1000);
//                    animator.start();
//                    canvas.drawRect(rectF, mBarChartPaint);
//                }
                txtEnd = valueStr.length() * (rightInner - child.getLeft()) / width;
                canvas.drawText(valueStr, txtStart, txtEnd, txtX, txtY, mTextPaint);
            }
            //保存当前的高度
            barEntry.setCurrentHeight(currentHeight);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
    }

    private void initPaint() {
        mLinePaint = new Paint();
        mLinePaint.reset();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(1);
        mLinePaint.setColor(Color.GRAY);
    }

    private void initDathPaint() {
        mDashPaint = new Paint();
        mDashPaint.reset();
        mDashPaint.setAntiAlias(true);
        mDashPaint.setStyle(Paint.Style.STROKE);
        mDashPaint.setStrokeWidth(1);
        mDashPaint.setColor(Color.GRAY);
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setTextSize(DisplayUtil.sp2px(mContext, 14));
    }

    private void initBarChartPaint() {
        mBarChartPaint = new Paint();
        mBarChartPaint.reset();
        mBarChartPaint.setAntiAlias(true);
        mBarChartPaint.setStyle(Paint.Style.FILL);
        mBarChartPaint.setColor(ColorUtil.getResourcesColor(mContext, R.color.pink));
    }

    private void drawHorizontalLine(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
//        Log.d(TAG, String.format("childCount -> %d", childCount));
        //由于RecyclerViwe 复用ItemView 这里的childCount 是用户可见的 count
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            //接上面:所以要拿到itemView 对应的adapter position,但是方法的参数里不像getItemOffsets()里回调了View view
            //所以 自己取到child 通过RecyclerView 的 getChildAdapterPosition(child) 来取到adapterPosition
            //旧的写法 parent.getAdapter().getItemType(i) -> 会造成 分割线的错乱
            int adapterPosition = parent.getChildAdapterPosition(child);
            int type = mAdapter.getItemViewType(adapterPosition);

            //拿到child 的布局信息
            final RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) child.getLayoutParams();
            final int y = child.getBottom() + params.bottomMargin;

            if (type == BarEntry.TYPE_FIRST || type == BarEntry.TYPE_SPECIAL) {//画实线
                mLinePaint.setColor(ColorUtil.getResourcesColor(mContext, R.color.black));
                Path path = new Path();
                path.moveTo(left, y);
                path.lineTo(right, y);
                canvas.drawPath(path, mLinePaint);
                if (type == BarEntry.TYPE_SPECIAL) {

                }
            } else if (type == BarEntry.TYPE_SECOND) {//画组虚线
                PathEffect pathEffect = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                mDashPaint.setPathEffect(pathEffect);
                mDashPaint.setColor(ColorUtil.getResourcesColor(mContext, R.color.black_80_transparent));
                Path path = new Path();
                path.moveTo(left, y);
                path.lineTo(right, y);
                canvas.drawPath(path, mDashPaint);
            } else if (type == BarEntry.TYPE_THIRD) {//画child虚线
                PathEffect pathEffect = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                mDashPaint.setPathEffect(pathEffect);
                mDashPaint.setColor(ColorUtil.getResourcesColor(mContext, R.color.black_30_transparent));
                Path path = new Path();
                path.moveTo(left, y);
                path.lineTo(right, y);
                canvas.drawPath(path, mDashPaint);
            }
        }
    }

    //绘制 Y轴刻度线
    private void drawGridLine(Canvas canvas, RecyclerView parent, YAxis yAxis) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        mLinePaint.setColor(ColorUtil.getResourcesColor(mContext, R.color.black_20_transparent));
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int distance = bottom - contentPaddingBottom - maxYAxisPaddingTop;
        int lineNums = yAxis.labelSize;
        int lineDistance = distance / lineNums;
        int gridLine = top + maxYAxisPaddingTop;
        for (int i = 0; i <= lineNums; i++) {
            if (i > 0) {
                gridLine = gridLine + lineDistance;
            }
            Path path = new Path();
            path.moveTo(left, gridLine);
            path.lineTo(right, gridLine);
            canvas.drawPath(path, mLinePaint);
        }
    }

    private void drawYAxisLabel(Canvas canvas, RecyclerView parent, YAxis yAxis) {
        int right = parent.getWidth();
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int distance = bottom - contentPaddingBottom - (top + maxYAxisPaddingTop);
        int max = yAxis.maxLabel;
        int lineNums = yAxis.labelSize;
        int lineDistance = distance / lineNums;
        int label = max;
        mTextPaint.setTextSize(DisplayUtil.sp2px(mContext, 11));
        String maxStr = Integer.toString(max);
        float textWidth = mTextPaint.measureText(maxStr) + maxYAxisPaddingTop;
        parent.setPadding(0, 0, (int) textWidth, 0);

        int labelDistance = max / lineNums;
        int gridLine = top + DisplayUtil.dip2px(10);
        for (int i = 0; i <= lineNums; i++) {
            if (i > 0) {
                gridLine = gridLine + lineDistance;
                label = label - labelDistance;
            }
            String labelStr = Integer.toString(label);
            canvas.drawText(labelStr, right - parent.getPaddingRight() + DisplayUtil.dip2px(2),
                    gridLine + DisplayUtil.dip2px(3), mTextPaint);
        }
    }

    //绘制网格 纵轴线
    private void drawVerticalLine(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        mTextPaint.setTextSize(DisplayUtil.sp2px(mContext, 14));
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int adapterPosition = parent.getChildAdapterPosition(child);
            int type = parent.getAdapter().getItemViewType(adapterPosition);
            final RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) child.getLayoutParams();
            final int x = child.getRight();
            if (x > right) {//超出的时候就不要画了
                return;
            }
            BarEntry barEntry = mEntries.get(adapterPosition);
            LocalDate localDate = barEntry.localDate;
            String dateStr = localDate.getDayOfMonth() + "日";

            if (type == BarEntry.TYPE_FIRST || type == BarEntry.TYPE_SPECIAL) {
                if (type == BarEntry.TYPE_SPECIAL) {
                    canvas.drawText(dateStr, x - DisplayUtil.dip2px(3) - mTextPaint.measureText(dateStr),
                            bottom - DisplayUtil.dip2px(1), mTextPaint);
                }
                boolean isNextSecondType = isNextEntrySecondType(adapterPosition);
                mLinePaint.setColor(ColorUtil.getResourcesColor(mContext, R.color.black));
                Path path = new Path();
                if (isNextSecondType) {
                    path.moveTo(x, bottom - contentPaddingBottom);
                } else {
                    path.moveTo(x, bottom);
                }
                path.lineTo(x, top);
                canvas.drawPath(path, mLinePaint);
            } else if (type == BarEntry.TYPE_SECOND) {
                //拿到child 的布局信息
                PathEffect pathEffect = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                mDashPaint.setPathEffect(pathEffect);
                mDashPaint.setColor(ColorUtil.getResourcesColor(mContext, R.color.black_60_transparent));
                Path path = new Path();
                path.moveTo(x, bottom - DisplayUtil.dip2px(1));
                path.lineTo(x, top);
                canvas.drawPath(path, mDashPaint);
                canvas.drawText(dateStr, x - DisplayUtil.dip2px(3) - mTextPaint.measureText(dateStr), bottom - DisplayUtil.dip2px(1), mTextPaint);
            } else if (type == BarEntry.TYPE_THIRD) {
                //拿到child 的布局信息
                PathEffect pathEffect = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                mDashPaint.setPathEffect(pathEffect);
                mDashPaint.setColor(ColorUtil.getResourcesColor(mContext, R.color.black_30_transparent));
                Path path = new Path();
                path.moveTo(x, bottom - contentPaddingBottom);
                path.lineTo(x, top);
                canvas.drawPath(path, mDashPaint);
            }
        }
    }

    //画月线的时候，下一组、下下组需要写日期。
    private boolean isNextEntrySecondType(int adapterPosition) {
        BarEntry barEntryNext;
        boolean isNextSecondType = false;
        if (adapterPosition + 1 < mEntries.size()) {
            barEntryNext = mEntries.get(adapterPosition + 1);
            if (barEntryNext.type == BarEntry.TYPE_SECOND) {
                isNextSecondType = true;
            }
        }
        if (adapterPosition + 2 < mEntries.size()) {
            barEntryNext = mEntries.get(adapterPosition + 2);
            if (barEntryNext.type == BarEntry.TYPE_SECOND) {
                isNextSecondType = true;
            }
        }
        return isNextSecondType;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, 0);
    }
}
