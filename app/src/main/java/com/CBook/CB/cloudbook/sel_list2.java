package com.CBook.CB.cloudbook;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class sel_list2 extends AsyncTask<Void, Void, Void> {
    public String line2=null, result2=null;
    public InputStream is2=null;
    private AsyncCallback refreshCall;
    public sel_list2(AsyncCallback refreshCall)
    {
        this.refreshCall = refreshCall;
    }
    @Override
    protected Void doInBackground(Void... arg0) {
        try {
            staticInfo.async = 0;
            HttpClient httpclient2 = new DefaultHttpClient();
            HttpPost httppost2 = new HttpPost("http://yjham2002.woobi.co.kr/cb_directory/getArticles.php");
            HttpResponse response2 = httpclient2.execute(httppost2);
            HttpEntity entity2 = response2.getEntity();
            is2 = entity2.getContent();

        } catch (Exception e) {}
        try {
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(is2, "UTF-8"), 30);
            StringBuilder sb2 = new StringBuilder();
            while ((line2 = reader2.readLine()) != null) sb2.append(line2 + "\n");
            is2.close();
            result2 = sb2.toString();
        } catch (Exception e) {}
        try {
            staticInfo.mAdapter.mListData.clear();
            JSONArray json_arr2 = new JSONArray(result2);
            int ART_CNT2 = json_arr2.getJSONObject(0).getInt("cnt_num");
            for(int i = 0; i < ART_CNT2 ; i++){
                JSONObject json_list2 = json_arr2.getJSONObject(i);
                staticInfo.mAdapter.addItem(json_list2.getString("id"),json_list2.getString("dt"),json_list2.getString("ct"),Integer.parseInt(json_list2.getString("ix")),json_list2.getString("tk"),Integer.parseInt(json_list2.getString("tf"))
                        ,Double.parseDouble(json_list2.getString("lat")),Double.parseDouble(json_list2.getString("lon")));
            }
        } catch (Exception e) {}
        return null;
    }
    protected void onPostExecute(Void result) {
        refreshCall.callback();
    }
}

