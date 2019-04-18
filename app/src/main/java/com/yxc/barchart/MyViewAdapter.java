package com.yxc.barchart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * @author yxc
 * @date 2019/4/18
 */
public class MyViewAdapter extends RecyclerView.Adapter<MyViewAdapter.MyViewHolder> {

    List<String> dataList;
    Context context;

    public MyViewAdapter(Context context, List<String> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.view_item, parent, false);
        return new MyViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{

        View contentView;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView = itemView;
            textView = contentView.findViewById(R.id.txt_data);
        }

        public void bind(String data) {
            textView.setText(data);
        }
    }
}
