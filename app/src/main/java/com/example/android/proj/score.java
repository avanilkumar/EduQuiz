package com.example.android.proj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class score extends AppCompatActivity {

    private String mSubject;
    private int mScore;
    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        mSubject = getIntent().getStringExtra("subject");
        mScore = getIntent().getIntExtra("score",0);
        mCount = getIntent().getIntExtra("total",0);
        ((TextView)findViewById(R.id.subject)).setText(mSubject);
        ((TextView)findViewById(R.id.correct)).setText(""+mScore);
        ((TextView)findViewById(R.id.count)).setText(""+mCount);
        ((TextView)findViewById(R.id.percentage)).setText(""+((mScore*100)/mCount)+"%");
    }


    public void onOkPressed(View view) {
        finish();
    }
}
