package com.yxc.barchart.map.location.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.barchart.R;
import com.yxc.barchart.map.model.Record;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

	private Context mContext;
	private List<Record> mRecordList;

	public RecordAdapter(Context context, List<Record> list) {
		this.mContext = context;
		this.mRecordList = list;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.layout_record_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Record record = mRecordList.get(position);
		if (null != record){
			holder.bindData(position, record);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return mRecordList.size();
	}


	private  OnRecordItemClickListener onRecordItemClickListener;

	public interface OnRecordItemClickListener{
		void onItemClick(int position, Record record);
		void onRecordCorrect(int position, Record record);
	}

	public void setOnRecordItemClickListener(OnRecordItemClickListener onRecordItemClickListener) {
		this.onRecordItemClickListener = onRecordItemClickListener;
	}

	class ViewHolder extends RecyclerView.ViewHolder{
		View itemView;
		TextView date;
		TextView record;
		Button recordCorrect;

		public ViewHolder(@NonNull View convertView) {
			super(convertView);
			date = convertView.findViewById(R.id.date);
			record = convertView.findViewById(R.id.record);
			itemView = convertView.findViewById(R.id.itemView);
			recordCorrect = convertView.findViewById(R.id.btn_record);
		}

		public void bindData(final int position, final Record item){
			date.setText(item.getDate() + ":" + item.id);
			record.setText(item.toString());
			recordCorrect.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onRecordItemClickListener != null){
						onRecordItemClickListener.onItemClick(position, item);
					}
				}
			});

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onRecordItemClickListener != null){
						onRecordItemClickListener.onItemClick(position, item);
					}
				}
			});
		}
	}

}
