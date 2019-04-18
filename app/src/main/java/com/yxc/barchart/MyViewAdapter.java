package com.yxc.barchart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author yxc
 * @date 2019/4/18
 */
public class MyViewAdapter extends RecyclerView.Adapter<MyViewAdapter.MyViewHolder> {

    private ArrayList<String> dataList = new ArrayList();

    Context context;

    public MyViewAdapter(Context context){
        this.context = context;
        dataList.add("a");
        dataList.add("b");
        dataList.add("c");
        dataList.add("d");
        dataList.add("e");
        dataList.add("f");
        dataList.add("g");
        dataList.add("h");
        dataList.add("i");
        dataList.add("j");
        dataList.add("k");
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
