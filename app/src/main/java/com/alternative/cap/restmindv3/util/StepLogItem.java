package com.alternative.cap.restmindv3.util;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class StepLogItem {

    public String stepId;
    public String stepCount;

    public StepLogItem() {
    }

    public StepLogItem(String stepId, String stepCount) {
        this.stepId = stepId;
        this.stepCount = stepCount;
    }

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
}
