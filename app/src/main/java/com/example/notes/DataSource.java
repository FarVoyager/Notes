package com.example.notes;

import androidx.annotation.NonNull;

import java.util.List;

public interface DataSource {
    List<Note> getNoteData();
    Note getItemAt(int idx);
    int getItemsCount();

    void add(@NonNull Note data);
    void remove(int position);
}
