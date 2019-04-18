package com.yxc.barchart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import com.yxc.barchartlib.util.DisplayUtil;
import com.yxc.barchartlib.view.AnimatedDecorator;
import com.yxc.barchartlib.view.CustomAnimatedDecorator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author yxc
 * @date 2019/4/18
 */
public class BarAnimatorActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener {

    private RecyclerView recycler_view;
    LinearLayoutManager linearLayoutManager;
    MyViewAdapter mAdapter;
    AnimatedDecorator mItemDecoration;

    private ArrayList<String> dataList = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_animator);
        recycler_view = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view.setLayoutManager(linearLayoutManager);
        initData();
        mAdapter = new MyViewAdapter(this, dataList);
        recycler_view.setAdapter(mAdapter);
        recycler_view.getViewTreeObserver().addOnGlobalLayoutListener(this);

    }

    private void initData(){
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

        dataList.add("a1");
        dataList.add("b1");
        dataList.add("c1");
        dataList.add("d1");
        dataList.add("e1");
        dataList.add("f1");
        dataList.add("g1");
        dataList.add("h1");
        dataList.add("i1");
        dataList.add("j1");
        dataList.add("k1");
    }

    @Override
    public void onGlobalLayout() {
        HashMap<Integer, CustomAnimatedDecorator> map = new HashMap<>();
        for (int i = 0; i< recycler_view.getChildCount(); i++){

            View child = recycler_view.getChildAt(i);
            int position = recycler_view.getChildAdapterPosition(child);
            int height = recycler_view.getHeight() * i/recycler_view.getChildCount();
            CustomAnimatedDecorator drawable = new CustomAnimatedDecorator(DisplayUtil.dip2px(15), recycler_view.getHeight(), DisplayUtil.dip2px(10),
                    height);
            map.put(position, drawable);
        }

        mItemDecoration = new AnimatedDecorator(map);
        recycler_view.addItemDecoration(mItemDecoration);
        recycler_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}
