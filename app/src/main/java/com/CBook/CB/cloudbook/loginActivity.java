package com.CBook.CB.cloudbook;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;

public class loginActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
		final ProgressDialog pdial = new ProgressDialog(this);
		pdial.setCancelable(true);
		pdial.setMessage("로그인 하는 중.....");
		staticInfo.spinItems = new ArrayList<>();
		staticInfo.spinItems.clear();
		staticInfo.spinItems.add("전체");
        Button bt01 = (Button)findViewById(R.id.button1);
        Button bt02 = (Button)findViewById(R.id.button2);
        final  CheckBox ch01 = (CheckBox)findViewById(R.id.checkBox);
        final EditText id_form = (EditText)findViewById(R.id.editText1), pw_form = (EditText)findViewById(R.id.editText2);
		SharedPreferences prefs = getSharedPreferences("CloudBook", MODE_PRIVATE);
		staticInfo.setAuthInfo(prefs.getString("ID", "Undefined"),prefs.getString("PW", "Undefined"));

		final SharedPreferences.Editor editor = prefs.edit();
        if(prefs.getBoolean("Auto", false)==true)
        {
            Intent i = new Intent(loginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
	    Button.OnClickListener btListener = new Button.OnClickListener(){
			public void onClick(View v)
			{
				switch(v.getId()){
				case R.id.button1:
					if(id_form.getText().toString().length()>0 && pw_form.getText().toString().length()>0) {
                        Authentifier infoAuth = new Authentifier(new AsyncCallback() {
							@Override
							public void callback() {
								pdial.dismiss();
								switch (staticInfo.auth)
								{
									case -1:Toast.makeText(getApplicationContext(),"인터넷에 연결할 수 없습니다.",Toast.LENGTH_SHORT).show(); break;
									case 0:Toast.makeText(getApplicationContext(),"존재하지 않는 아이디입니다.",Toast.LENGTH_SHORT).show(); break;
									case 1:Toast.makeText(getApplicationContext(),"유효하지 않은 비밀번호입니다.",Toast.LENGTH_SHORT).show(); break;
									case 2:break;
									default:Toast.makeText(getApplicationContext(),"잘못된 접근입니다.",Toast.LENGTH_SHORT).show(); break;
								}
								if(staticInfo.auth==2) {
									if(ch01.isChecked()) editor.putBoolean("Auto", true);
									else editor.putBoolean("Auto", false);
									editor.putString("ID", id_form.getText().toString());
									editor.putString("PW", pw_form.getText().toString());
									editor.commit();
									staticInfo.setAuthInfo(id_form.getText().toString(), pw_form.getText().toString());
									Intent i = new Intent(loginActivity.this, MainActivity.class);
									startActivity(i);
									finish();
									overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
								}
							}
						});
						infoAuth.insertInfo(id_form.getText().toString(), pw_form.getText().toString());
						infoAuth.execute();
						pdial.show();
					}
					else
					{Toast.makeText(getApplicationContext(),"내용을 입력하세요",Toast.LENGTH_SHORT).show();}

					break;
				case R.id.button2:
					Intent goReg = new Intent(loginActivity.this, RegisterActivity.class);
					startActivity(goReg);
					break;
			}
		}};
	    bt01.setOnClickListener(btListener);
	    bt02.setOnClickListener(btListener);
	}
}
