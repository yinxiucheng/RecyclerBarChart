package com.yxc.barchartlib.view;

import android.content.Context;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yxc.barchartlib.R;
import com.yxc.barchartlib.component.YAxis;
import com.yxc.barchartlib.entrys.BarChart;
import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.component.XAxis;
import com.yxc.barchartlib.util.BarChartAttrs;
import com.yxc.barchartlib.util.ChartComputeUtil;
import com.yxc.barchartlib.util.DisplayUtil;

import java.util.List;

/**
 * @author yxc
 * @date 2019/4/6
 */
final public class BarChartAdapter extends RecyclerView.Adapter<BarChartAdapter.BarChartViewHolder> {

    Context mContext;
    List<BarEntry> mEntries;
    RecyclerView mRecyclerView;
    XAxis mXAxis;
    YAxis mYAxis;
    BarChartAttrs mBarChartAttrs;

    BarEntry selectBarEntry;

    public BarChartAdapter(Context context, List<BarEntry> entries, RecyclerView recyclerView, YAxis mYAxis, BarChartAttrs attrs) {
        this.mContext = context;
        this.mEntries = entries;
        this.mRecyclerView = recyclerView;
        this.mYAxis = mYAxis;
        this.mBarChartAttrs = attrs;
    }

    public void setXAxis(XAxis mXAxis) {
        this.mXAxis = mXAxis;
        notifyDataSetChanged();
    }

    public void setEntries(List<BarEntry> mEntries) {
        this.mEntries = mEntries;
        notifyDataSetChanged();
    }

    public List<BarEntry> getEntries() {
        return mEntries;
    }

    @NonNull
    @Override
    public BarChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_barchart, parent, false);
        BarChartViewHolder viewHolder = new BarChartViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BarChartViewHolder viewHolder, int position) {
        float contentWidth = (DisplayUtil.getScreenWidth(mContext) - mRecyclerView.getPaddingRight() - mRecyclerView.getPaddingLeft());

        int itemWidth = (int) (contentWidth / mBarChartAttrs.displayNumbers);
        int reminderWidth = (int) (contentWidth % mBarChartAttrs.displayNumbers);

        //todo 这里只画右边，所以调整多余的只加在右边。 会造成重绘时，月的抖动，因为来回变动,
        // todo没有限定一定要显示一周，一天、一年、一月的数据是，不用重新设定这个padding，多显示一点没事。
        if (position > mEntries.size() - 2 && mBarChartAttrs.enableScrollToScale) {//加载更多的时候，最后一次绘制item时 再改RecyclerView的padding
            resetRecyclerPadding(reminderWidth);
        }
        setLinearLayout(viewHolder.contentView, itemWidth);
        bindBarEntryToView(viewHolder, position, contentWidth);
    }

    private void bindBarEntryToView(BarChartViewHolder viewHolder, final int position, float contentWidth) {
        final BarEntry barEntry = mEntries.get(position);
        float itemWidth = contentWidth/mBarChartAttrs.displayNumbers;
        Log.d("BarChartAdapter", " mYAxis:" + mYAxis.getAxisMaximum());
//        float width = viewHolder.contentView.getWidth();
        float barSpaceWidth = itemWidth * mBarChartAttrs.barSpace;
        float realBottomPadding = mRecyclerView.getPaddingBottom() + mBarChartAttrs.contentPaddingBottom;
        float realTopPadding = mRecyclerView.getPaddingTop() + mBarChartAttrs.maxYAxisPaddingTop;
        float realContentHeight = mRecyclerView.getHeight() - realBottomPadding - realTopPadding;
        float barChartWidth = itemWidth - barSpaceWidth;//柱子的宽度
        float height = barEntry.getY() / mYAxis.getAxisMaximum() * realContentHeight;
        CustomAnimatedDecorator drawable = new CustomAnimatedDecorator(barChartWidth, realContentHeight,
                0, realContentHeight - height);
        barEntry.setDrawable(drawable);

        viewHolder.contentView.setTag(barEntry);

        viewHolder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectBarEntry == barEntry) {
                    selectBarEntry = null;
                    barEntry.isSelected = false;
                } else {
                    if (null != selectBarEntry){
                        selectBarEntry.isSelected = false;
                    }
                    selectBarEntry = barEntry;
                    barEntry.isSelected = true;
                }
                notifyItemChanged(position, false);
            }
        });
    }

    private void resetRecyclerPadding(int reminderWidth) {
        if (mBarChartAttrs.enableLeftYAxisLabel && mBarChartAttrs.enableRightYAxisLabel) {
            mRecyclerView.setPadding(mRecyclerView.getPaddingLeft() + reminderWidth / 2, mRecyclerView.getPaddingTop(),
                    mRecyclerView.getPaddingRight() + reminderWidth / 2, mRecyclerView.getPaddingBottom());
        } else if (mBarChartAttrs.enableRightYAxisLabel) {
            mRecyclerView.setPadding(mRecyclerView.getPaddingLeft(), mRecyclerView.getPaddingTop(),
                    mRecyclerView.getPaddingRight() + reminderWidth, mRecyclerView.getPaddingBottom());
        } else if (mBarChartAttrs.enableLeftYAxisLabel) {
            mRecyclerView.setPadding(mRecyclerView.getPaddingLeft() + reminderWidth, mRecyclerView.getPaddingTop(),
                    mRecyclerView.getPaddingRight(), mRecyclerView.getPaddingBottom());
        }
    }

    @Override
    public int getItemViewType(int position) {
        BarEntry barEntry = mEntries.get(position);
        if (null != barEntry) {
            return barEntry.type;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    /**
     * * 设置每个色块宽度
     *
     * @param contentView 对应需要设置LinearLayout
     * @param itemWidth   对应的宽度
     */
    private void setLinearLayout(View contentView, int itemWidth) {
        ViewGroup.LayoutParams lp;
        lp = contentView.getLayoutParams();
        lp.width = itemWidth;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        contentView.setLayoutParams(lp);
    }

    class BarChartViewHolder extends RecyclerView.ViewHolder {
        View contentView;

        public BarChartViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView = itemView;
        }
    }

    public void setYAxis(YAxis mYAxis) {
        this.mYAxis = mYAxis;
        notifyDataSetChanged();
    }

}
