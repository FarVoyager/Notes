package com.example.notes;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
//класс больше не нужен, но не уверен, стоит ли его сейчас удалять, на всякий случай оставлю
//всё теперь подтягивается из DataSourceFireBaseImpl
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

    }
}
