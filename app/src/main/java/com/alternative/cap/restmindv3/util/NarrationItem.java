package com.alternative.cap.restmindv3.util;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class NarrationItem {

    public String rawId;

    public NarrationItem() {
    }

    public NarrationItem(String rawData) {
        this.rawId = rawData;
    }
}
