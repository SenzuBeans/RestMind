package com.alternative.cap.restmindv3.util;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class MediaItem {

    public String name;
    public String artist;
    public String link;
    public String image_link;
    public long temp_steam;

    public MediaItem() {
    }

    public MediaItem(String name, String artist, String link, String image_link, long temp_steam) {
        this.name = name;
        this.artist = artist;
        this.link = link;
        this.image_link = image_link;
        this.temp_steam = temp_steam;
    }

    public void setter(String name, String artist, String link, String image_link, long temp_steam) {
        this.name = name;
        this.artist = artist;
        this.link = link;
        this.image_link = image_link;
        this.temp_steam = temp_steam;
    }



    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("artist", artist);
        result.put("link", link);
        result.put("use_count", image_link);
        result.put("temp_steam", temp_steam);
        return result;
    }
}
