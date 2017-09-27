package com.app.android.greenmarketapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jieun on 2016-10-28.
 */

public class Activity_3 extends AppCompatActivity {

    ActionBar actionBar;
    TextView textview;
    RelativeLayout write;

    ImageButton back,home;

    String myJSON;
    JSONArray peoples;

    private ProgressDialog progressDialog;
    private BBSListAdapter BBSAdapter = null;
    private ListView BBSList;
    ArrayList<ListData> mListData = new ArrayList<>();

    private ConnectivityManager cManager;
    private NetworkInfo mobile;
    private NetworkInfo wifi;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_TIME = "time";
    private static final String TAG_TITLE = "title";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_CONTENT = "content";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

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
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

        //글쓰기버튼
        write = (RelativeLayout) findViewById(R.id.write);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Activity_3_1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });


        BBSList = (ListView)findViewById(R.id.listView2); //리스트선언
        BBSAdapter = new BBSListAdapter(this);

        if(isInternetCon()) { //false 반환시 if 문안의 로직 실행
            Toast.makeText(Activity_3.this, "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }else{ //인터넷 체크 통과시 실행할 로직
            try {
                Log.d("상태","뀨");
                getData("http://52.78.101.50/dbselect.php");
                BBSAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.d("ERROR", e + "");
            }
        }

        BBSList.setAdapter(BBSAdapter); //리스트에 어댑터를 먹여준다.
        BBSList.setOnItemClickListener( //리스트 클릭시 실행될 로직 선언
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        ListData mData = mListData.get(position);
                        String time = mData.mTime;
                        String title = mData.mTitle;
                        String location = mData.mLocation;
                        String content = mData.mContent;

                        Intent intent = new Intent(getApplicationContext(),Activity_3_2.class);
                        intent.putExtra("time",time);
                        intent.putExtra("title",title);
                        intent.putExtra("location",location);
                        intent.putExtra("content",content);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);

                    }
                });

    }
    protected void showList(){
        try{
            JSONObject jsonobj = new JSONObject(myJSON);
            peoples = jsonobj.getJSONArray(TAG_RESULTS);

            for(int i=0; i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String time = c.getString(TAG_TIME);
                String title = c.getString(TAG_TITLE);
                String location = c.getString(TAG_LOCATION);
                String content = c.getString(TAG_CONTENT);

                Log.d("time",time);
                Log.d("title",title);
                Log.d("location",location);
                Log.d("content",content);
                mListData.add(new ListData(time,title,location,content));

            }
            BBSAdapter.notifyDataSetChanged();


        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(Activity_3.this,"","데이터 정보를 가져오는 중입니다.");
            }

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];

                BufferedReader bufferedReader = null;
                try{
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!=null){
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                //super.onPostExecute(s);
                myJSON=s;
                showList();
                progressDialog.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }




    private boolean isInternetCon() {
        cManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); //모바일 데이터 여부
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); //와이파이 여부
        return !mobile.isConnected() && !wifi.isConnected(); //결과값을 리턴
    }

    // <리스트 적용부분
    class ViewHolder {

        public TextView mTitle;

        public TextView mContent;
        public TextView mLocation;
        public TextView mTime;

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
                convertView = inflater.inflate(R.layout.itemstyle2, null);

                holder.mTime = (TextView) convertView.findViewById(R.id.item_time);
                holder.mTitle = (TextView) convertView.findViewById(R.id.item_title);
                holder.mLocation = (TextView) convertView.findViewById(R.id.item_location);
                holder.mContent = (TextView) convertView.findViewById(R.id.item_content);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);


            holder.mTime.setText(mData.mTime);
            holder.mTitle.setText(mData.mTitle);
            holder.mLocation.setText(mData.mLocation); //
            holder.mContent.setText(mData.mContent);


            return convertView;

        }


    }

    public class ListData { // 데이터를 받는 클래스

        public String mTitle;
        public String mLocation;
        public String mContent;
        public String mTime;


        public ListData()  {


        }

        public ListData(String mTime, String mTitle, String mLocation, String mContent)  { //데이터를 받는 클래스 메서드

            this.mTitle = mTitle;
            this.mLocation = mLocation;
            this.mContent = mContent;
            this.mTime = mTime;

        }

    }
    // 리스트 적용부분 >



    @Override
    protected void onStop() { //멈추었을때 다이어로그를 제거해주는 메서드
        super.onStop();
        if ( progressDialog != null)
            progressDialog.dismiss(); //다이어로그가 켜져있을경우 (!null) 종료시켜준다
    }
}
