package com.alternative.cap.restmindv3.util;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class StepListItem {

    public String rawId;
    public String artist;
    public String image_link;

    public StepListItem() {
    }

    public StepListItem(String rawId, String artist, String image_link) {
        this.rawId = rawId;
        this.artist = artist;
        this.image_link = image_link;
    }


    public String getRawId() {
        return rawId;
    }

    public void setRawId(String rawId) {
        this.rawId = rawId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }
}
