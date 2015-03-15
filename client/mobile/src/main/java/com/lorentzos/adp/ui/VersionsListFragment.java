package com.lorentzos.adp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lorentzos.adp.R;
import com.lorentzos.adp.model.File;
import com.lorentzos.adp.model.RestError;
import com.lorentzos.adp.network.API;
import com.lorentzos.adp.network.RetrofitAction1;
import com.lorentzos.adp.util.DialogUtil;
import com.lorentzos.adp.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.lorentzos.adp.util.ComparatorFactory.getComparatorType;
import static com.lorentzos.adp.util.ObservableUtil.versionFragmentSubscription;

/**
 * Created by dionysis_lorentzos on 7/12/14
 * for package com.lorentzos.adp.ui
 * Use with caution dinosaurs might appear!
 */
public class VersionsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = VersionsListFragment.class.getSimpleName();
    private static final String FILES = "FILES";
    private static final String ERROR = "ERROR";
    private static final int EDIT_DETAILS = 4;

    private FilesAdapter filesAdapter;
    private API api;
    private int sortSelection = 0; // Default sorting is date

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @InjectView(R.id.refresh_wrapper)
    SwipeRefreshLayout mRefreshWrapper;


    public static void replace(ActionBarActivity activity, RestError error) {
        VersionsListFragment f = newInstance(error);
        replace(activity, f);
    }

    public static void replace(ActionBarActivity activity, ArrayList<File> files) {
        VersionsListFragment f = newInstance(files);
        replace(activity, f);
    }

    private static void replace(ActionBarActivity activity, VersionsListFragment f) {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, f, TAG)
                .commit();
    }

    public static VersionsListFragment newInstance(ArrayList<File> files) {
        VersionsListFragment fragment = new VersionsListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(FILES, files);
        fragment.setArguments(args);
        return fragment;
    }

    private static VersionsListFragment newInstance(RestError error) {
        VersionsListFragment fragment = new VersionsListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ERROR, error);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, rootView);
        setHasOptionsMenu(true);

        filesAdapter = new FilesAdapter() {
            @Override
            public void onItemClick(View v) {
                int position = mRecyclerView.getChildPosition(v);
                File f = filesAdapter.getItem(position);
                AlertDialog alertDialog = DialogUtil.spawnDownloadDialog(getActivity(), f,
                        (dialog, which) -> {
                            //On Positive Button clicked
                            DownloadingFragment.show((ActionBarActivity) getActivity(), f.getId());
                        });
                alertDialog.show();
            }
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(filesAdapter);


        mRefreshWrapper.setOnRefreshListener(this);

        if(getArguments()!=null){

            List<File> files = getArguments().getParcelableArrayList(FILES);
            if(files!=null)
                filesAdapter.addItems(files, getComparatorType(sortSelection));

            else if(getArguments().getParcelable(ERROR)!=null)
                makeToast(getActivity(), ((RestError) getArguments().getParcelable(ERROR)).error );

        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferencesUtil preferences = new SharedPreferencesUtil(getActivity());
        api = API.getInstance(preferences.getUrl(), preferences.getToken());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == EDIT_DETAILS && resultCode == Activity.RESULT_OK)
            VersionsLoadingFragment.replace((ActionBarActivity) getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), EditDetailsActivity.class);
                startActivityForResult(intent, EDIT_DETAILS);
                return true;
            case R.id.action_sort_by:
                SortingDialogFragment dialog = new SortingDialogFragment(sortSelection) {
                    @Override
                    protected void onItemClick(DialogInterface dialog, int which) {
                        filesAdapter.sort(getComparatorType(which));
                        sortSelection = which;
                    }
                };
                dialog.show(getActivity().getSupportFragmentManager(), SortingDialogFragment.TAG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        mRefreshWrapper.setRefreshing(true);

        versionFragmentSubscription(this, api)
            .subscribe(files -> {
                filesAdapter.addItems(files, getComparatorType(sortSelection));
                mRefreshWrapper.setRefreshing(false);
            }, new RetrofitAction1() {

                @Override
                protected void onHttpStatusError(RestError body) {
                    makeToast(getActivity(), body.error);
                }

                @Override
                protected void call() {
                    mRefreshWrapper.setRefreshing(false);
                }
            });
    }

    private static void makeToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}