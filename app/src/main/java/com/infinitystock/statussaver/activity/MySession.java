package com.infinitystock.statussaver.activity;

import android.content.Context;
import android.content.SharedPreferences;

public class MySession {
    Context context;

    private String ISPurchase = "IsPurchase";

    public MySession(Context context) {
        this.context = context;
    }

    public void setUserPurchase(boolean IsPurchase) {
        if (context != null) {
            SharedPreferences LoginPref = context.getSharedPreferences("inAppPrefs",context.MODE_PRIVATE);
            SharedPreferences.Editor editor = LoginPref.edit();
            editor.putBoolean(ISPurchase,IsPurchase);
            editor.apply();
        }
    }

    public boolean isUserPurchased(){
        if (context != null) {
            SharedPreferences LoginPref = context.getSharedPreferences("inAppPrefs",context.MODE_PRIVATE);
            return LoginPref.getBoolean(ISPurchase, false);
        } else {
            return false;
        }
    }






}
