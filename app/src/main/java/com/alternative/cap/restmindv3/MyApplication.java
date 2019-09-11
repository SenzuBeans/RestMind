package com.alternative.cap.restmindv3;

import android.app.Application;

import com.alternative.cap.restmindv3.manager.Contextor;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Contextor.getInstance().init(getApplicationContext());
    }
}
