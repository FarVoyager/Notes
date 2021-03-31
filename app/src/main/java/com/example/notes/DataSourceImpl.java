package com.example.notes;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DataSourceImpl extends BaseDataSource {


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

        LinkedList<Note> fireData = mData;


//        String[] notesNames = resources.getStringArray(R.array.notes);
//        String[] notesDescriptions = resources.getStringArray(R.array.descriptions);
//        String[] notesDates = resources.getStringArray(R.array.dates);
//        for (int i = 0; i < notesNames.length; i++) {
//            mData.add(new Note(notesNames[i], notesDescriptions[i], notesDates[i]));
//        }
//        notifyDataSetChanged();
    }
}
