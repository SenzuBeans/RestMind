package com.alternative.cap.restmindv3.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class VideoItem implements Parcelable {

    public String name;
    public String artist;
    public String link;
    public String link_2;
    public long use_count;
    public long temp_steam;

    public VideoItem() {
    }

    public VideoItem(String name, String artist, String link, String link_2, long use_count, long temp_steam) {
        this.name = name;
        this.artist = artist;
        this.link = link;
        this.link_2 = link_2;
        this.use_count = use_count;
        this.temp_steam = temp_steam;
    }

    protected VideoItem(Parcel in) {
        name = in.readString();
        artist = in.readString();
        link = in.readString();
        link_2 = in.readString();
        use_count = in.readLong();
        temp_steam = in.readLong();
    }

    public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel in) {
            return new VideoItem(in);
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
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
        dest.writeLong(use_count);
        dest.writeLong(temp_steam);
    }
}
