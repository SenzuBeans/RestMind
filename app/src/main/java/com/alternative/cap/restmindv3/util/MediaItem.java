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

    protected MediaItem(Parcel in) {
        name = in.readString();
        artist = in.readString();
        link = in.readString();
        image_link = in.readString();
        temp_steam = in.readLong();
    }

    public static final Creator<MediaItem> CREATOR = new Creator<MediaItem>() {
        @Override
        public MediaItem createFromParcel(Parcel in) {
            return new MediaItem( in );
        }

        @Override
        public MediaItem[] newArray(int size) {
            return new MediaItem[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString( name );
        parcel.writeString( artist );
        parcel.writeString( link );
        parcel.writeString( image_link );
        parcel.writeLong( temp_steam );
    }
}
