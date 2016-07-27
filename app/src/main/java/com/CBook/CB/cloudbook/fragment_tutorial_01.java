package com.CBook.CB.cloudbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class fragment_tutorial_01 extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            int position = getArguments().getInt("key");
            View rootView = null;
            SharedPreferences prefs = getActivity().getSharedPreferences("CloudBook", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = prefs.edit();
            switch(position){
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_tutorial_01, container, false);
                    final TextView c=(TextView)rootView.findViewById(R.id.textView4);
                    final TextView t=(TextView)rootView.findViewById(R.id.textView8);
                    final ImageView iv0 = (ImageView)rootView.findViewById(R.id.tutorial_iv);
                    iv0.setImageDrawable(getResources().getDrawable(R.drawable.tuto_a));
                    t.setText(getString(R.string.tutorial01_t));
                    c.setText(getString(R.string.tutorial01));
                    break;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_tutorial_01, container, false);
                    final TextView a=(TextView)rootView.findViewById(R.id.textView4);
                    final TextView tt=(TextView)rootView.findViewById(R.id.textView8);
                    final ImageView iv1 = (ImageView)rootView.findViewById(R.id.tutorial_iv);
                    iv1.setImageDrawable(getResources().getDrawable(R.drawable.tuto_b));
                    tt.setText(getString(R.string.tutorial02_t));
                    a.setText(getString(R.string.tutorial02));
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_tutorial_01, container, false);
                    final TextView cc=(TextView)rootView.findViewById(R.id.textView4);
                    final TextView ttt=(TextView)rootView.findViewById(R.id.textView8);
                    final ImageView iv2 = (ImageView)rootView.findViewById(R.id.tutorial_iv);
                    iv2.setImageDrawable(getResources().getDrawable(R.drawable.tuto_c));
                    ttt.setText(getString(R.string.tutorial03_t));
                    cc.setText(getString(R.string.tutorial03));
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_tutorial_02, container, false);
                    final ImageView iv3 = (ImageView)rootView.findViewById(R.id.tutorial_iv);
                    iv3.setImageDrawable(getResources().getDrawable(R.drawable.tuto_d));
                    final TextView ccc=(TextView)rootView.findViewById(R.id.textView4);
                    final TextView tttt=(TextView)rootView.findViewById(R.id.textView8);
                    tttt.setText(getString(R.string.tutorial04_t));
                    final Button start = (Button)rootView.findViewById(R.id.button);
                    start.setOnClickListener(new Button.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            editor.putBoolean("tutorial", false);
                            editor.commit();
                            getActivity().finish();
                        }
                    });
                    ccc.setText(getString(R.string.tutorial04));
                    break;
                default: break;
            }

            return rootView;
        }
}
