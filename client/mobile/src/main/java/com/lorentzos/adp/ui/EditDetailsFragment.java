package com.lorentzos.adp.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lorentzos.adp.Config;
import com.lorentzos.adp.R;
import com.lorentzos.adp.util.SharedPreferencesUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by dionysis_lorentzos on 7/12/14
 * for package com.lorentzos.adp.ui
 * Use with caution dinosaurs might appear!
 */
public class EditDetailsFragment extends Fragment {

    public static final String TAG = "EditDetailsFragment";
    private SharedPreferences preferences;

    @InjectView(R.id.url) EditText mURL;
    @InjectView(R.id.token) EditText mToken;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_details, container, false);
        ButterKnife.inject(this, rootView);

        SharedPreferencesUtil sp = new SharedPreferencesUtil(getActivity());
        preferences = sp.getPreferences();
        mURL.setText(sp.getUrl());
        mToken.setText(sp.getToken());

        return rootView;
    }

    @SuppressLint("CommitPrefEdits")
    @OnClick(R.id.ok)
    public void onSubmitClicked(View v){
        preferences.edit()
                .putString(Config.PREF_URL, mURL.getText().toString())
                .putString(Config.PREF_TOKEN, mToken.getText().toString())
                .commit();

        if(getActivity() instanceof EditDetailsActivity)
            getActivity().finish();
        else
            getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new VersionsListFragment(), VersionsListFragment.TAG)
                .commit();
    }

}