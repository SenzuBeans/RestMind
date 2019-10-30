package com.alternative.cap.restmindv3.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class UserDetails implements Parcelable {

    public String name;
    public String email;
    public ArrayList<BreathLogItem> breath_log;
    public ArrayList<StepLogItem> step_log;
    public String totalTime;
    public String missTime;
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

    public UserDetails(String name, String email, ArrayList<BreathLogItem> breath_log, ArrayList<StepLogItem> step_log, String totalTime, String missTime) {
        this.name = name;
        this.email = email;
        this.breath_log = breath_log;
        this.step_log = step_log;
        this.totalTime = totalTime;
        this.missTime = missTime;
    }

    public static Comparator<UserDetails> userDetailsComparator = new Comparator<UserDetails>() {
        @Override
        public int compare(UserDetails o1, UserDetails o2) {
            if (o1.totalTime != null && o2.totalTime!= null) {
                return o1.totalTime.compareTo(o2.totalTime);
            }else if (o1.totalTime == null && o2.totalTime != null){
                return -1;
            }else if (o1.totalTime != null && o2.totalTime == null){
                return 1;
            }else{
                return 0;
            }
        }
    };

    protected UserDetails(Parcel in) {
        name = in.readString();
        email = in.readString();
        breath_log = in.createTypedArrayList(BreathLogItem.CREATOR);
        step_log = in.createTypedArrayList(StepLogItem.CREATOR);
        totalTime = in.readString();
        missTime = in.readString();
        temp_steam = in.readInt();
    }

    public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel in) {
            return new UserDetails(in);
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

    public String getTotalTime() {
        return totalTime;
    }

    public void updateTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public void updateTotalTime(long totalTime) {
        int minte = (int) (totalTime/60000);
        if (this.totalTime == null || this.totalTime == "0"){
            this.totalTime = minte+ "";
        }else{
            int temp = Integer.parseInt(this.totalTime);
            this.totalTime = (temp + minte) +"";
        }
    }

    public String getMissTime() {
        return missTime;
    }

    public void updateMissTime(String missTime) {
        this.missTime = missTime;
    }

    public void updateMissTime(long missTime) {
        int minte = (int) (missTime/60000);
        if (this.missTime == null || this.missTime == "0"){
            this.missTime = minte+ "";
        }else{
            int temp = Integer.parseInt(this.missTime);
            this.missTime = (temp + minte) +"";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeTypedList(breath_log);
        dest.writeTypedList(step_log);
        dest.writeString(totalTime);
        dest.writeString(missTime);
        dest.writeInt(temp_steam);
    }
}
