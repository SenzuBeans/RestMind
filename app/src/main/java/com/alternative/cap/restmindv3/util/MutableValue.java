package com.alternative.cap.restmindv3.util;

import android.content.Context;
import android.os.Bundle;

public class MutableValue {

    private static long[] breathData = {3000,1000,3000,0};

    public MutableValue() {
    }

    public long[] getBreathData() {
        return breathData;
    }

    public void setBreathData(long[] passingBreathData) {
        breathData = passingBreathData;
    }


}
