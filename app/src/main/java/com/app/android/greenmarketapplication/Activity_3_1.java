package com.app.android.greenmarketapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Jieun on 2016-10-28.
 */

public class Activity_3_1 extends AppCompatActivity {
    private final String SERVER_ADDRESS = "http://localhost";

    ActionBar actionBar;
    TextView textview;
    EditText title, location, content;
    Button submit, cancel;
    ImageButton back,home;


    //http서버 관련


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_1);

        //액션바 설정
        actionBar = this.getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_activity, null);
        actionBar.setCustomView(mCustomView);
        textview = (TextView) findViewById(R.id.textview);
        textview.setText("글쓰기");

        //액션바 버튼 클릭
        back = (ImageButton) findViewById(R.id.back);
        home = (ImageButton) findViewById(R.id.home);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Activity_3.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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

        //받아서 넘길 속성들
        title = (EditText) findViewById(R.id.title);
        location = (EditText) findViewById(R.id.location);
        content = (EditText) findViewById(R.id.content);
        //등록, 취소 버튼
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);

        //등록 버튼 리스너
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //null이 있을때
                if (title.getText().toString().equals("") || location.getText().toString().equals("") || content.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "모든 항목에 입력을 완료해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                String str1 = title.getText().toString();
                String str2 = location.getText().toString();
                String str3 = content.getText().toString();

                insertToDB(str1, str2, str3);
            }
        });
        //취소 버튼 리스너
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Activity_3.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });



    }

    private void insertToDB(String str1, String str2, String str3){

            class InsertData extends AsyncTask<String, Void, String>{
               ProgressDialog progressDialog;

                @Override
                protected String doInBackground(String... params) {
                    try{
                        String title = (String)params[0];
                        String location = (String)params[1];
                        String content = (String)params[2];

                        String link = "http://52.78.101.50/dbinsert.php";
                        String data = URLEncoder.encode("title","UTF-8") + "=" + URLEncoder.encode(title,"UTF-8");
                        data += "&" + URLEncoder.encode("location","UTF-8") + "=" + URLEncoder.encode(location,"UTF-8");
                        data += "&" + URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(content,"UTF-8");

                        URL url = new URL(link);
                        URLConnection conn = url.openConnection();

                        conn.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                        wr.write(data);
                        wr.flush();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        StringBuilder sb = new StringBuilder();
                        String line = null;

                        while((line = reader.readLine())!= null){
                            sb.append(line);
                            break;
                        }return sb.toString();

                    }catch(Exception e){
                        return new String("Exception: "+e.getMessage());
                    }

                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"등록이 완료되었습니다.",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),Activity_3.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = ProgressDialog.show(Activity_3_1.this, "", "잠시만 기다려주세요.");
                }
            }

        //디비 insert 실행
        InsertData task = new InsertData();
        task.execute(str1,str2,str3);
    }

    //뒤로가기버튼 클릭
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),Activity_3.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

}
