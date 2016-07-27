package com.CBook.CB.cloudbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {
    public Context mContext = null;
    public ArrayList<ChatItems> mListData = new ArrayList<>();

    public ChatAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }
    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(boolean byme, int idx, int isSent, String msg, String date, String caller){
        ChatItems addInfo = new ChatItems();
        addInfo.byme = byme;
        addInfo.isSent = isSent;
        addInfo.msg = msg;
        addInfo.date = date;
        addInfo.caller = caller;
        addInfo.idx = idx;
        mListData.add(addInfo);
    }

    public void dataChange(){
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ChatItems mData = mListData.get(position);
        ViewHolder holder;

        holder = new ViewHolder();

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(mData.byme) convertView = inflater.inflate(R.layout.chatbox_me, null);
        else convertView = inflater.inflate(R.layout.chatbox_you, null);

        holder.mText = (TextView) convertView.findViewById(R.id.mText);
        holder.mDate = (TextView) convertView.findViewById(R.id.mDate);
        convertView.setTag(holder);
        holder.mText.setText(mData.msg);
        holder.mDate.setText(mData.date);

        return convertView;
    }

    private class ViewHolder {
        public TextView mText;
        public TextView mDate;
    }

}