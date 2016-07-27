package com.CBook.CB.cloudbook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailActivity extends BaseActivity implements View.OnClickListener{
    private Button back,ref;
    private TextView id, date, idj, num, status, content;
    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        id = (TextView)findViewById(R.id.textView2);
        date = (TextView)findViewById(R.id.textView4);
        idj = (TextView)findViewById(R.id.textView8);
        num = (TextView)findViewById(R.id.textView10);
        status = (TextView)findViewById(R.id.textView12);
        content = (TextView)findViewById(R.id.textView14);
        back = (Button)findViewById(R.id.button8);
        ref= (Button)findViewById(R.id.button6);
        back.setOnClickListener(this);
        ref.setOnClickListener(this);

        refresh();

    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.button6:
                refresh();
                break;
            case R.id.button8:
                finish();
                break;
            default: break;
        }
    }

    public void refresh(){
        final ProgressDialog pdial = new ProgressDialog(this);
        pdial.setCancelable(true);
        pdial.setMessage("의뢰 정보를 가져오는중...");
        pdial.show();

        HashMap<String, String> nameValuePairs = new HashMap<>();
        nameValuePairs.put("index", Integer.toString(staticInfo.detail));
        staticInfo.async = 0;

        new Communicator().postHttp(staticInfo.URL_GET_DETAIL, nameValuePairs, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    JSONObject json_list = new JSONObject(msg.getData().getString("jsonString"));
                    staticInfo.DetalId = json_list.getString("id");
                    staticInfo.DetailDate = json_list.getString("dt");
                    staticInfo.DetailContent = json_list.getString("ct");
                    staticInfo.DetailIndex = Integer.parseInt(json_list.getString("ix"));
                    staticInfo.DetailTaker = json_list.getString("tk");
                    staticInfo.DetailTaken = Integer.parseInt(json_list.getString("tf"));
                    staticInfo.DetailNumber = json_list.getString("nm");
                } catch (Exception e) {}

                id.setText(staticInfo.DetalId.equals("null") ? "없음" : staticInfo.DetalId);
                date.setText(staticInfo.DetailDate.equals("null") ? "없음" : staticInfo.DetailDate);
                idj.setText(staticInfo.DetailTaker == null ? "없음" : staticInfo.DetailTaker);
                num.setText(staticInfo.DetailNumber == null ? "없음" : staticInfo.DetailNumber);
                content.setText(staticInfo.DetailContent.equals("null") ? "없음" : staticInfo.DetailContent);

                if (staticInfo.DetailTaken == 0) {
                    status.setText("접수 대기중");
                    status.setTextColor(Color.parseColor("#FFFC7373"));
                } else if (staticInfo.DetailTaken == 1) {
                    status.setText("접수됨");
                    status.setTextColor(Color.parseColor("#FF6898EB"));
                } else {
                    status.setText("완료됨");
                    status.setTextColor(Color.parseColor("#FF69CB69"));
                }
                pdial.dismiss();
            }
        });

    }
}
