package com.lorentzos.adp.ui;

import android.app.AlertDialog;
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
import com.lorentzos.adp.model.BuildsList;
import com.lorentzos.adp.model.File;
import com.lorentzos.adp.model.RestError;
import com.lorentzos.adp.network.API;
import com.lorentzos.adp.util.DialogUtil;
import com.lorentzos.adp.util.SharedPreferencesUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;
import rx.Observable;
import rx.functions.Action1;

import static com.lorentzos.adp.util.ComparatorFactory.getComparatorType;
import static rx.android.app.AppObservable.bindFragment;

/**
 * Created by dionysis_lorentzos on 7/12/14
 * for package com.lorentzos.adp.ui
 * Use with caution dinosaurs might appear!
 */
public class VersionsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = VersionsListFragment.class.getSimpleName();

    private FilesAdapter filesAdapter;
    private API api;
    private int sortSelection = 1; // Default sorting is date

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @InjectView(R.id.refresh_wrapper)
    SwipeRefreshLayout mRefreshWrapper;



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
                            LoadingFragment.show((ActionBarActivity) getActivity(), f.getId());


                        });
                alertDialog.show();
            }
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(filesAdapter);


        mRefreshWrapper.setOnRefreshListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferencesUtil preferences = new SharedPreferencesUtil(getActivity());
        api = API.getInstance(preferences.getUrl(), preferences.getToken());

        versionFragmentSubscription()
                .subscribe(files -> {
                    //TODO
                    filesAdapter.addItems(files, getComparatorType(sortSelection));
                }, new RetrofitAction1());
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
                startActivity(intent);
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

        versionFragmentSubscription()
            .subscribe(files -> {
                //TODO
                filesAdapter.addItems(files, getComparatorType(sortSelection));
                mRefreshWrapper.setRefreshing(false);
            }, new RetrofitAction1() {
                @Override
                protected void call() {
                    mRefreshWrapper.setRefreshing(false);
                }
            });
    }

    private Observable<List<File>> versionFragmentSubscription(){
        return bindFragment(this, api.getVersions())
                        .map(BuildsList::getFiles)
                        .filter(files -> files!=null);
    }


    private class RetrofitAction1 implements Action1<Throwable> {
        @Override
        public void call(Throwable e) {
            e.printStackTrace();
            if(e instanceof RetrofitError){
                RestError body = (RestError) ((RetrofitError) e).getBodyAs(RestError.class);
                switch (((RetrofitError) e).getResponse().getStatus()){
                    case 400:
                    case 403:
                        Toast.makeText(getActivity(), body.error, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            call();
        }

        protected void call(){}
    }

}