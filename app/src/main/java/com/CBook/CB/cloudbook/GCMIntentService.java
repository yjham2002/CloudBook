package com.CBook.CB.cloudbook;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.Calendar;

public class GCMIntentService extends IntentService {
    private String LOGKEY = "[GcmIntentService.java]";
    private WakeLock screenWakeLock;
    private String friend_id;
    public static final int NOTIFICATION_ID = 1;
    public static final String PROJECT_ID = "437375558310";

    SQLiteDatabase database;
    String dbName = "CloudBook";
    String createTable = "create table chatList (byme integer, idx integer, isSent integer, msg text, date text, caller text, myname text);";

    public GCMIntentService() {
        super("GcmIntentService");
    }

    private void sendMessage() {
        Intent intent = new Intent("my-event");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences prefs = getSharedPreferences("CloudBook", MODE_PRIVATE);
        Log.e("GcmItentService","onHandleIntent!");
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if(! extras.isEmpty())
        {
            if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
            {
                Log.e("GcmIntentService","Send error : "+extras.toString());
            }
            else if(GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
            {
                Log.e("GcmIntentService","Deleted messages on server : "+extras.toString());
            }
            else if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {
                if(extras.getString("forward").equals("#ABANDONED") && prefs.getBoolean("push_req", true)) {
                    //Log.e("GcmIntentService","onHandleIntent received : "+extras.toString());
                    notifyReg(extras.getString("title"), extras.getString("message"), false);
                }
                else{

                    createDatabase();
                    createTable();

                    Calendar cal = Calendar.getInstance();
                    String dateSet = Integer.toString(cal.get(Calendar.YEAR))+"-"+Integer.toString(cal.get(Calendar.MONTH)+1)+"-"+
                            Integer.toString(cal.get(Calendar.DAY_OF_MONTH))+" "+Integer.toString(cal.get(Calendar.HOUR_OF_DAY))+":"+Integer.toString(cal.get(Calendar.MINUTE))+":"+Integer.toString(cal.get(Calendar.SECOND));
                    insertData(false, 0, 0, extras.getString("message"), dateSet, extras.getString("forward"), extras.getString("myname"));

                    if(staticInfo.isRun != 1 && prefs.getBoolean("push_msg", true)) notifyReg(extras.getString("title"), extras.getString("forward") + "님으로부터의 메세지", true);
                    sendMessage();
                }
            }
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

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



    public void notifyReg(String title, String msg, boolean isMsg){
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setTicker("클라우드북");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);

        if(isMsg) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, IntroActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
        }

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(isMsg) nm.notify(2014112021, builder.build());
        else nm.notify(2014112022, builder.build());
    }

}

