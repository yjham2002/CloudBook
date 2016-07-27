package com.CBook.CB.cloudbook;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class Util {
    public static void setGlobalFont(Context context,View view){
        if (view != null) {
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                int len = vg.getChildCount();
                for (int i = 0; i < len; i++) {
                    View v = vg.getChildAt(i);
                    if(v instanceof EditText || v instanceof TextView) {
                       // ((EditText) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/NanumGothic.ttf"));
                        ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/NanumGothic.ttf"));
                    }
                    setGlobalFont(context, v);
                }
            }
        }
    }
}
