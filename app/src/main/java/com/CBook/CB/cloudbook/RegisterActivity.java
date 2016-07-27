package com.CBook.CB.cloudbook;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;

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

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private EditText id_field, pw_field, name_field, mail_field, contact_field, profile_field, confirm;
    private TextView warn;
    private Button submit, cancel;
    private CheckBox check;
    private ProgressDialog pdial;

    private int flag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        warn = (TextView)findViewById(R.id.warn);
        confirm = (EditText)findViewById(R.id.confirm);
        id_field = (EditText)findViewById(R.id.editText1);
        pw_field = (EditText)findViewById(R.id.editText2);
        name_field = (EditText)findViewById(R.id.editText3);
        mail_field = (EditText)findViewById(R.id.editText4);
        contact_field = (EditText)findViewById(R.id.editText5);
        profile_field = (EditText)findViewById(R.id.editText6);
        submit = (Button)findViewById(R.id.button1);
        cancel = (Button)findViewById(R.id.button2);
        check = (CheckBox)findViewById(R.id.checkBox2);

        contact_field.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);

        pdial = new ProgressDialog(this);
        pdial.setCancelable(true);
        pdial.setMessage("회원가입 진행중.....");

        confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals(pw_field.getText().toString())){
                    if(pw_field.getText().length()<=5){
                        flag = 0;
                        warn.setText("비밀번호가 너무 짧습니다. (6자리 이상)");
                    }else {
                        flag = 1;
                        warn.setText("비밀번호가 확인되었습니다.");
                    }
                }
                else{
                    flag = 0;
                    warn.setText("비밀번호가 일치하지 않습니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.button1:
                if(!check.isChecked()) Toast.makeText(getApplicationContext(), "정보 제공에 동의하세요.", Toast.LENGTH_LONG).show();
                else {
                    if(flag == 1){
                        new registerWork(id_field.getText().toString(),pw_field.getText().toString(), name_field.getText().toString(), mail_field.getText().toString(), contact_field.getText().toString(), profile_field.getText().toString()).execute();
                        pdial.show();
                    }else{
                        Toast.makeText(getApplicationContext(), "비밀번호를 확인하세요.", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.button2:
                finish();
                break;
            case R.id.button8:
                finish();
                break;
            default: break;
        }
    }

    public void notifyReg(){
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, loginActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setTicker("클라우드북");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("CloudBook");
        builder.setContentText("클라우드북에 오신 것을 환영합니다!");
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
        //builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(2014112021, builder.build());

    }

    public class registerWork extends AsyncTask<Void, Void, Void>{
        private InputStream is;
        private String id, pw, nm, ml, ct, pr;
        private int registration;
        private String message = null, errorString = null, result=null, line=null;
        public registerWork(String id, String pw, String nm, String ml, String ct, String pr){
            this.id = id;
            this.pw = pw;
            this.nm = nm;
            this.ml = ml;
            this.ct = ct;
            this.pr = pr;
        }
        protected Void doInBackground(Void... arg0) {
            if(this.id.length()<=0 || this.pw.length()<=0 || this.nm.length()<=0 || this.ml.length()<=0 || this.ct.length()<=0 || this.pr.length()<=0){
                registration = 1;
                return null;
            }else if(this.id.length()<=5){
                registration = 4;
                return null;
            }else if(this.id.equals("#ABANDONED")){
                registration = 5;
                return null;
            }else if((this.id.indexOf(" ")!=-1) || this.pw.indexOf(" ")!=-1){
                registration = 6;
                return null;
            }
            registration = -1;
            result = null;
            line = null;
            message = null;
            errorString = null;
            ArrayList<NameValuePair> nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair("id", this.id));
            nameValuePairs.add(new BasicNameValuePair("pw", this.pw));
            nameValuePairs.add(new BasicNameValuePair("nm", this.nm));
            nameValuePairs.add(new BasicNameValuePair("ml", this.ct));
            nameValuePairs.add(new BasicNameValuePair("ct", this.ml));
            nameValuePairs.add(new BasicNameValuePair("pr", this.pr));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://yjham2002.woobi.co.kr/cb_directory/appRegister.php");
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
                registration = (Integer.parseInt(json_data.getString("Auth")));
            } catch (Exception e) {
                errorString += "JSON : " + e.toString() + '\n';
            }
            return null;
        }protected void onPostExecute(Void result) {
            switch(registration){
                case 0: message = "회원가입이 완료되었습니다."; break;
                case 1: message = "모든 양식을 입력해주세요."; break;
                case 2: message = "이미 존재하는 아이디입니다."; break;
                case 4: message = "아이디는 6자 이상이어야 합니다."; break;
                case 5: message = "사용할 수 없는 아이디입니다."; break;
                case 6: message = "아이디와 비밀번호는 공백을 사용할 수 없습니다."; break;
                default: message = "잘못된 접근이거나 네트워크에 연결할 수 없습니다."; break;
            }
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            pdial.dismiss();
            if(registration == 0) {
                notifyReg();
                finish();
            }
            registration = 0;
        }
    }


}
