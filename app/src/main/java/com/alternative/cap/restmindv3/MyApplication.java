package com.alternative.cap.restmindv3;

import android.app.Application;

import androidx.multidex.MultiDexApplication;

import com.alternative.cap.restmindv3.manager.Contextor;

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Contextor.getInstance().init(getApplicationContext());
    }
}
