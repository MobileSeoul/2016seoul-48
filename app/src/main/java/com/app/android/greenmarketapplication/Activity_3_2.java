package com.app.android.greenmarketapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Jieun on 2016-10-28.
 */

public class Activity_3_2 extends AppCompatActivity {

    ActionBar actionBar;
    TextView textview;
    TextView text1,text2,text3,text4;

    ImageButton home, back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_2);

        //액션바 설정
        actionBar = this.getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_activity,null);
        actionBar.setCustomView(mCustomView);
        textview = (TextView) findViewById(R.id.textview);
        textview.setText("건의방");
        //액션바 버튼 클릭
        back = (ImageButton) findViewById(R.id.back);
        home = (ImageButton) findViewById(R.id.home);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);

        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        String title = intent.getStringExtra("title");
        String location = intent.getStringExtra("location");
        String content = intent.getStringExtra("content");

        text1.setText(title);
        text2.setText(location);
        text3.setText(time);
        text4.setText(content);
    }

}
