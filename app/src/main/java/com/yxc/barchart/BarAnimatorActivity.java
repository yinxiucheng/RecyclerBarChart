package com.yxc.barchart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;

import com.yxc.barchartlib.util.DisplayUtil;
import com.yxc.barchartlib.view.AnimatedDecorator;
import com.yxc.barchartlib.view.CustomAnimatedDecorator;

/**
 * @author yxc
 * @date 2019/4/18
 */
public class BarAnimatorActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener {

    private RecyclerView recycler_view;
    LinearLayoutManager linearLayoutManager;
    MyViewAdapter mAdapter;
    AnimatedDecorator mItemDecoration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_animator);
        recycler_view = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        recycler_view.setLayoutManager(linearLayoutManager);
        mAdapter = new MyViewAdapter(this);
        recycler_view.setAdapter(mAdapter);
        recycler_view.getViewTreeObserver().addOnGlobalLayoutListener(this);

    }


    @Override
    public void onGlobalLayout() {
        CustomAnimatedDecorator drawable = new CustomAnimatedDecorator(recycler_view.getHeight(), DisplayUtil.dip2px(30));
        mItemDecoration = new AnimatedDecorator(drawable, AnimatedDecorator.Side.TOP);
        recycler_view.addItemDecoration(mItemDecoration);
        recycler_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}
