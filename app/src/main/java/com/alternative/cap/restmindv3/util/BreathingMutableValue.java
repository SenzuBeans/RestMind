package com.alternative.cap.restmindv3.util;

import android.os.Parcel;
import android.os.Parcelable;

public class BreathingMutableValue implements Parcelable {

    private static long[] breathData = {3000,1000,3000,300000};

    public BreathingMutableValue() {
    }

    protected BreathingMutableValue(Parcel in) {
    }

    public static final Creator<BreathingMutableValue> CREATOR = new Creator<BreathingMutableValue>() {
        @Override
        public BreathingMutableValue createFromParcel(Parcel in) {
            return new BreathingMutableValue( in );
        }

        @Override
        public BreathingMutableValue[] newArray(int size) {
            return new BreathingMutableValue[size];
        }
    };

    public long[] getBreathData() {
        return breathData;
    }

    public void setBreathData(long[] passingBreathData) {
        breathData = passingBreathData;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
