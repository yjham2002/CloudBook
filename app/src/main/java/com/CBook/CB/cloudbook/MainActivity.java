package com.CBook.CB.cloudbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import static android.view.Gravity.START;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

	MainActivity.SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	private Button menu1, menu2, menu3, menum, menus;
	private TextView tv1;
	private DrawerArrowDrawable drawerArrowDrawable;
	private float offset;
	private boolean flipped;

	private String SENDER_ID = "437375558310";
	private GoogleCloudMessaging gcm;
	private String regid;


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

			}
		}.execute(null, null, null);
	}

	private void sendRegistrationIdToBackend() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer);

		gcm = GoogleCloudMessaging.getInstance(this);
		registerInBackground();

		menu1 = (Button)findViewById(R.id.menu1);
		menu2 = (Button)findViewById(R.id.menu2);
		menu3 = (Button)findViewById(R.id.menu3);
		menum = (Button)findViewById(R.id.menum);
		menus = (Button)findViewById(R.id.menus);

		menu1.setOnClickListener(this);
		menu2.setOnClickListener(this);
		menu3.setOnClickListener(this);
		menum.setOnClickListener(this);
		menus.setOnClickListener(this);

		tv1 = (TextView)findViewById(R.id.textView1); // Get textView instance from view

		final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		final ImageView imageView = (ImageView) findViewById(R.id.drawer_indicator);
		final Resources resources = getResources();
		drawerArrowDrawable = new DrawerArrowDrawable(resources);
		drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.white));
		imageView.setImageDrawable(drawerArrowDrawable);

		mSectionsPagerAdapter = new MainActivity.SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		final ProgressDialog pdial = new ProgressDialog(this);
		pdial.setCancelable(true);
		pdial.setMessage("계정 정보를 가져오는중...");
		pdial.show();

		new getInfo(new AsyncCallback() {
			@Override
			public void callback() {
				pdial.dismiss();
				tv1.setText(staticInfo.netID);
				if(staticInfo.netID == null){
					SharedPreferences prefs = getSharedPreferences("CloudBook", MODE_PRIVATE);
					final SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean("Auto", false);
					editor.commit();
					Intent i = new Intent(MainActivity.this, loginActivity.class);
					startActivity(i);
					Toast.makeText(getApplicationContext(), "인터넷에 연결할 수 없습니다.", Toast.LENGTH_LONG).show();
					finish();
				}
				GpsInfo gps = new GpsInfo(MainActivity.this);
				staticInfo.latitude = gps.getLatitude();
				staticInfo.longitude = gps.getLongitude();
				new executeQ(new AsyncCallback() {
					@Override
					public void callback() {
					}}).execute("update `dcse_ac` set `regId`='"+ regid +"' where `id`='"+staticInfo.getID().toString()+"'");
			}
		}).execute();

		if(staticInfo.getID().equals("admin") || staticInfo.getID().equals("CloudBook")){
			Intent i = new Intent(MainActivity.this, adminActivity.class);
			startActivity(i);
		}else{
			SharedPreferences prefs = getSharedPreferences("CloudBook", MODE_PRIVATE);
			if(prefs.getBoolean("tutorial", true)) {
				//Intent i = new Intent(MainActivity.this, tutorialActivity.class);
				//startActivity(i);
			}
		}

		drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				offset = slideOffset;

				// Sometimes slideOffset ends up so close to but not quite 1 or 0.
				if (slideOffset >= .995) {
					flipped = true;
					drawerArrowDrawable.setFlip(flipped);
				} else if (slideOffset <= .005) {
					flipped = false;
					drawerArrowDrawable.setFlip(flipped);
				}

				drawerArrowDrawable.setParameter(offset);
			}
		});
// gravity compat added
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (drawer.isDrawerVisible(GravityCompat.START)) {
					drawer.closeDrawer(GravityCompat.START);
				} else {
					drawer.openDrawer(GravityCompat.START);
				}
			}
		});

		Util.setGlobalFont(this, getWindow().getDecorView());

	}

	public void onClick(View v){
		switch(v.getId()){
			case R.id.menus:
				Intent bb = new Intent(MainActivity.this, settingActivity.class);
				startActivity(bb);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				break;
			case R.id.menu1:
				Intent b = new Intent(MainActivity.this, myPageActivity.class);
				startActivity(b);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				break;
			case R.id.menu2:
				Intent q = new Intent(MainActivity.this, InfoActivity.class);
				startActivity(q);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				break;
			case R.id.menum:
				Intent aaa = new Intent(MainActivity.this, ChatListActivity.class);
				startActivity(aaa);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				break;
			case R.id.menu3:
				SharedPreferences prefs = getSharedPreferences("CloudBook", MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putBoolean("Auto", false);
				editor.putString("ID", null);
				editor.putString("PW", null);
				editor.commit();
				Intent i = new Intent(MainActivity.this, loginActivity.class);
				startActivity(i);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
				break;
			default: break;
		}
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment temp;
			switch(position){
				case 0: temp = new fm_1(); break;
				case 1: temp = new fm_2(); break;
				case 2: temp = new fm_3(); break;
				default: temp = null; break;
			}
			return temp;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String title = null;
			switch (position) {
				case 0:
					title = "Request";
					break;
				case 1:
					title = "All";
					break;
				case 2:
					title = "Log";
					break;
				default: break;

			}
			/*
			SpannableStringBuilder sb = new SpannableStringBuilder("   "); // space added before text for convenience
			try {
				myDrawable.setBounds(1, 1, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
				ImageSpan span = new ImageSpan(myDrawable, DynamicDrawableSpan.ALIGN_BASELINE);
				sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} catch (Exception e) {
			}*/
			return title;
		}
	}

	public boolean mFlag;

	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what == 0){
				mFlag=false;
			}
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)){
			if(!mFlag) {
				Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
				mFlag = true;
				mHandler.sendEmptyMessageDelayed(0, 2000);
				return false;
			} else {
				finish();
				System.exit(0);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
