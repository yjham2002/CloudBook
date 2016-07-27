package com.CBook.CB.cloudbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    public Context mContext = null;
    public ArrayList<ChatListData> mListData = new ArrayList<>();

    public ChatListAdapter(Context mContext) {
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

    public void addItem(String caller, String recent, boolean isRead){
        ChatListData addInfo = new ChatListData();
        addInfo.caller = caller;
        addInfo.recent = recent;
        addInfo.isRead = isRead;
        mListData.add(addInfo);
    }

    public void dataChange(){
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chatlist, null);

            holder.caller = (TextView) convertView.findViewById(R.id.caller);
            holder.recent = (TextView) convertView.findViewById(R.id.recent);
            holder.isRead = (TextView) convertView.findViewById(R.id.isRead);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final ChatListData mData = mListData.get(position);

        holder.caller.setText(mData.caller);
        holder.recent.setText(mData.recent);
        if(mData.isRead) holder.isRead.setBackgroundColor(Color.parseColor("#FFFC7373"));
        else holder.isRead.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        return convertView;
    }

    private class ViewHolder {
        public TextView caller;
        public TextView recent;
        public TextView isRead;
    }

}