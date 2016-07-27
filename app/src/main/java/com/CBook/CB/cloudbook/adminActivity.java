package com.CBook.CB.cloudbook;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class adminActivity extends BaseActivity {

    TextView resultset;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);
        ImageView im = (ImageView)findViewById(R.id.imageView3);
        im.setImageBitmap(new NetworkImage("http://yjham2002.woobi.co.kr/cb_directory/images/back.png").getImage());
        resultset = (TextView)findViewById(R.id.textView15);
        Button exec = (Button)findViewById(R.id.button1);
        Button pre1 = (Button)findViewById(R.id.button);
        Button pre2 = (Button)findViewById(R.id.button3);
        Button pre3 = (Button)findViewById(R.id.button4);
        Button pre4 = (Button)findViewById(R.id.button5);
        Button test = (Button)findViewById(R.id.button9);
        Button.OnClickListener btListener = new Button.OnClickListener(){
            EditText query = (EditText)findViewById(R.id.editText1);
            EditText idq = (EditText)findViewById(R.id.editText8);
            EditText msq = (EditText)findViewById(R.id.editText9);
            public void onClick(View v)
            {
                switch(v.getId()){
                    case R.id.button1 :
                        new executeQ(new AsyncCallback() {
                            @Override
                            public void callback() {
                                resultset.setText(staticInfo.error);
                                Toast.makeText(getApplicationContext(),"Query executed", Toast.LENGTH_SHORT).show();
                            }}).execute(query.getText().toString());
                        break;
                    case R.id.button :
                        query.setText(query.getText().toString() + "delete from `dcse_contact` where `index`=0;");
                        break;
                    case R.id.button3 :
                        query.setText(query.getText().toString() + "delete from `dcse_article` where `index`=0;");
                        break;

                    case R.id.button4 :
                        query.setText(query.getText().toString() + "optimize table `dcse_contact`");
                        break;

                    case R.id.button5 :
                        query.setText(query.getText().toString() + "optimize table `dcse_article`");
                        break;
                    case R.id.button9 :
                        if(idq.getText().length()<=0) {
                            staticInfo.caller = msq.getText().toString();
                            Intent ii = new Intent(adminActivity.this, ChatClientActivity.class);
                            startActivity(ii);
                            break;
                        }
                        new sendPush(new AsyncCallback() {
                            @Override
                            public void callback() {
                                resultset.setText(staticInfo.error);
                                Toast.makeText(getApplicationContext(),"Push sent", Toast.LENGTH_SHORT).show();
                            }}).execute(idq.getText().toString(), msq.getText().toString());
                        break;
                }
            }};
        exec.setOnClickListener(btListener);
        pre1.setOnClickListener(btListener);
        pre2.setOnClickListener(btListener);
        pre3.setOnClickListener(btListener);
        pre4.setOnClickListener(btListener);
        test.setOnClickListener(btListener);
    }

}
