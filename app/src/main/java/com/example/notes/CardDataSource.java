package com.example.notes;

import androidx.annotation.NonNull;

import java.util.List;

public interface CardDataSource {
    List<CardData> getCardData();
    CardData getItemAt(int idx);
    int getItemsCount();

    void add(@NonNull CardData data);
    void remove(int position);
}
