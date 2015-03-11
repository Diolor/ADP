package com.lorentzos.adp.util;

import android.app.Activity;
import android.content.SharedPreferences;

import com.lorentzos.adp.Config;

/**
 * Created by dionysis_lorentzos on 7/12/14
 * for package com.lorentzos.adp.util
 * Use with caution dinosaurs might appear!
 */
public class SharedPreferencesUtil {


    private final SharedPreferences preferences;

    public SharedPreferencesUtil(Activity activity) {
        preferences = activity.getSharedPreferences(Config.PREF_SIMPLE, 0);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public String getUrl(){
        return preferences.getString(Config.PREF_URL,"");
    }

    public String getToken(){
        return preferences.getString(Config.PREF_TOKEN,"");
    }


}
