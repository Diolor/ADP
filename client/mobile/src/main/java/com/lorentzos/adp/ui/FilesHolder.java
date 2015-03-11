package com.lorentzos.adp.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lorentzos.adp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dionysis_lorentzos on 17/2/15
 * for package com.lorentzos.adp.ui
 * Use with caution dinosaurs might appear!
 */
public class FilesHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.version)
    TextView version;
    @InjectView(R.id.build_variant)
    TextView buildVariant;
    @InjectView(R.id.commit)
    TextView commit;
    @InjectView(R.id.date)
    TextView date;
    @InjectView(R.id.notes)
    TextView notes;


    public FilesHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

}
