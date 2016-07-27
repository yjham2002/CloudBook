package com.CBook.CB.cloudbook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class IntroActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
	    h = new Handler();
	    h.postDelayed(intro, 1500);
	}

	Handler h;
	
	Runnable intro = new Runnable() {
		public void run()
		{
			Intent i = new Intent(IntroActivity.this, loginActivity.class);
			startActivity(i);
			finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
	};

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		h.removeCallbacks(intro);
	}
}
