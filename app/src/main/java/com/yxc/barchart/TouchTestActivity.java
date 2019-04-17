
package com.yxc.barchart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.yxc.barchart.test.FirstViewGroup;
import com.yxc.barchart.test.MyRecyclerView;

public class TouchTestActivity extends AppCompatActivity {


    FrameLayout container;

    View view1;
    MyRecyclerView myRecyclerView;
    FirstViewGroup recyclerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        initView();
    }

    private void initView() {
        container = findViewById(R.id.container);
        recyclerContainer = container.findViewById(R.id.recyclerContainer);
        view1 = container.findViewById(R.id.view1);

        myRecyclerView = container.findViewById(R.id.myRecyclerView);

        myRecyclerView.setOnTouchInterceptListener(recyclerContainer);

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TouchTestActivity.this, "view1 click", Toast.LENGTH_LONG).show();
            }
        });
    }

}
