package com.CBook.CB.cloudbook;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class gcmManager extends Activity {
    private EditText reg;
    private String SENDER_ID = "437375558310";
    private GoogleCloudMessaging gcm;
    private String regid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gcm);
        reg = (EditText)findViewById(R.id.editText7);
        gcm = GoogleCloudMessaging.getInstance(this);
        registerInBackground();
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    regid = gcm.register(SENDER_ID);
                    sendRegistrationIdToBackend();
                } catch (IOException ex) {
                }
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                reg.setText(regid);
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void sendRegistrationIdToBackend() {
        // Your implementation here.
        Log.d(null, "RegId = "+regid);

    }
}
