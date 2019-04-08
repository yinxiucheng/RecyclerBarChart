package com.yxc.barchart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author yxc
 * @date 2019/4/6
 */
public class BarChartAdapter extends RecyclerView.Adapter<BarChartAdapter.BarChartViewHolder> {

    Context mContext;

    public List<BarEntry> getEntries() {
        return mEntries;
    }

    public void setEntries(List<BarEntry> mEntries) {
        this.mEntries = mEntries;
    }

    public int getDisplayChartNumbers() {
        return displayChartNumbers;
    }

    List<BarEntry> mEntries;

    RecyclerView mRecyclerView;

    YAxis mYAxis;

    public int displayChartNumbers;

    public BarChartAdapter(Context context, List<BarEntry> entries, RecyclerView recyclerView) {
        this.mContext = context;
        this.mEntries = entries;
        this.mRecyclerView = recyclerView;
    }

    public BarChartAdapter(Context context, List<BarEntry> entries, int displayChartNumbers) {
        this.mContext = context;
        this.mEntries = entries;
        this.displayChartNumbers = displayChartNumbers;
    }

    public void setDisplayChartNumbers(int displayChartNumbers) {
        this.displayChartNumbers = displayChartNumbers;
        notifyDataSetChanged();
    }

    public YAxis getYAxis() {
        return mYAxis;
    }

    public void setYAxis(YAxis yAxis) {
        this.mYAxis = yAxis;
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
        //设置每个格子高度
        Log.d("BarChart", "onBindViewHolder");
        int contentWidth = (DisplayUtil.getScreenWidth(mContext) - mRecyclerView.getPaddingRight() - mRecyclerView.getPaddingLeft());
        setLinearLayout(viewHolder.contentView, contentWidth / displayChartNumbers);
        BarEntry barEntry = mEntries.get(position);

        BarEntry viewBarEntry = (BarEntry) viewHolder.contentView.getTag();
        viewBarEntry.value = barEntry.value;
        viewBarEntry.type = barEntry.type;
        viewBarEntry.timestamp = barEntry.timestamp;
        viewBarEntry.localDate = barEntry.localDate;


//        int parentHeight = mRecyclerView.getHeight() - DisplayUtil.dip2px(36);
//
//        ViewGroup.LayoutParams layoutParams = viewHolder.barChartItem.getLayoutParams();
//
//        layoutParams.height = (int) (barEntry.value / mYAxis.maxLabel * parentHeight);
//
//        layoutParams.width = viewHolder.barContainer.getWidth() * 2 / 3;
//        viewHolder.barChartItem.setBackgroundResource(R.drawable.shape_data_item_barchart_ee5971);
//
//        viewHolder.value.setText("" + barEntry.value);
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
     * @param itemWidth    对应的宽度
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
//        ViewGroup barContainer;
//        TextView value;
//        TextView barChartItem;

        public BarChartViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView = itemView;
            BarEntry barEntry = new BarEntry();
            contentView.setTag(barEntry);
//            barContainer = itemView.findViewById(R.id.fl_chart);
//            barChartItem = itemView.findViewById(R.id.barchartitem);
//            value = itemView.findViewById(R.id.bar_value);
        }
    }


}
