package com.lorentzos.adp.widget;

import android.os.Parcelable;
import android.view.View;

import auto.parcel.AutoParcel;


/**
 * Created by dionysis_lorentzos on 18/2/15
 * for package com.lorentzos.adp.widget
 * Use with caution dinosaurs might appear!
 */

@AutoParcel
public abstract class OnRefreshEvent implements Parcelable{

    public static OnRefreshEvent create(View v) {
        return new AutoParcel_OnRefreshEvent(v);
    }

    public abstract View view();

}
