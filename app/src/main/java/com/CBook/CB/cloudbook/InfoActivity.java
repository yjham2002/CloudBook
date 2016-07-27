package com.CBook.CB.cloudbook;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class InfoActivity extends BaseActivity implements View.OnClickListener{
    private Button back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        back = (Button)findViewById(R.id.button8);
        back.setOnClickListener(this);
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.button8:
                finish();
                break;
            default: break;
        }
    }
}
