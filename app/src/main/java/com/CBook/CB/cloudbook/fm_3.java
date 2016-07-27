package com.CBook.CB.cloudbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

public class fm_3 extends Fragment{

    public RecyclerView mRecycle = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fm_3, container, false);

        mRecycle = (RecyclerView) rootView.findViewById(R.id.listView);
        staticInfo.mAdaptergo = new ListViewAdapterM(rootView.getContext(), R.layout.listview_item, 1);
        mRecycle.setAdapter(staticInfo.mAdaptergo);
        mRecycle.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecycle.setItemAnimator(new DefaultItemAnimator());

        new sel_list3(new AsyncCallback() {
            @Override
            public void callback() {
                staticInfo.mAdaptergo.dataChange();
            }}).execute();

        return rootView;
    }

}
