package com.CBook.CB.cloudbook;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ChatClientActivity extends BaseActivity implements Button.OnClickListener{

    private EditText msg;
    private TextView title;
    private ListView chat;
    private Button exit, send;
    private ChatAdapter cAdapter;

    SQLiteDatabase database;
    String dbName = "CloudBook";
    String createTable = "create table chatList (byme integer, idx integer, isSent integer, msg text, date text, caller text, myname text);";

    public void createDatabase(){
        database = openOrCreateDatabase(dbName, android.content.Context.MODE_PRIVATE, null);
    }

    public void createTable(){
        try{
            database.execSQL(createTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertData(boolean byme, int idx, int isSent, String msg, String date, String caller, String myname){
        String sqla = "select MAX(idx) from chatList";
        int idxnum = (int)database.compileStatement("select max(idx) FROM chatList").simpleQueryForLong();
        database.beginTransaction();
        int by = byme ? 1 : 0;
        try{
            String sql = "insert into chatList values ("+ by +", " + (++idxnum) + ", "+ isSent + ", '" + msg + "', '"+ date +"' ,'" + caller + "', '"+ myname +"');";
            database.execSQL(sql);
            database.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            database.endTransaction();
        }
    }

    public void selectData(){
        String sql = "select * from chatList where caller = '" + staticInfo.caller + "' and myname = '"+ staticInfo.netID +"' order by idx asc";
        Cursor result = database.rawQuery(sql, null);
        result.moveToFirst();
        cAdapter.mListData.clear();
        while(!result.isAfterLast()){
            cAdapter.addItem(result.getString(0).charAt(0)-'0'==1, Integer.parseInt(result.getString(1)), result.getString(2).charAt(0)-'0',result.getString(3), result.getString(4), result.getString(5));
            result.moveToNext();
        }
        cAdapter.dataChange();
        result.close();
    }

    public void dropTable(){
        database.beginTransaction();
        try{
            String sql = "drop table chatList";
            database.execSQL(sql);
            database.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            database.endTransaction();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        staticInfo.isRun = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        View root = findViewById(R.id.rootLay);
        root.requestFocus();
        createDatabase();
        createTable();

        staticInfo.isRun = 1;
        NotificationManager nMgr = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(2014112021);

        msg = (EditText)findViewById(R.id.msg);
        title = (TextView)findViewById(R.id.title);
        chat = (ListView)findViewById(R.id.chat);
        exit = (Button)findViewById(R.id.exit);
        send = (Button)findViewById(R.id.send);

        exit.setOnClickListener(this);
        send.setOnClickListener(this);

        cAdapter = new ChatAdapter(this);

        chat.setAdapter(cAdapter);

        selectData();

        title.setText(staticInfo.caller);

        chat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int position = cAdapter.mListData.get(arg2).idx;
                final String sql = "delete from chatList where idx = " + position;
                database.execSQL(sql);
                selectData();
                return false;
            }
        });

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.exit:
                finish();
                break;
            case R.id.send:
                if(msg.getText().length()<=0) break;
                else {
                    Calendar cal = Calendar.getInstance();
                    String dateSet = Integer.toString(cal.get(Calendar.YEAR))+"-"+Integer.toString(cal.get(Calendar.MONTH)+1)+"-"+
                            Integer.toString(cal.get(Calendar.DAY_OF_MONTH))+" "+Integer.toString(cal.get(Calendar.HOUR_OF_DAY))+":"+Integer.toString(cal.get(Calendar.MINUTE))+":"+Integer.toString(cal.get(Calendar.SECOND));
                    insertData(true, 0, 0, msg.getText().toString(), dateSet, staticInfo.caller, staticInfo.netID);
                    selectData();
                    new sendMessage(new AsyncCallback() {
                        @Override
                        public void callback() {
                        }}).execute(msg.getText().toString());

                    msg.setText("");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("my-event"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            selectData();

        }
    };

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

}
