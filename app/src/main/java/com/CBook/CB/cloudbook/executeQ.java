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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class executeQ extends AsyncTask<String, String, String> {
    public InputStream is=null;
    private AsyncCallback refreshCall;
    private String line, result;
    public executeQ(AsyncCallback refreshCall)
    {
        this.refreshCall = refreshCall;
    }
    @Override
    protected String doInBackground(String... args) {
        Calendar cal = Calendar.getInstance();
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("query", args[0]));
        nameValuePairs.add(new BasicNameValuePair("passcode", Integer.toString(cal.get(Calendar.YEAR))));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://yjham2002.woobi.co.kr/cb_directory/adminQuery.php");
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
                  staticInfo.error = result;
        } catch (Exception e) {}
        return null;
    }
    protected void onPostExecute(String rs) {
        refreshCall.callback();
    }
}