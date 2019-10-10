package com.alternative.cap.restmindv3.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class StepItem implements Parcelable {

    public String stepId;
    public int currentState;

    public StepItem() {
    }

    public StepItem(String stepId, int currentState) {
        this.stepId = stepId;
        this.currentState = currentState;
    }

    protected StepItem(Parcel in) {
        stepId = in.readString();
        currentState = in.readInt();
    }

    public static final Creator<StepItem> CREATOR = new Creator<StepItem>() {
        @Override
        public StepItem createFromParcel(Parcel in) {
            return new StepItem( in );
        }

        @Override
        public StepItem[] newArray(int size) {
            return new StepItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString( stepId );
        parcel.writeInt( currentState );
    }
}
