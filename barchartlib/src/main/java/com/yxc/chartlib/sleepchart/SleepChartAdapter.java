package com.yxc.chartlib.sleepchart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yxc.chartlib.R;
import com.yxc.chartlib.entrys.SleepEntry;

import java.util.List;

/**
 * @author yxc
 * @since 2019/4/6
 */
final public class SleepChartAdapter extends RecyclerView.Adapter<SleepChartAdapter.BarChartViewHolder> {

    private Context mContext;
    private List<SleepEntry> mEntries;
    private RecyclerView mRecyclerView;
    private long timestampDistance;
    private long currentTimeDistance;

    public SleepChartAdapter(Context context, List<SleepEntry> entries, RecyclerView recyclerView) {
        this.mContext = context;
        this.mEntries = entries;
        this.mRecyclerView = recyclerView;
    }

    public void setEntries(List<SleepEntry> mEntries) {
        this.mEntries = mEntries;
        notifyDataSetChanged();
    }

    public List<SleepEntry> getEntries() {
        return mEntries;
    }

    @Override
    public BarChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_barchart, parent, false);
        BarChartViewHolder viewHolder = new BarChartViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BarChartViewHolder viewHolder, int position) {
        float contentWidth = mRecyclerView.getWidth() -
                mRecyclerView.getPaddingRight() - mRecyclerView.getPaddingLeft();
        SleepEntry latestSleepEntry = mEntries.get(0);
        SleepEntry longestSleepEntry = mEntries.get(mEntries.size() - 1);
        timestampDistance = Math.abs(latestSleepEntry.endTimestamp - longestSleepEntry.startTimestamp);
        SleepEntry currentSleepEntry = mEntries.get(position);
        currentTimeDistance = currentSleepEntry.endTimestamp - currentSleepEntry.startTimestamp;
        int itemWidth = (int) ((currentTimeDistance * contentWidth) / timestampDistance);
        setLinearLayout(viewHolder.contentView, itemWidth < 1 ? 1 : itemWidth);
        bindBarEntryToView(viewHolder, position);
    }

    private void bindBarEntryToView(BarChartViewHolder viewHolder, final int position) {
        final SleepEntry barEntry = mEntries.get(position);
        viewHolder.contentView.setTag(barEntry);
    }

    @Override
    public int getItemViewType(int position) {
        SleepEntry barEntry = mEntries.get(position);
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
}
