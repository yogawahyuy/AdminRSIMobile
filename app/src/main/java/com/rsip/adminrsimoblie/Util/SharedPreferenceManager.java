package com.rsip.adminrsimoblie.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {

    public static final String SP_ADMIN_RSI="spAdminRSI";
    public static final String SP_USERNAME="spUsername";
    public static final String SP_NAMA="spNama";
    public static final String SP_BAGIAN="spBagian";
    public static final String SP_SUDAH_LOGIN="spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPreferenceManager(Context context) {
        sp = context.getSharedPreferences(SP_ADMIN_RSI,Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPNama(){
        return sp.getString(SP_NAMA, "");
    }

    public String getSPUsername(){
        return sp.getString(SP_USERNAME, "");
    }
    public String getSPBagian(){
        return sp.getString(SP_BAGIAN, "");
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }
}
