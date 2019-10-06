package com.alternative.cap.restmindv3.util;

import java.util.ArrayList;
import java.util.List;

public class UserDetails {

    public String name;
    public String email;
    public ArrayList<BreathLogItem> breath_log;
//    public StepItem stepItem;
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

    public UserDetails(String name, String email,ArrayList<BreathLogItem> breath_log, int temp_steam) {
        this.name = name;
        this.email = email;
        this.breath_log = breath_log;

//        , StepItem stepItem
//        this.stepItem = stepItem;
        this.temp_steam = temp_steam;
    }
}
