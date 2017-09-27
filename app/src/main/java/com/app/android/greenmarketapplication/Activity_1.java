/**
 *
 * 동백고등학교 게시판 파서
 * Made By Gomsang , Use "Jericho" Library
 * You should write down gomsang license and "jericho" license
 *
 *
 * */

package com.app.android.greenmarketapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Activity_1 extends ActionBarActivity {

    //액티비티 관련 객체
    ActionBar actionBar;
    LinearLayout linearcontent;
    TextView textview;

    ImageButton back, home;

    private static String URL_PRIMARY = "http://fleamarket.seoul.go.kr"; //홈페이지 원본 주소이다.
    private static String GETNOTICE = "/greenmarket/market_area.do?Page=1&Code=114"; //홈페이지 의 게시판을 나타내는 뒤 주소, 비슷한 게시판들은 거의 파싱이 가능하므로 응용하여 사용하자.

    private String[] url = new String[5];
    private java.net.URL URL;

    private Source source;
    private ProgressDialog progressDialog;
    private BBSListAdapter BBSAdapter = null;
    private ListView BBSList;
    private int BBSlocate;

    private ConnectivityManager cManager;
    private NetworkInfo mobile;
    private NetworkInfo wifi;

  ArrayList<ListData> mListData = new ArrayList<>();

    @Override
    protected void onStop() { //멈추었을때 다이어로그를 제거해주는 메서드
        super.onStop();
        if ( progressDialog != null)
            progressDialog.dismiss(); //다이어로그가 켜져있을경우 (!null) 종료시켜준다
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);



        //액션바 설정
        actionBar = this.getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_activity,null);
        actionBar.setCustomView(mCustomView);
        textview = (TextView) findViewById(R.id.textview);
        textview.setText("장터일정");

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

        BBSList = (ListView)findViewById(R.id.listView); //리스트선언
        BBSAdapter = new BBSListAdapter(this);
        BBSList.setAdapter(BBSAdapter); //리스트에 어댑터를 먹여준다.
        BBSList.setOnItemClickListener( //리스트 클릭시 실행될 로직 선언
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        ListData mData = mListData.get(position); // 클릭한 포지션의 데이터를 가져온다.
                        String URL_BCS = mData.mUrl; //가져온 데이터 중 url 부분만 적출해낸다.

                        Intent intent = new Intent(getApplicationContext(),Activity_1_1.class);
                        intent.putExtra("url",URL_PRIMARY + URL_BCS);                                       //적출해낸 url 을 이용해 URL_PRIMARY 와 붙임
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);

                    }
                });


        url[0] = URL_PRIMARY + "/greenmarket/market_area.do?Page=1&Code=114";
        url[1] = URL_PRIMARY + "/greenmarket/market_area.do?Page=2&Code=114";
        url[2] = URL_PRIMARY + "/greenmarket/market_area.do?Page=3&Code=114";
        url[3] = URL_PRIMARY + "/greenmarket/market_area.do?Page=4&Code=114";
        url[4] = URL_PRIMARY + "/greenmarket/market_area.do?Page=5&Code=114";

        if(isInternetCon()) { //false 반환시 if 문안의 로직 실행
            Toast.makeText(Activity_1.this, "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }else{ //인터넷 체크 통과시 실행할 로직
            try {
                process();
                BBSAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.d("ERROR", e + "");

            }
        }





    }



    private void process() throws IOException {
        new Thread() {

            @Override
            public void run() {
                Handler Progress = new Handler(Looper.getMainLooper());
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(Activity_1.this, "", "장터 정보를 가져오는중 입니다.");
                    }
                }, 0);

                try {
                    URL = new URL(url[0]);
                    InputStream html = URL.openStream();
                    source = new Source(new InputStreamReader(html, "utf-8"));
                    source.fullSequentialParse(); //순차적으로 구문분석
                    parse(source);

                    URL = new URL(url[1]);
                    InputStream html1 = URL.openStream();
                    source = new Source(new InputStreamReader(html1, "utf-8"));
                    source.fullSequentialParse(); //순차적으로 구문분석
                    parse(source);

                    URL = new URL(url[2]);
                    InputStream html2 = URL.openStream();
                    source = new Source(new InputStreamReader(html2, "utf-8"));
                    source.fullSequentialParse(); //순차적으로 구문분석
                    parse(source);

                    URL = new URL(url[3]);
                    InputStream html3 = URL.openStream();
                    source = new Source(new InputStreamReader(html3, "utf-8"));
                    source.fullSequentialParse(); //순차적으로 구문분석
                    parse(source);

                    URL = new URL(url[4]);
                    InputStream html4 = URL.openStream();
                    source = new Source(new InputStreamReader(html4, "utf-8"));
                    source.fullSequentialParse(); //순차적으로 구문분석
                    parse(source);
                } catch (Exception e) {
                    Log.d("ERROR", e + "");
                }

                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BBSAdapter.notifyDataSetChanged(); //모든 작업이 끝나면 리스트 갱신
                        progressDialog.dismiss(); //모든 작업이 끝나면 다이어로그 종료
                    }
                }, 0);

            }

        }.start();


    }

    private void parse(Source source) {
        List<StartTag> tabletags = source.getAllStartTags(HTMLElementName.DIV); // DIV 타입의 모든 태그들을 불러온다.

        for(int arrnum = 0;arrnum < tabletags.size(); arrnum++){ //DIV 모든 태그중 market-list 태그가 몇번째임을 구한다.

            if(tabletags.get(arrnum).toString().equals("<div class=\"market-list\" id=\"ui_market_list\">")) {
                BBSlocate = arrnum; //DIV 클래스가 market-list 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                Log.d("BBSLOCATES", arrnum+""); //arrnum 로깅
                break;
            }
        }

        Element BBS_DIV = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate); //BBSlocate 번째 의 DIV
        Element BBS_TABLE = (Element) BBS_DIV.getAllElements(HTMLElementName.TABLE).get(0); //테이블 접속
        Element BBS_TBODY = (Element) BBS_TABLE.getAllElements(HTMLElementName.TBODY).get(0); //데이터가 있는 TBODY 에 접속


        for(int C_TR = 0; C_TR < BBS_TBODY.getAllElements(HTMLElementName.TR).size(); C_TR++){




            try {
                Element BBS_TR = (Element) BBS_TBODY.getAllElements(HTMLElementName.TR).get(C_TR); //TR 접속

                Element BC_TYPE = (Element) BBS_TR.getAllElements(HTMLElementName.TH).get(0); //타입 을 불러온다.

                Element BC_info = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(1); //URL(herf) TITLE(title)
                Element BC_a = (Element) BC_info.getAllElements(HTMLElementName.A).get(0); //BC_info 안의 a 태그
                String BCS_url = BC_a.getAttributeValue("href"); //a 태그의 herf 는 BCS_url 로 선언
                String BCS_title = BC_a.getContent().toString(); //a 태그의 title 은 BCS_title 로 선언

                Element BC_location = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(2); //소재지를 불러온다.
                Element BC_opentime = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(4); // 장터개장일을 불러온다.

                String BCS_type = BC_TYPE.getContent().toString();
                String BCS_location = BC_location.getContent().toString();
                String BCS_date = BC_opentime.getContent().toString();


                mListData.add(new ListData(BCS_type, BCS_title, BCS_url, BCS_location, BCS_date)); //데이터가 모이면 데이터 리스트 클래스에 데이터들을 등록



            }catch(Exception e){
                Log.d("BCSERROR",e+"");
            }
        }

    }


    private boolean isInternetCon() {
        cManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); //모바일 데이터 여부
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); //와이파이 여부
        return !mobile.isConnected() && !wifi.isConnected(); //결과값을 리턴
    }




    // <리스트 적용부분
    class ViewHolder {
        public TextView mType;
        public TextView mTitle;
        public TextView mUrl;
        public TextView mLocation;
        public TextView mDate;
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
                convertView = inflater.inflate(R.layout.itemstyle, null);

                holder.mTitle = (TextView) convertView.findViewById(R.id.item_title);
                holder.mLocation = (TextView) convertView.findViewById(R.id.item_location);
                holder.mDate = (TextView) convertView.findViewById(R.id.item_opentime);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);



            holder.mTitle.setText(mData.mTitle);


            holder.mLocation.setText(mData.mLocation); //
            holder.mDate.setText(mData.mOpenTime);

            return convertView;

        }



    }

    public class ListData { // 데이터를 받는 클래스

        public String mType;
        public String mTitle;
        public String mUrl;
        public String mLocation;
        public String mOpenTime;

        public String getmOpenTime() {
            return mOpenTime;
        }

        public String getmTitle() {
            return mTitle;
        }

        public ListData()  {


        }

        public ListData(String mType, String mTitle, String mUrl, String mLocation, String mOpenTime)  { //데이터를 받는 클래스 메서드
            this.mType = mType;
            this.mTitle = mTitle;
            this.mUrl = mUrl;
            this.mLocation = mLocation;
            this.mOpenTime = mOpenTime;

        }

    }
    // 리스트 적용부분 >
}

