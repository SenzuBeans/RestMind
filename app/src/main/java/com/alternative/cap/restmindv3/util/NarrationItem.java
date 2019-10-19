package com.alternative.cap.restmindv3.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class NarrationItem implements Parcelable {

    public String rawId;

    public NarrationItem() {
    }

    public NarrationItem(String rawData) {
        this.rawId = rawData;
    }

    protected NarrationItem(Parcel in) {
        rawId = in.readString();
    }

    public static final Creator<NarrationItem> CREATOR = new Creator<NarrationItem>() {
        @Override
        public NarrationItem createFromParcel(Parcel in) {
            return new NarrationItem( in );
        }

        @Override
        public NarrationItem[] newArray(int size) {
            return new NarrationItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString( rawId );
    }
}
