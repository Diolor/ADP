package com.lorentzos.adp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dionysis_lorentzos on 7/12/14
 * for package com.lorentzos.adp.model
 * Use with caution dinosaurs might appear!
 */

@SuppressWarnings("UnusedDeclaration")
public class BuildsList implements Parcelable{

    private List<File> files = new ArrayList<>();

    /**
     *
     * @return
     * The files
     */
    public List<File> getFiles() {
        return files;
    }

    /**
     *
     * @param files
     * The files
     */
    public void setFiles(List<File> files) {
        this.files = files;
    }

    protected BuildsList(Parcel in) {
        if (in.readByte() == 0x01) {
            files = new ArrayList<File>();
            in.readList(files, File.class.getClassLoader());
        } else {
            files = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (files == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(files);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BuildsList> CREATOR = new Parcelable.Creator<BuildsList>() {
        @Override
        public BuildsList createFromParcel(Parcel in) {
            return new BuildsList(in);
        }

        @Override
        public BuildsList[] newArray(int size) {
            return new BuildsList[size];
        }
    };
}

