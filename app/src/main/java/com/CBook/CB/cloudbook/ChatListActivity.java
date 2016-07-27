package com.CBook.CB.cloudbook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class ChatListActivity extends BaseActivity implements View.OnClickListener{

    private Button back;
    private ListView mList;
    private ChatListAdapter cAdapter;

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

    public boolean isAlreadyExist(String caller){
        for(int i = 0; i < cAdapter.mListData.size(); i++){
            if(cAdapter.mListData.get(i).caller.equals(caller)) return true;
        }
        return false;
    }

    public void selectData(){
        String sql = "select * from chatList where myname = '"+ staticInfo.netID +"' order by idx desc";
        Cursor result = database.rawQuery(sql, null);
        result.moveToFirst();
        cAdapter.mListData.clear();
        while(!result.isAfterLast()){
            if(!isAlreadyExist(result.getString(5))) cAdapter.addItem(result.getString(5), result.getString(3), false);
            result.moveToNext();
        }
        cAdapter.dataChange();
        result.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

        createDatabase();
        createTable();

        back = (Button)findViewById(R.id.button8);
        mList = (ListView)findViewById(R.id.listView2);
        mList.setEmptyView(findViewById(R.id.empty));

        back.setOnClickListener(this);

        cAdapter = new ChatListAdapter(this);

        mList.setAdapter(cAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ChatListData mData = cAdapter.mListData.get(position);
                staticInfo.caller = mData.caller;
                Intent ii = new Intent(ChatListActivity.this, ChatClientActivity.class);
                startActivity(ii);
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        selectData();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button8:
                finish();
                break;
        }
    }
}
