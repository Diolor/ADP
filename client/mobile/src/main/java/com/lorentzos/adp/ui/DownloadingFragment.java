package com.lorentzos.adp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lorentzos.adp.R;
import com.lorentzos.adp.network.API;
import com.lorentzos.adp.util.APKUtil;
import com.lorentzos.adp.util.SharedPreferencesUtil;

import java.io.IOException;

import butterknife.ButterKnife;
import retrofit.mime.TypedByteArray;

import static rx.android.app.AppObservable.bindFragment;

/**
 * Created by dionysis_lorentzos on 5/3/15
 * for package com.lorentzos.adp.ui
 * Use with caution dinosaurs might appear!
 */
public class DownloadingFragment extends Fragment {


    public static final String TAG = DownloadingFragment.class.getSimpleName();
    private static final String ARG_FILE_ID = "fileID";
    private static final int CODE_INSTALLER = 100;


    public static DownloadingFragment newInstance(String fileID) {
        DownloadingFragment fragment = new DownloadingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILE_ID, fileID);
        fragment.setArguments(args);
        return fragment;
    }


    public static void show(ActionBarActivity activity, String fileID) {
        DownloadingFragment f = DownloadingFragment.newInstance(fileID);

        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, f, TAG)
                .addToBackStack(TAG)
                .commit();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_loading, container, false);
        ButterKnife.inject(this, rootView);

        String id = getArguments().getString(ARG_FILE_ID);

        SharedPreferencesUtil preferences = new SharedPreferencesUtil(getActivity());
        API api = API.getInstance(preferences.getUrl(), preferences.getToken());

        bindFragment(DownloadingFragment.this, api.getAPK(id))
                .map(response -> ((TypedByteArray) response.getBody()).getBytes())
                .subscribe(bytes -> {
                    try {
                        APKUtil.writeFile(bytes);

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new java.io.File(Environment.getExternalStorageDirectory() + "/download/" + "app.apk")), "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivityForResult(intent, CODE_INSTALLER);
                    } catch (IOException e) {
                        finish(e);
                    }
                }, this::finish);
        return rootView;
    }

    private void finish(Throwable e) {
        e.printStackTrace();
        Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_INSTALLER)
            finish();
    }

    private void finish(){
        getActivity().getSupportFragmentManager().popBackStack();
    }

}
