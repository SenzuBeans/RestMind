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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public void updateTotalTime(long time) {
        int minte = (int) (time/60);
        int current = Integer.parseInt(this.totalTime);
        this.totalTime = (current + (minte/1000)) + "";
    }
}
