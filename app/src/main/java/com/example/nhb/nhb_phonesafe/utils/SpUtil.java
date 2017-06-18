package com.example.nhb.nhb_phonesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {

    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    public static void setConfigBoolean(Context context, String key, boolean value){
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putBoolean(key,value).commit();
    }
    public static boolean getConfigBoolean(Context context,String key){
        if(sp==null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,false);
    }

    public static void setConfigPassword(Context context, String key, String value){
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putString(key,value).commit();
    }
    public static String getConfigPassword(Context context,String key){
        if(sp==null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key,"");
    }
}
