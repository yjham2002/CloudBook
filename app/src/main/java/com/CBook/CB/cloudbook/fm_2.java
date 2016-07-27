package com.CBook.CB.cloudbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.*;
import android.widget.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class fm_2 extends Fragment {

    private RecyclerView mRecycle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    View rootView = inflater.inflate(R.layout.fm_2, container, false);

        mRecycle = (RecyclerView) rootView.findViewById(R.id.listView);

        staticInfo.mAdapter = new ListViewAdapterM(rootView.getContext(), R.layout.listview_item);
        mRecycle.setAdapter(staticInfo.mAdapter);
        mRecycle.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecycle.setItemAnimator(new DefaultItemAnimator());

        new sel_list2(new AsyncCallback() {
            @Override
            public void callback() {
                staticInfo.mAdapter.dataChange();
            }}).execute();
		return rootView;
	}

}

