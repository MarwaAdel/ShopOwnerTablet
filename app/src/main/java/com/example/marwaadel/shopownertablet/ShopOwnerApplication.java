package com.example.marwaadel.shopownertablet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.marwaadel.shopownertablet.utils.Constants;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

/**
 * Includes one-time initialization of Firebase related code
 */
public class ShopOwnerApplication extends android.app.Application {
    SharedPreferences pref;
    SharedPreferences.Editor editor;


String mListId;
    @Override
    public void onCreate() {
        super.onCreate();
   /* Initialize Firebase */
        Firebase.setAndroidContext(this);


    }

}