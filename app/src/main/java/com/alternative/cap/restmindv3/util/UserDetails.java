package com.alternative.cap.restmindv3.util;

import android.util.ArrayMap;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserDetails {

    public String name;
    public String email;
    public ArrayList<BreathLogItem> breath_log;
    public int temp_steam;

    public UserDetails() {
    }

    public UserDetails(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserDetails(ArrayList<BreathLogItem> breath_log) {
        this.breath_log = breath_log;
    }

    public UserDetails(String name, String email,ArrayList<BreathLogItem> breath_log) {
        this.name = name;
        this.email = email;
        this.breath_log = breath_log;
    }

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
}
