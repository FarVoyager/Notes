package com.example.notes;

import androidx.annotation.NonNull;

import java.util.List;

public interface DataSource {
    interface DataSourceListener {
        void onItemAdded(int idx);
        void onItemRemoved(int idx);
        void onItemUpdated(int idx);
        void onDataSetChanged();
    }

    void addDataSourceListener(DataSourceListener listener);
    void removeDataSourceListener(DataSourceListener listener);


    List<Note> getNoteData();
    Note getItemAt(int idx);
    int getItemsCount();

    void update(@NonNull Note data);
    void add(@NonNull Note data);
    void remove(int position);
}
