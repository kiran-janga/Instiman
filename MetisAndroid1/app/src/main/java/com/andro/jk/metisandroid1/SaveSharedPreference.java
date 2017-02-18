package com.andro.jk.metisandroid1;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SaveSharedPreference {

    static final String USER_ACCESS_TOKEN = "access_token";

    static final String USER_ACCESS_TYPE = "access_type";

    static final String USER_EMAIL = "user_email";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setAccessToken(Context ctx, String access_token) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_ACCESS_TOKEN, access_token);
        editor.commit();
    }

    public static String getAccessToken(Context ctx) {
        return getSharedPreferences(ctx).getString(USER_ACCESS_TOKEN, "missing");
    }

    public static void removeAccessToken(Context ctx) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_ACCESS_TOKEN, "missing");
        editor.commit();
    }


    public static void removeSharedPreference(Context ctx) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();

    }

    public static void setAccessType(Context ctx, String access_token) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_ACCESS_TYPE, access_token);
        editor.commit();
    }

    public static String getAccessType(Context ctx) {
        return getSharedPreferences(ctx).getString(USER_ACCESS_TYPE, "missing");
    }

    public static void removeAccessType(Context ctx) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_ACCESS_TYPE, "missing");
        editor.commit();
    }

    public static void setUserEmail(Context ctx, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_EMAIL, email);
        editor.commit();
    }

    public static String getUserEmail(Context ctx) {
        return getSharedPreferences(ctx).getString(USER_EMAIL, "missing");
    }

    public static void removeUserEmail(Context ctx) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_EMAIL, "missing");
        editor.commit();
    }
}
