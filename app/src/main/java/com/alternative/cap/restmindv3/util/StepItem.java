package com.alternative.cap.restmindv3.util;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class StepItem {

    public String stepId;
    public int currentState;

    public StepItem() {
    }

    public StepItem(String stepId, int currentState) {
        this.stepId = stepId;
        this.currentState = currentState;
    }
}
