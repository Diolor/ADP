package com.lorentzos.adp.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lorentzos.adp.R;
import com.lorentzos.adp.model.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.lorentzos.adp.util.DateTimeUtil.getDateForListView;

/**
 * Created by dionysis_lorentzos on 17/2/15
 * for package com.lorentzos.adp.ui
 * Use with caution dinosaurs might appear!
 */
public abstract class FilesAdapter extends RecyclerView.Adapter<FilesHolder> implements View.OnClickListener {


    private final Object mLock = new Object();
    private List<File> fileList = new ArrayList<>();


    @Override
    public FilesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycled_item_file, parent, false);

        itemView.setOnClickListener(this);

        return new FilesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FilesHolder holder, int position) {
        File file = fileList.get(position);

        holder.version.setText(file.getVersion());
        holder.buildVariant.setText(file.getBuildVariant());

        holder.commit.setText(file.getCommit());
        holder.notes.setText(file.getNotes());

        holder.date.setText(getDateForListView(file.getDate()));
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    @Override
    public void onClick(View v) {
        onItemClick(v);
    }

    public void addItems(List<File> files, Comparator<File> comparator) {
        synchronized (mLock) {
            Collections.sort(files, comparator);
        }
        addItems(files);
    }

    public void addItems(@NonNull List<File> files) {
        fileList.clear();
        fileList = files;
        notifyDataSetChanged();
    }

    public File getItem(int position) {
        return fileList.get(position);
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     *        in this adapter.
     */
    public void sort(Comparator<File> comparator) {
        synchronized (mLock) {
            Collections.sort(fileList, comparator);
        }
        notifyDataSetChanged();
    }

    public abstract void onItemClick(View v);


}
