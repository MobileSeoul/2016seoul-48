package com.app.android.greenmarketapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

/**
 * Created by Jieun on 2016-10-24.
 */

public class Activity_1_1 extends ActionBarActivity {
    //액티비티 관련 객체
    ImageButton back, home;

    private String url;
    private java.net.URL URL;

    Element BC_title;
    Element BC_location1;
    Element BC_location2;
    Element BC_contact;
    Element BC_opentime;
    Element BC_duration;

    String BCS = "";
    String src;
    String str,opening,cycle;
    String[] array1;
    String[] array2;
    String[] duration;

    TextView text_title;
    TextView text_location;
    TextView text_opentime;
    TextView text_opening;
    TextView text_cycle;
    TextView text_contact;
    TextView mText;
    TextView textview;

    LinearLayout mbutton;

    Bitmap bm;

    ImageView mImage;

    ActionBar actionBar;

    private Source source;
    private ProgressDialog progressDialog;
    private Activity_1.BBSListAdapter BBSAdapter = null;
    private ListView BBSList;
    private int BBSlocate;

    private ConnectivityManager cManager;
    private NetworkInfo mobile;
    private NetworkInfo wifi;

    ArrayList<Activity_1.ListData> mListData = new ArrayList<>();

    @Override
    protected void onStop() { //멈추었을때 다이어로그를 제거해주는 메서드
        super.onStop();
        if ( progressDialog != null)
            progressDialog.dismiss(); //다이어로그가 켜져있을경우 (!null) 종료시켜준다
    }
    //뒤로가기 버튼 클릭
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1_1);


        //텍스트뷰
        text_title = (TextView) findViewById(R.id.text_title);
        text_location = (TextView) findViewById(R.id.text_location);
        text_opentime = (TextView) findViewById(R.id.text_opentime);
        text_contact = (TextView) findViewById(R.id.text_contact);
        text_cycle = (TextView) findViewById(R.id.text_cycle);
        text_opening = (TextView) findViewById(R.id.text_opening);
        //버튼뷰
        mbutton = (LinearLayout) findViewById(R.id.mbutton);

        //액션바 설정
        actionBar = this.getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_activity,null);
        actionBar.setCustomView(mCustomView);
        textview = (TextView) findViewById(R.id.textview);
        textview.setText("장터정보");

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
            }
        });

        final Intent intent = getIntent();
        url = intent.getStringExtra("url");          //인텐트로 전달된 uri저장

        if(isInternetCon()) { //false 반환시 if 문안의 로직 실행
            Toast.makeText(this, "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }else{ //인터넷 체크 통과시 실행할 로직
            try {
                process();

            } catch (Exception e) {
                Log.d("ERROR", e + "");

            }
        }
        //자세히 보기 버튼 클릭
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent1);
            }
        });


    }

    private void process() throws IOException {

        new Thread() {

            @Override
            public void run() {

                Handler Progress = new Handler(Looper.getMainLooper());

                try {
                    URL = new URL(url);
                    InputStream html = URL.openStream();
                    source = new Source(new InputStreamReader(html, "utf-8"));
                    source.fullSequentialParse();
                } catch (Exception e) {
                    Log.d("ERROR", e + "");
                }

                List<StartTag> tabletags = source.getAllStartTags(HTMLElementName.DIV); // DIV 타입

                for(int arrnum = 0;arrnum < tabletags.size(); arrnum++){ //DIV 모든 태그중 market-list 태그가 몇번째

                 if(tabletags.get(arrnum).toString().equals("<div id=\"cont\">")) {
                     BBSlocate = arrnum; //DIV 클래스가 market-list 면 arrnum 값을 BBSlocate 로 몇번째
                     Log.d("BBSLOCATES", arrnum+""); //arrnum 로깅
                     break;
                 }
                }


                Element BBS_DIV = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate+1); //BBSlocate 번째 의 DIV
                Element BBS_TABLE = (Element) BBS_DIV.getAllElements(HTMLElementName.TABLE).get(0); //테이블 접속
                Element BBS_TBODY = (Element) BBS_TABLE.getAllElements(HTMLElementName.TBODY).get(0); //데이터가 있는 TBODY 에 접속


                    try {
                        Element BBS_TR = (Element) BBS_TBODY.getAllElements(HTMLElementName.TR).get(0); //첫번째 행
                        BC_location1 = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(0);//지역 ex)구로구
                        BC_location2 = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(1);//소재지 ex)오류2동

                        BBS_TR = (Element) BBS_TBODY.getAllElements(HTMLElementName.TR).get(1);
                        BC_opentime = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(0);//개장일

                        BBS_TR = (Element) BBS_TBODY.getAllElements(HTMLElementName.TR).get(2);
                        BC_title = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(0);//장터명

                        BBS_TR = (Element) BBS_TBODY.getAllElements(HTMLElementName.TR).get(3);
                        BC_duration = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(0);//기간(개장주기,개장시간)


                        BBS_TR = (Element) BBS_TBODY.getAllElements(HTMLElementName.TR).get(4);
                        BC_contact = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(0);//문의처

                        duration = BC_duration.getContent().toString().trim().split("<br />"); //<br>태그 제거

                        array1 = duration[2].trim().split(" : ");                                                //':'문자 제거
                        array2 = duration[3].trim().split(" : ");                                                //':'문자 제거

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                text_title.setText(BC_title.getContent().toString());
                                text_location.setText("소재지 : "+BC_location1.getContent().toString()+" "+BC_location2.getContent().toString());
                                text_opentime.setText("개장일 : "+BC_opentime.getContent().toString());
                                text_cycle.setText("개장주기 : "+array1[1]);
                                text_opening.setText("개장시간 : "+array2[1]);
                                text_contact.setText("문의처 : "+BC_contact.getContent().toString());
                            }
                        });



                    }catch(Exception e){
                        Log.d("BCSERROR",e+"");
                    }






            }

        }.start();


    }
    private boolean isInternetCon() {
        cManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); //모바일 데이터 여부
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); //와이파이 여부
        return !mobile.isConnected() && !wifi.isConnected(); //결과값을 리턴
    }
}
