package com.alternative.cap.restmindv3.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class BreathLogItem implements Parcelable {

    public String date;
    public String totalTime;
    public String dismissTime = "0";

    public BreathLogItem() {
    }

    public BreathLogItem(String date, String totalTime) {
        this.date = date;
        this.totalTime = totalTime;
    }

    public BreathLogItem(String date, String totalTime, String dismissTime) {
        this.date = date;
        this.totalTime = totalTime;
        this.dismissTime = dismissTime;
    }

    protected BreathLogItem(Parcel in) {
        date = in.readString();
        totalTime = in.readString();
        dismissTime = in.readString();
    }

    public static final Creator<BreathLogItem> CREATOR = new Creator<BreathLogItem>() {
        @Override
        public BreathLogItem createFromParcel(Parcel in) {
            return new BreathLogItem( in );
        }

        @Override
        public BreathLogItem[] newArray(int size) {
            return new BreathLogItem[size];
        }
    };

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

    public String getDismissTime() {
        return dismissTime;
    }

    public void setDismissTime(String dismissTime) {
        this.dismissTime = dismissTime;
    }

    public void updateTotalTime(long time) {
        int minte = (int) (time/60);
        int current = Integer.parseInt(this.totalTime);
        this.totalTime = (current + (minte/1000)) + "";
    }

    public void updateDismissTime(long time) {

        int minte = (int) (time/60);
        int current = Integer.parseInt(this.dismissTime);
        this.dismissTime = (current + ((minte+1000) / 1000)) + "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString( date );
        parcel.writeString( totalTime );
        parcel.writeString( dismissTime );
    }
}
