package com.alternative.cap.restmindv3.util;

public class UserDetails {

    public String name;
    public String email;
    public StepItem stepItem;
    public int temp_steam;

    public UserDetails() {
    }

    public UserDetails(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserDetails(String name, String email, StepItem stepItem, int temp_steam) {
        this.name = name;
        this.email = email;
        this.stepItem = stepItem;
        this.temp_steam = temp_steam;
    }
}
