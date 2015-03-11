package com.lorentzos.adp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dionysis_lorentzos on 17/2/15
 * for package com.lorentzos.adp.model
 * Use with caution dinosaurs might appear!
 */

@SuppressWarnings("UnusedDeclaration")
public class File implements Parcelable {

    @SerializedName("build_variant")
    @Expose
    private String buildVariant;
    @Expose
    private String notes;
    @Expose
    private String version;
    @Expose
    private String commit;
    @Expose
    private String date;
    @Expose
    private String path;
    @Expose
    private String id;


    protected File(Parcel in) {
        buildVariant = in.readString();
        notes = in.readString();
        version = in.readString();
        commit = in.readString();
        date = in.readString();
        path = in.readString();
        id = in.readString();
    }

    /**
     *
     * @return
     * The buildVariant
     */
    public String getBuildVariant() {
        return buildVariant;
    }

    /**
     *
     * @param buildVariant
     * The build_variant
     */
    public void setBuildVariant(String buildVariant) {
        this.buildVariant = buildVariant;
    }

    /**
     *
     * @return
     * The notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     *
     * @param notes
     * The notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     *
     * @return
     * The version
     */
    public String getVersion() {
        return version;
    }

    /**
     *
     * @param version
     * The version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     *
     * @return
     * The commit
     */
    public String getCommit() {
        return commit;
    }

    /**
     *
     * @param commit
     * The commit
     */
    public void setCommit(String commit) {
        this.commit = commit;
    }

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The path
     */
    public String getPath() {
        return path;
    }

    /**
     *
     * @param path
     * The path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(buildVariant);
        dest.writeString(notes);
        dest.writeString(version);
        dest.writeString(commit);
        dest.writeString(date);
        dest.writeString(path);
        dest.writeString(id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<File> CREATOR = new Parcelable.Creator<File>() {
        @Override
        public File createFromParcel(Parcel in) {
            return new File(in);
        }

        @Override
        public File[] newArray(int size) {
            return new File[size];
        }
    };
}

