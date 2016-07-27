package com.CBook.CB.cloudbook;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class fm_1 extends Fragment implements View.OnClickListener{

    private EditText sView;
    private ImageView go;
    private String lat, lon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fm_1, container, false);

        sView = (EditText) rootView.findViewById(R.id.sView);

        go = (ImageView) rootView.findViewById(R.id.go);
        go.setOnClickListener(this);

        return rootView;
    }

    public void onClick(View v) {
        sView.setError(null);
        switch(v.getId()){
            case R.id.go:
                if(sView.getText().length()>=10 && sView.getText().length()<=100) {
                    Calendar cal = Calendar.getInstance();
                    String dateSet = Integer.toString(cal.get(Calendar.YEAR))+"-"+Integer.toString(cal.get(Calendar.MONTH)+1)+"-"+
                            Integer.toString(cal.get(Calendar.DAY_OF_MONTH))+" "+Integer.toString(cal.get(Calendar.HOUR_OF_DAY))+":"+Integer.toString(cal.get(Calendar.MINUTE))+":"+Integer.toString(cal.get(Calendar.SECOND));
                    GpsInfo gps = new GpsInfo(getActivity().getBaseContext());
                    lat = Double.toString(gps.getLatitude());
                    lon = Double.toString(gps.getLongitude());

                    HashMap<String, String> nameValuePairs = new HashMap<>();
                    nameValuePairs.put("id", staticInfo.getID());
                    nameValuePairs.put("date", dateSet);
                    nameValuePairs.put("contents", sView.getText().toString());
                    nameValuePairs.put("lat", lat);
                    nameValuePairs.put("lon", lon);
                    new Communicator().postHttp(staticInfo.URL_UPLOAD_ART, nameValuePairs, new Handler(){
                        @Override
                        public void handleMessage(Message msg){
                            sView.setText("");
                            Toast.makeText(getActivity().getBaseContext(),"게시됨", Toast.LENGTH_SHORT).show();
                            new sel_list2(new AsyncCallback() {
                                @Override
                                public void callback() {
                                    //staticInfo.mAdapter.dataChange();
                                }}).execute();
                            if(staticInfo.whoscall == 1) {
                                new sel_list4(new AsyncCallback() {
                                    @Override
                                    public void callback() {
                                        //staticInfo.mAdapterMe.dataChange();
                                    }
                                }).execute();
                            }

                        }
                    });
                }
                else {
                    sView.setError("10~100 바이트만 입력이 가능합니다.");
                }
                break;
            default: break;
        }
    }
}
