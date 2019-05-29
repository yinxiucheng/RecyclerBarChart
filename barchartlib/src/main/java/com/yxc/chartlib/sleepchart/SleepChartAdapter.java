package com.yxc.chartlib.sleepchart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.R;
import com.yxc.chartlib.entrys.SleepItemEntry;

import java.util.List;

/**
 * @author yxc
 * @since 2019/4/6
 */
final public class SleepChartAdapter extends RecyclerView.Adapter<SleepChartAdapter.BarChartViewHolder> {

    private Context mContext;
    private List<SleepItemEntry> mEntries;
    private RecyclerView mRecyclerView;
    private long timestampDistance;
    private long currentTimeDistance;

    public SleepChartAdapter(Context context, List<SleepItemEntry> entries, RecyclerView recyclerView) {
        this.mContext = context;
        this.mEntries = entries;
        this.mRecyclerView = recyclerView;
    }

    public void setEntries(List<SleepItemEntry> mEntries) {
        this.mEntries = mEntries;
        notifyDataSetChanged();
    }

    public List<SleepItemEntry> getEntries() {
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
        float contentWidth = mRecyclerView.getWidth() - mRecyclerView.getPaddingRight() - mRecyclerView.getPaddingLeft();

        SleepItemEntry latestSleepEntry = mEntries.get(0);
        SleepItemEntry longestSleepEntry = mEntries.get(mEntries.size() - 1);
        timestampDistance = latestSleepEntry.sleepItemTime.endTimestamp - longestSleepEntry.sleepItemTime.startTimestamp;
        SleepItemEntry currentSleepEntry = mEntries.get(position);
        currentTimeDistance = currentSleepEntry.sleepItemTime.getSleepTime();
        int itemWidth = (int) ((currentTimeDistance * contentWidth) / timestampDistance);
        setLinearLayout(viewHolder.contentView, itemWidth < 1 ? 1 : itemWidth);
        viewHolder.contentView.setTag(currentSleepEntry);
    }

    @Override
    public int getItemViewType(int position) {
        SleepItemEntry barEntry = mEntries.get(position);
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
