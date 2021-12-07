package com.test.currentTest.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "restaurant_table", indices = {@Index(value = {"name"}, unique = true)})
public class Restaurant implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("offer")
    public String offer;
    @SerializedName("description")
    public String description;
    @SerializedName("image_url")
    public String imageUrl;

    public Restaurant() {
    }

    protected Restaurant(Parcel in) {
        this.name = in.readString();
        this.offer = in.readString();
        this.description = in.readString();
        this.imageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.offer);
        dest.writeString(this.description);
        dest.writeString(this.imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
}
