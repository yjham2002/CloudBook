package com.CBook.CB.cloudbook;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class getInfo extends AsyncTask<Void, Void, Void> {

    public String line=null, result=null;
    public InputStream is=null;
    private AsyncCallback refreshCall;
    public getInfo(AsyncCallback refreshCall)
    {
        this.refreshCall = refreshCall;
    }
    @Override
    protected Void doInBackground(Void... arg0) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("id_form", staticInfo.getID()));
        try {
            staticInfo.async = 0;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://yjham2002.woobi.co.kr/cb_directory/getInfo.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {}
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 30);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) sb.append(line + "\n");
            is.close();
            result = sb.toString();
        } catch (Exception e) {}
        try {
                JSONObject json_list = new JSONObject(result);
                staticInfo.netID = json_list.getString("id");
                staticInfo.netPW = json_list.getString("pw");
                staticInfo.netNM = json_list.getString("nm");
                staticInfo.netEM = json_list.getString("em");
                staticInfo.netCT = json_list.getString("ct");
                staticInfo.netPR = json_list.getString("pr");
            } catch (Exception e) {}
        return null;
    }
    protected void onPostExecute(Void result) {
        refreshCall.callback();
    }
}

