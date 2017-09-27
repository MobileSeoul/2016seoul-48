package com.app.android.greenmarketapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jieun on 2016-10-30.
 */

public class Activity_4 extends AppCompatActivity {

    //액티비티 관련 객체
    ActionBar actionBar;
    TextView textview;

    private BBSListAdapter BBSAdapter;
    private ListView BBSList;

    ArrayList<ListData> mListData = new ArrayList<>();

    ImageButton back, home;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        //액션바 설정
        actionBar = this.getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_activity,null);
        actionBar.setCustomView(mCustomView);
        textview = (TextView) findViewById(R.id.textview);
        textview.setText("연락처 안내");

        //액션바 버튼 클릭
        back = (ImageButton) findViewById(R.id.back);
        home = (ImageButton) findViewById(R.id.home);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

        BBSList = (ListView)findViewById(R.id.listview3); //리스트선언
        BBSAdapter = new BBSListAdapter(this);


        mListData.add(new ListData("종로구","청소행정과","기현민","2148-2392"));
        mListData.add(new ListData("중구","청소행정과","김종희","3396-5465"));
        mListData.add(new ListData("용산구","청소행정과","김종찬","2199-7312"));
        mListData.add(new ListData("성동구","청소행정과","추상백","2286-5544"));
        mListData.add(new ListData("광진구","청소과","송영준","450-7627"));
        mListData.add(new ListData("동대문구","청소행정과","김문영","2127-4379"));
        mListData.add(new ListData("중랑구","청소행정과","박노균","2094-1963"));
        mListData.add(new ListData("성북구","청소행정과","강양순","920-3377"));
        mListData.add(new ListData("강북구","청소행정과","정종삼","901-6794"));
        mListData.add(new ListData("도봉구","청소행정과","이선윤","2091-3272"));
        mListData.add(new ListData("노원구","자원순환과","김가영","2116-3810"));
        mListData.add(new ListData("은평구","청소행정과","노재흥","351-7583"));
        mListData.add(new ListData("서대문구","청소행정과","강영자","330-1514"));
        mListData.add(new ListData("마포구","청소행정과","전동훈","3153-9225"));
        mListData.add(new ListData("양천구","청소행정과","이미경","2620-3436"));
        mListData.add(new ListData("강서구","청소자원과","주종화","2600-4075"));
        mListData.add(new ListData("구로구","청소행정과","안영철","860-2903"));
        mListData.add(new ListData("금천구","청소행정과","석승현","2627-1493"));
        mListData.add(new ListData("영등포구","청소과","김창현","2670-3486"));
        mListData.add(new ListData("동작구","청소행정과","이수진","820-9758"));
        mListData.add(new ListData("관악구","청소행정과","권영은","879-6222"));
        mListData.add(new ListData("서초구","청소행정과","유일수","2155-6743"));
        mListData.add(new ListData("강남구","청소행정과","이상기","3423-5986"));
        mListData.add(new ListData("송파구","클린도시과","백경탁","2147-2865"));
        mListData.add(new ListData("강동구","청소행정과","전민경","3425-5884"));

        BBSAdapter.notifyDataSetChanged();

        BBSList.setAdapter(BBSAdapter); //리스트에 어댑터를 먹여준다.
        BBSList.setOnItemClickListener( //리스트 클릭시 실행될 로직 선언
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        ListData mData = mListData.get(position); // 클릭한 포지션의 데이터를 가져온다.
                        String tel = "tel:02-";
                        tel += mData.mCall;
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(tel)));
                    }
                });

    }


    // <리스트 적용부분
    class ViewHolder {
        public TextView mGeo;
        public TextView mDepartment;
        public TextView mName;
        public TextView mCall;

    }


    public class BBSListAdapter extends BaseAdapter {
        private Context mContext = null;


        public BBSListAdapter(Context mContext) {
            this.mContext = mContext;

        }


        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.itemstyle3, null);

                holder.mGeo = (TextView) convertView.findViewById(R.id.item_geo);
                holder.mDepartment = (TextView) convertView.findViewById(R.id.item_department);
                holder.mName = (TextView) convertView.findViewById(R.id.item_name);
                holder.mCall = (TextView) convertView.findViewById(R.id.item_call);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);

            holder.mGeo.setText(mData.mGeo);
            holder.mDepartment.setText(mData.mDepartment); //
            holder.mName.setText(mData.mName);
            holder.mCall.setText(mData.mCall);

            return convertView;

        }



    }

    public class ListData { // 데이터를 받는 클래스

        public String mGeo;
        public String mDepartment;
        public String mName;
        public String mCall;


        public ListData()  {


        }

        public ListData(String mGeo, String mDepartment, String mName, String mCall) {
            this.mCall = mCall;
            this.mDepartment = mDepartment;
            this.mGeo = mGeo;
            this.mName = mName;
        }
    }
    // 리스트 적용부분 >

}


