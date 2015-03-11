package com.lorentzos.adp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dionysis_lorentzos on 11/3/15
 * for package com.lorentzos.adp.model
 * Use with caution dinosaurs might appear!
 */
public class RestError {

    @SerializedName("code")
    public int code;
    @SerializedName("error")
    public String error;

}