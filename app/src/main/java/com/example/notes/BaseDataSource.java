package com.example.notes;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseDataSource implements DataSource {
    private HashSet<DataSourceListener> mListeners = new HashSet<>();
    protected final LinkedList<Note> mData = new LinkedList<>();

    public void addDataSourceListener(DataSourceListener listener) {
        mListeners.add(listener);
    }

    public void removeDataSourceListener(DataSourceListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void update(@NonNull Note data) {
        String id = data.getId();
        if (id != null) {
            int idx = 0;
            for (Note note : mData) {
                if (id.equals(note.getId())) {
                    note.setName(data.getName());
                    note.setDescription(data.getDescription());
                    note.setDate(data.getDate());

                    notifyUpdated(idx);
                    return;
                }
                idx++;
            }
        }
        add(data);
    }
    protected final void notifyUpdated(int idx) {
        for (DataSourceListener listener : mListeners) {
            listener.onItemUpdated(idx);
        }
    }

    protected final void notifyDataSetChanged() {
        for (DataSourceListener listener : mListeners) {
            listener.onDataSetChanged();
        }
    }

    @Override
    public List<Note> getNoteData() {
        return Collections.unmodifiableList(mData);
    }

    @Override
    public Note getItemAt(int idx) {
        return mData.get(idx);
    }

    @Override
    public int getItemsCount() {
        return mData.size();
    }

    @Override
    public void add(@NonNull Note data) {
        mData.add(data);
        int idx = mData.size() - 1;
        for (DataSourceListener listener : mListeners) {
            listener.onItemAdded(idx);
        }
    }

    @Override
    public void remove(int position) {
        mData.remove(position);
        for (DataSourceListener listener : mListeners) {
            listener.onItemRemoved(position);
        }
    }

}
