package com.yxc.barchart.ui.waterdrop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yxc.barchart.R;
import com.yxc.barchart.view.BezierCircle;


public class BezierActivity extends AppCompatActivity {

    private BezierCircle bezierCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        bezierCircle = findViewById(R.id.bezier_view);
    }
}
