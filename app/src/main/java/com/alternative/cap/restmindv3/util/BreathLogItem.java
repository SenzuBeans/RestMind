package com.alternative.cap.restmindv3.util;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class BreathLogItem {

    public String date;
    public String totalTime;

    public BreathLogItem() {
    }

    public BreathLogItem(String date, String totalTime) {
        this.date = date;
        this.totalTime = totalTime;
    }
}
