package com.CBook.CB.cloudbook;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class myPageActivity extends BaseActivity implements View.OnClickListener{

    private ImageView bt_close;
    private TextView name, email;
    private Button menuq;

    private LinearLayout scroller;
    private ParallaxScrollView pscroll;

    public void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_exit:
                finish();
                break;
            case R.id.menuq:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("정말 탈퇴하시겠습니까?");
                builder.setCancelable(true);
                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new executeQ(new AsyncCallback() {
                                    @Override
                                    public void callback() {
                                        Toast.makeText(getApplicationContext(), "탈퇴 처리가 완료되었습니다.", Toast.LENGTH_LONG).show();
                                        SharedPreferences prefs = getSharedPreferences("CloudBook", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putBoolean("Auto", false);
                                        editor.putString("ID", null);
                                        editor.putString("PW", null);
                                        editor.commit();
                                        Intent i = new Intent(myPageActivity.this, loginActivity.class);
                                        startActivity(i);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        finish();
                                    }
                                }).execute("delete from `dcse_ac` where `id`= '" + staticInfo.netID +"'");

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        final SharedPreferences prefs = getSharedPreferences("WMN_PREF", MODE_PRIVATE);
        final SharedPreferences.Editor prefEditor = prefs.edit();

        scroller = (LinearLayout)findViewById(R.id.scroller);
        pscroll = (ParallaxScrollView)findViewById(R.id.pscroll);

        name = (TextView)findViewById(R.id.Me);
        email = (TextView)findViewById(R.id.Mail);

        menuq = (Button)findViewById(R.id.menuq);
        menuq.setOnClickListener(this);

        name.setText(staticInfo.netID);
        email.setText(staticInfo.netEM);

        bt_close = (ImageView)findViewById(R.id.bt_exit);
        bt_close.setOnClickListener(this);

        new SizeS().execute();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private class SizeS extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(pscroll.getHeight() > scroller.getHeight()) scroller.getLayoutParams().height = pscroll.getHeight();
            //parallex.getLayoutParams().height = parentView.getHeight();
            pscroll.setTopBound(pscroll.getHeight());
            super.onPostExecute(result);
        }
    }

}
