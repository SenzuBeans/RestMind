package com.alternative.cap.restmindv3.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class StepListItem implements Parcelable  {

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


    protected StepListItem(Parcel in) {
        rawId = in.readString();
        artist = in.readString();
        image_link = in.readString();
    }

    public static final Creator<StepListItem> CREATOR = new Creator<StepListItem>() {
        @Override
        public StepListItem createFromParcel(Parcel in) {
            return new StepListItem( in );
        }

        @Override
        public StepListItem[] newArray(int size) {
            return new StepListItem[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString( rawId );
        parcel.writeString( artist );
        parcel.writeString( image_link );
    }
}
