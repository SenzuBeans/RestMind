package com.alternative.cap.restmindv3.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class StepLogItem implements Parcelable {

    public String stepId;
    public String stepCount;

    public StepLogItem() {
    }

    public StepLogItem(String stepId, String stepCount) {
        this.stepId = stepId;
        this.stepCount = stepCount;
    }

    protected StepLogItem(Parcel in) {
        stepId = in.readString();
        stepCount = in.readString();
    }

    public static final Creator<StepLogItem> CREATOR = new Creator<StepLogItem>() {
        @Override
        public StepLogItem createFromParcel(Parcel in) {
            return new StepLogItem( in );
        }

        @Override
        public StepLogItem[] newArray(int size) {
            return new StepLogItem[size];
        }
    };

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getStepCount() {
        return stepCount;
    }

    public void setStepCount(String stepCount) {
        this.stepCount = stepCount;
    }

    public void updateStep(){
        int x = Integer.parseInt(this.stepCount) + 1;
        this.stepCount = (x) + "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString( stepId );
        parcel.writeString( stepCount );
    }
}
