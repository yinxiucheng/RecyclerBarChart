package com.yxc.chartlib.barchart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.R;
import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.component.BaseYAxis;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.entrys.BarEntry;

import java.util.List;

/**
 * @author yxc
 * @since  2019/4/6
 */
public abstract class BaseBarChartAdapter<T extends BarEntry, V extends BaseYAxis> extends
        RecyclerView.Adapter<BaseBarChartAdapter.BarChartViewHolder> {

    protected Context mContext;
    protected List<T> mEntries;
    protected RecyclerView mRecyclerView;
    protected XAxis mXAxis;
    protected V mYAxis;
    protected BarChartAttrs mBarChartAttrs;

    public BaseBarChartAdapter(Context context, List<T> entries, RecyclerView recyclerView,
                               XAxis xAxis, BarChartAttrs attrs) {
        this.mContext = context;
        this.mEntries = entries;
        this.mRecyclerView = recyclerView;
        this.mXAxis = xAxis;
        this.mBarChartAttrs = attrs;
    }

    public void setXAxis(XAxis mXAxis) {
        this.mXAxis = mXAxis;
        notifyDataSetChanged();
    }

    public void setEntries(List<T> mEntries) {
        this.mEntries = mEntries;
        notifyDataSetChanged();
    }

    public List<T> getEntries() {
        return mEntries;
    }

    @NonNull
    @Override
    public BarChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_barchart, parent, false);
        BarChartViewHolder viewHolder = new BarChartViewHolder(view);

        return viewHolder;
    }

    public abstract void onBindViewHolder(@NonNull BarChartViewHolder viewHolder, int i);

    @Override
    public int getItemViewType(int position) {
        T barEntry = mEntries.get(position);
        if (null != barEntry) {
            return barEntry.type;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    public class BarChartViewHolder extends RecyclerView.ViewHolder {
        View contentView;

        BarChartViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView = itemView;
        }
    }

    public void setYAxis(V mYAxis) {
        this.mYAxis = mYAxis;
        notifyDataSetChanged();
    }

}
