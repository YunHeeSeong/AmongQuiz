package com.example.amongquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    //모든 액티비티에서 인스턴스를 얻기위한 뱃드
    static final String PREF_USER_NAME = "username";
    static final String PREF_USER_PWD = "userpwd";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    //계정 정보 저장(ID저장)
    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    // 유저 PWD 저장
    public static void  setUserPwd(Context ctx, String userPwd) {
        SharedPreferences.Editor editor1 = getSharedPreferences(ctx).edit();
        editor1.putString(PREF_USER_PWD, userPwd);
        editor1.commit();
    }

    //저장된 정보 가져오기
    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
    public static String getUserPwd(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_PWD, "");
    }

    //로그아웃
    public static void clearUserName(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
