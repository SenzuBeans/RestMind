package com.alternative.cap.restmindv3.util;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class MusicItem {

    public String name;
    public String artist;
    public String link;
    public long use_count;
    public long temp_steam;

    public MusicItem() {
    }

    public MusicItem(String name, String artist, String link, long use_count, long temp_steam) {
        this.name = name;
        this.artist = artist;
        this.link = link;
        this.use_count = use_count;
        this.temp_steam = temp_steam;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("artist", artist);
        result.put("link", link);
        result.put("use_count", use_count);
        result.put("temp_steam", temp_steam);
        return result;
    }
}
