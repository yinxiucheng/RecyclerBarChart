package com.yxc.barchartlib.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yxc.barchartlib.R;
import com.yxc.barchartlib.component.BarEntry;
import com.yxc.barchartlib.component.XAxis;
import com.yxc.barchartlib.util.DisplayUtil;

import java.util.List;

/**
 * @author yxc
 * @date 2019/4/6
 */
public class BarChartAdapter extends RecyclerView.Adapter<BarChartAdapter.BarChartViewHolder> {

    Context mContext;

    List<BarEntry> mEntries;

    RecyclerView mRecyclerView;

    XAxis mXAxis;

    public BarChartAdapter(Context context, List<BarEntry> entries, RecyclerView recyclerView, XAxis xAxis) {
        this.mContext = context;
        this.mEntries = entries;
        this.mRecyclerView = recyclerView;
        this.mXAxis = xAxis;
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
//        Log.d("BarChart", "onBindViewHolder");
        int contentWidth = (DisplayUtil.getScreenWidth(mContext) - mRecyclerView.getPaddingRight() - mRecyclerView.getPaddingLeft());
        setLinearLayout(viewHolder.contentView, contentWidth / mXAxis.displayNumbers);
        BarEntry barEntry = mEntries.get(position);

        BarEntry viewBarEntry = (BarEntry) viewHolder.contentView.getTag();
        viewBarEntry.value = barEntry.value;
        viewBarEntry.type = barEntry.type;
        viewBarEntry.timestamp = barEntry.timestamp;
        viewBarEntry.localDate = barEntry.localDate;
        viewBarEntry.currentHeight = barEntry.currentHeight;
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

        public BarChartViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView = itemView;
            BarEntry barEntry = new BarEntry();
            contentView.setTag(barEntry);
        }
    }

}
