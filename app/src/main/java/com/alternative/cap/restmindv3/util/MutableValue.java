package com.alternative.cap.restmindv3.util;

import android.content.Context;
import android.os.Bundle;

public class MutableValue {

    private static int heightRecyclerViewValue;
    private static long[] breathData = {3000,1000,3000,0};

    public MutableValue() {
    }

    public int getHeightRecyclerViewValue() {
        return heightRecyclerViewValue;
    }

    public void setHeightRecyclerViewValue(int heightValue) {
        heightRecyclerViewValue = heightValue;
    }

    public long[] getBreathData() {
        return breathData;
    }

    public void setBreathData(long[] passingBreathData) {
        breathData = passingBreathData;
    }

    public Bundle onSave(){
        Bundle bundle = new Bundle();
        bundle.putInt("heightRecyclerViewValue", heightRecyclerViewValue);
        return bundle;
    }

    public void onRestore(Bundle savedInstance){
        heightRecyclerViewValue = savedInstance.getInt("heightRecyclerViewValue");
    }

    public float convertDpToPixel(float dp, Context context){
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
