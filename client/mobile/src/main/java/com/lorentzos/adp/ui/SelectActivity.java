package com.lorentzos.adp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.lorentzos.adp.R;
import com.lorentzos.adp.util.SharedPreferencesUtil;

/**
 * Created by dionysis_lorentzos on 7/12/14
 * for package com.lorentzos.adp.ui
 * Use with caution dinosaurs might appear!
 */
public class SelectActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            SharedPreferencesUtil sp = new SharedPreferencesUtil(this);


            Fragment fragment;
            if(sp.getUrl().equals("") && sp.getToken().equals("")) {
                // TODO replace EditDetailsFragment with a custom SettingsActivity
                fragment = new EditDetailsFragment();
            }else {
                fragment = new VersionsListFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

    }






}
