package com.lorentzos.adp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lorentzos.adp.R;
import com.lorentzos.adp.model.File;
import com.lorentzos.adp.model.RestError;
import com.lorentzos.adp.network.API;
import com.lorentzos.adp.network.RetrofitAction1;
import com.lorentzos.adp.util.SharedPreferencesUtil;

import java.util.ArrayList;

import static com.lorentzos.adp.util.ObservableUtil.versionFragmentSubscription;

/**
 * Created by dionysis_lorentzos on 15/3/15
 * for package com.lorentzos.adp.ui
 * Use with caution dinosaurs might appear!
 */
public class VersionsLoadingFragment extends Fragment {

    public static final String TAG = VersionsLoadingFragment.class.getSimpleName();


    public static void replace(ActionBarActivity activity) {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new VersionsLoadingFragment(), VersionsLoadingFragment.TAG)
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferencesUtil preferences = new SharedPreferencesUtil(getActivity());
        API api = API.getInstance(preferences.getUrl(), preferences.getToken());

        versionFragmentSubscription(this, api)
            .subscribe(files ->
                            VersionsListFragment.replace((ActionBarActivity) getActivity(), (ArrayList<File>) files),
                    new RetrofitAction1() {
                        @Override
                        protected void onHttpStatusError(RestError body) {
                            VersionsListFragment.replace((ActionBarActivity) getActivity(), body);
                        }
                    });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.loading_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), EditDetailsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
