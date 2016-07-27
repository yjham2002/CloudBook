package com.CBook.CB.cloudbook;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;

public class settingActivity extends BaseActivity implements View.OnClickListener{

    private Button back;
    private Switch sw_msg, sw_req;
    private Switch.OnClickListener sw_listen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        back = (Button)findViewById(R.id.button8);
        sw_msg = (Switch)findViewById(R.id.switch1);
        sw_req = (Switch)findViewById(R.id.switch2);

        sw_msg.setChecked(true);
        sw_req.setChecked(true);

        SharedPreferences prefs = getSharedPreferences("CloudBook", MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        sw_msg.setChecked(prefs.getBoolean("push_msg", true));
        sw_req.setChecked(prefs.getBoolean("push_req", true));

        sw_listen = new Switch.OnClickListener(){
            public void onClick(View v){
                switch (v.getId()) {
                    case R.id.switch1:
                        editor.putBoolean("push_msg", sw_msg.isChecked());
                        editor.commit();
                        break;
                    case R.id.switch2:
                        editor.putBoolean("push_req", sw_req.isChecked());
                        editor.commit();
                        break;
                    default:
                        break;
                }
            }
        };

        sw_msg.setOnClickListener(sw_listen);
        sw_req.setOnClickListener(sw_listen);

        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.button8:
                finish();
                break;
            default:
                break;
        }
    }
}
