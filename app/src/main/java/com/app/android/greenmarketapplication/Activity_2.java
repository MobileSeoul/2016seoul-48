package com.app.android.greenmarketapplication;

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

public class Activity_2 extends AppCompatActivity {

    ActionBar actionBar;
    TextView textview;
    TextView textbox1,textbox2;
    String str1,str2;
    ImageButton back,home;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        //액션바 설정
        actionBar = this.getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_activity,null);
        actionBar.setCustomView(mCustomView);
        textview = (TextView) findViewById(R.id.textview);
        textview.setText("장터소개");

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
                finish();
            }
        });

        textbox1 = (TextView) findViewById(R.id.textbox1);
        textbox2 = (TextView) findViewById(R.id.textbox2);

        str1 = "서울시는 생활 속에서 재사용과 자원순환의 녹색문화를 정착시키기 위하여 생활주변 작은 나눔장터를 지원하는 ‘녹색장터’ 사업을 추진하고 있습니다. 녹색장터는 시민이 직접 아파트, 공원 등 일상생활 곳곳에서 장터를 운영함으로써 지역주민이 즐겁게 참여할 수 있는 주민교육의 장으로 우리 아이들에게는 환경과 경제교육의 장소입니다.";
        str2 = "1. 녹색장터에서 만나는 사람들과 친절하게 인사합니다.\n\n" +
                "2. 장터에 도착하면 접수처에 먼저 접수한 후 자리를 폅니다.\n\n" +
                "3. 정해진 구역에서 자리를 폅니다.\n\n" +
                "4. 예쁘게 전시하고, 물품마다 가격표를 적어두면 더 좋습니다.\n\n" +
                "5. 가격 흥정과정에서 시시비비를 주의하고 고운 말을 사용합니다.\n\n" +
                "6. 아름다운 지구를 위해 일회용 용기는 쓰지 않습니다.\n\n" +
                "7. 감동적이고 향기로운 녹색장터가 되도록 자기주변 쓰레기는 잘 정리합니다.\n\n" +
                "8. 녹색장터가 끝나고 남은 물건은 기증하거나 잘 챙겨서 가지고 갑니다.\n\n" +
                "9. 수익금의 10%는 사회적 공익활동을 위해 기부할 수 있습니다.";

        textbox1.setText(str1);
        textbox2.setText(str2);

    }
}
