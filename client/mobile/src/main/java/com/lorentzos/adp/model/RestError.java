package com.lorentzos.adp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dionysis_lorentzos on 11/3/15
 * for package com.lorentzos.adp.model
 * Use with caution dinosaurs might appear!
 */
public class RestError implements Parcelable {

    @SerializedName("code")
    public int code;
    @SerializedName("error")
    public String error;

    public RestError(String error){
        this.error = error;
    }

    protected RestError(Parcel in) {
        code = in.readInt();
        error = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(error);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RestError> CREATOR = new Parcelable.Creator<RestError>() {
        @Override
        public RestError createFromParcel(Parcel in) {
            return new RestError(in);
        }

        @Override
        public RestError[] newArray(int size) {
            return new RestError[size];
        }
    };
}