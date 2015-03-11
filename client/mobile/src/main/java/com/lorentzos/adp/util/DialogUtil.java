package com.lorentzos.adp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.lorentzos.adp.R;
import com.lorentzos.adp.model.File;

/**
 * Created by dionysis_lorentzos on 19/2/15
 * for package com.lorentzos.adp.util
 * Use with caution dinosaurs might appear!
 */
public abstract class DialogUtil {

    public static AlertDialog spawnDownloadDialog(Context context, File f,
                                                  DialogInterface.OnClickListener onClickListener) {
        return new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_download_title)
                .setMessage(R.string.dialog_download_message)
                .setPositiveButton(R.string.confirm, onClickListener)
                .setNegativeButton(android.R.string.cancel, null).create();
    }


}
