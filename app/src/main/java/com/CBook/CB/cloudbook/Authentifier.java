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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Authentifier extends AsyncTask<Void, Void, Void> {
    public static String line = null, result = null, errorString=null;
    public static InputStream is = null;
    private static String id, pw;
    public static int status = 0;
    private static int Authnum=-1;
    public static void insertInfo(String id_c, String pw_c)
    {
        id = id_c; pw = pw_c;
    }

    private AsyncCallback refreshCall;
    public Authentifier(AsyncCallback refreshCall) {
        this.refreshCall = refreshCall;
    }
    @Override
    protected Void doInBackground(Void... arg0) {
        errorString = null;
        Authnum = -1;
        line = null; result = null; errorString=null; status=0;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id_form", id));
        nameValuePairs.add(new BasicNameValuePair("pw_form", pw));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://yjham2002.woobi.co.kr/cb_directory/appLog.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            errorString += "HTTP : " + e.toString() +'\n';
        }
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, "UTF-8"), 30);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            errorString += "BUFFER : " + e.toString() + '\n';
        }
        try {
            JSONObject json_data = new JSONObject(result);
            Authnum = (Integer.parseInt(json_data.getString("Auth")));
        } catch (Exception e) {
            errorString += "JSON : " + e.toString() + '\n';
        }
        status = 1;
        return null;
    }
    protected void onPostExecute(Void result) {
        staticInfo.auth = Authnum;
        refreshCall.callback();
    }
    public int getStat() {return status;}
    public String catchErrorString()
    {
        return errorString;
    }
    public static int getLoginAuth()
    {
        return Authnum;
    }
}
