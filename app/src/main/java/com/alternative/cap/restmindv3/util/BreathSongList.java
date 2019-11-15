package com.alternative.cap.restmindv3.util;

import java.util.ArrayList;

public class BreathSongList {

    public static ArrayList<MediaItem> dataList;
    public static int current = 0;

    public BreathSongList() {
    }

    public static ArrayList<MediaItem> getDataList() {
        return dataList;
    }

    public static void setDataList(ArrayList<MediaItem> dataList) {
        if (BreathSongList.dataList == null){
            BreathSongList.dataList = new ArrayList<>();
        }
        BreathSongList.dataList = dataList;
    }

    public static int getCurrent() {
        return current;
    }

    public static void setCurrent(int current) {
        BreathSongList.current = current;
    }
}
