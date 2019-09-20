package com.kety.smock210.ovnsicorrectqrfull;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by smock210 on 08.11.2016.
 */

public class Errors implements Parcelable {
    String text;
    String fuulText;
    boolean box;


    Errors(String _describe, String _fuulDescript, boolean _box) {
        text = _describe;
        fuulText = _fuulDescript;
        box = _box;
    }

    protected Errors(Parcel in) {
        text = in.readString();
        fuulText = in.readString();
        box = in.readByte() != 0;
    }

    public static final Creator<Errors> CREATOR = new Creator<Errors>() {
        @Override
        public Errors createFromParcel(Parcel in) {
            return new Errors(in);
        }

        @Override
        public Errors[] newArray(int size) {
            return new Errors[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(fuulText);
        dest.writeByte((byte) (box ? 1 : 0));
    }
}
