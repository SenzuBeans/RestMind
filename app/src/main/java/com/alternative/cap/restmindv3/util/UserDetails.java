package com.alternative.cap.restmindv3.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserDetails implements Parcelable {

    public String name;
    public String email;
    public ArrayList<BreathLogItem> breath_log;
    public ArrayList<StepLogItem> step_log;
    public int temp_steam;

    public UserDetails() {
    }

    public UserDetails(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserDetails(String name, String email, ArrayList<BreathLogItem> breath_log, ArrayList<StepLogItem> step_log) {
        this.name = name;
        this.email = email;
        this.breath_log = breath_log;
        this.step_log = step_log;
    }

    protected UserDetails(Parcel in) {
        name = in.readString();
        email = in.readString();
        breath_log = in.createTypedArrayList( BreathLogItem.CREATOR );
        step_log = in.createTypedArrayList( StepLogItem.CREATOR );
        temp_steam = in.readInt();
    }

    public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel in) {
            return new UserDetails( in );
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<BreathLogItem> getBreath_log() {
        return breath_log;
    }

    public void setBreath_log(ArrayList<BreathLogItem> breath_log) {
        this.breath_log = breath_log;
    }

    public ArrayList<StepLogItem> getStep_log() {
        return step_log;
    }

    public void setStep_log(ArrayList<StepLogItem> step_log) {
        this.step_log = step_log;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString( name );
        parcel.writeString( email );
        parcel.writeTypedList( breath_log );
        parcel.writeTypedList( step_log );
        parcel.writeInt( temp_steam );
    }
}
