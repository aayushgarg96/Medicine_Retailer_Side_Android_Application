package com.example.ayush.retailer;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Ayush on 12/21/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    static final String TAG = "abc";
    public static String token;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        token = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed Token" + token);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("sp_token", token);
        ed.commit();
    }
}
