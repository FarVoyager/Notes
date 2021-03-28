package com.example.notes;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DataSourceImpl implements DataSource {
    public LinkedList<Note> mData = new LinkedList<>();

    //singleTone
    private volatile static DataSourceImpl sInstance;

    public static DataSourceImpl getInstance(Resources resources) {
        DataSourceImpl instance = sInstance;
        if (instance == null) {
            synchronized (DataSourceImpl.class) {
                if (sInstance == null) {

                    instance = new DataSourceImpl(resources);
                    sInstance = instance;
                }
            }
        }
        return instance;
    }

    private DataSourceImpl(Resources resources) {
        String[] notesNames = resources.getStringArray(R.array.notes);
        String[] notesDescriptions = resources.getStringArray(R.array.descriptions);
        String[] notesDates = resources.getStringArray(R.array.dates);
        for (int i = 0; i < notesNames.length; i++) {
            mData.add(new Note(notesNames[i], notesDescriptions[i], notesDates[i]));
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
    }

    @Override
    public void remove(int position) {
        mData.remove(position);
    }
}
