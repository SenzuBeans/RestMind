package com.alternative.cap.restmindv3.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class MediaItem implements Parcelable {

    public String target;
    public String name;
    public String artist;
    public String link;
    public String link_2;
    public String image_link;
    public String image_link_2;
    public float rating_score;
    public long rating_count;
    public long temp_steam;

    public MediaItem() {
    }

    public MediaItem(String target, String name, String artist, String link, String link_2, String image_link, String image_link_2, float rating_score, long rating_count) {
        this.target = target;
        this.name = name;
        this.artist = artist;
        this.link = link;
        this.link_2 = link_2;
        this.image_link = image_link;
        this.image_link_2 = image_link_2;
        this.rating_score = rating_score;
        this.rating_count = rating_count;
    }

    protected MediaItem(Parcel in) {
        target = in.readString();
        name = in.readString();
        artist = in.readString();
        link = in.readString();
        link_2 = in.readString();
        image_link = in.readString();
        image_link_2 = in.readString();
        rating_score = in.readFloat();
        rating_count = in.readLong();
        temp_steam = in.readLong();
    }

    public static final Creator<MediaItem> CREATOR = new Creator<MediaItem>() {
        @Override
        public MediaItem createFromParcel(Parcel in) {
            return new MediaItem(in);
        }

        @Override
        public MediaItem[] newArray(int size) {
            return new MediaItem[size];
        }
    };

    public void updateRating_score(float rating_score) {
        this.rating_score = this.rating_score + rating_score;
        updateRating_count();
    }

    public void updateRating_count() {
        this.rating_count = this.rating_count + 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(target);
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(link);
        dest.writeString(link_2);
        dest.writeString(image_link);
        dest.writeString(image_link_2);
        dest.writeFloat(rating_score);
        dest.writeLong(rating_count);
        dest.writeLong(temp_steam);
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink_2() {
        return link_2;
    }

    public void setLink_2(String link_2) {
        this.link_2 = link_2;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getImage_link_2() {
        return image_link_2;
    }

    public void setImage_link_2(String image_link_2) {
        this.image_link_2 = image_link_2;
    }

    public float getRating_score() {
        return rating_score;
    }

    public void setRating_score(float rating_score) {
        this.rating_score = rating_score;
    }

    public long getRating_count() {
        return rating_count;
    }

    public void setRating_count(long rating_count) {
        this.rating_count = rating_count;
    }
}
