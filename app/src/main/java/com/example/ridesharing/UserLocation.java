package com.example.ridesharing;

import android.os.Parcel;
import android.os.Parcelable;

public class UserLocation implements Parcelable {
    public double latitude;
    public double longitude;
    public boolean isActive;

    public UserLocation() {
    }

    protected UserLocation(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        isActive = in.readByte() != 0;
    }

    public static final Creator<UserLocation> CREATOR = new Creator<UserLocation>() {
        @Override
        public UserLocation createFromParcel(Parcel in) {
            return new UserLocation(in);
        }

        @Override
        public UserLocation[] newArray(int size) {
            return new UserLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeByte((byte) (isActive ? 1 : 0));
    }
}


