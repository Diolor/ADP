package com.lorentzos.adp.util;


import android.support.v4.app.Fragment;

import com.lorentzos.adp.model.BuildsList;
import com.lorentzos.adp.model.File;
import com.lorentzos.adp.network.API;

import java.util.List;

import rx.Observable;

import static rx.android.app.AppObservable.bindFragment;

/**
 * Created by dionysis_lorentzos on 15/3/15
 * for package com.lorentzos.adp.util
 * Use with caution dinosaurs might appear!
 */
public class ObservableUtil {

    public static Observable<List<File>> versionFragmentSubscription(Fragment fragment, API api){
        return bindFragment(fragment, api.getVersions())
                .map(BuildsList::getFiles)
                .filter(files -> files!=null);
    }
}
