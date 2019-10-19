package com.alternative.cap.restmindv3.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class MediaItem implements Parcelable {

    public String name;
    public String artist;
    public String link;
    public String link_2;
    public String image_link;
    public String image_link_2;
    public long temp_steam;

    public MediaItem() {
    }

    public MediaItem(String name, String artist, String link, String link_2, String image_link, String image_link_2, long temp_steam) {
        this.name = name;
        this.artist = artist;
        this.link = link;
        this.link_2 = link_2;
        this.image_link = image_link;
        this.image_link_2 = image_link_2;
        this.temp_steam = temp_steam;
    }

    protected MediaItem(Parcel in) {
        name = in.readString();
        artist = in.readString();
        link = in.readString();
        link_2 = in.readString();
        image_link = in.readString();
        image_link_2 = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(link);
        dest.writeString(link_2);
        dest.writeString(image_link);
        dest.writeString(image_link_2);
        dest.writeLong(temp_steam);
    }
}
