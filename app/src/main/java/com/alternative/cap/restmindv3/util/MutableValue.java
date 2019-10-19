package com.alternative.cap.restmindv3.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class MutableValue implements Parcelable {

    private static long[] breathData = {3000,1000,3000,300000};

    public MutableValue() {
    }

    protected MutableValue(Parcel in) {
    }

    public static final Creator<MutableValue> CREATOR = new Creator<MutableValue>() {
        @Override
        public MutableValue createFromParcel(Parcel in) {
            return new MutableValue( in );
        }

        @Override
        public MutableValue[] newArray(int size) {
            return new MutableValue[size];
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
