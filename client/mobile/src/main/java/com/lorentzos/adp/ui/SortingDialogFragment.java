package com.lorentzos.adp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.lorentzos.adp.R;

/**
 * Created by dionysis_lorentzos on 5/3/15
 * for package com.lorentzos.adp.ui
 * Use with caution dinosaurs might appear!
 */
public abstract class SortingDialogFragment extends DialogFragment {

    public static final String TAG = SortingDialogFragment.class.getSimpleName();
    private final int selected;

    public SortingDialogFragment(int selected){
        this.selected = selected;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sort_by)
                .setSingleChoiceItems(R.array.sorting_by, selected, (dialog, which) -> {
                    onItemClick(dialog, which);
                    dismiss();
                });
        return builder.create();
    }

    protected abstract void onItemClick(DialogInterface dialog, int which);



}
