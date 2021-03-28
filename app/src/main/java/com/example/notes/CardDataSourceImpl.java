package com.example.notes;

import android.content.res.Resources;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CardDataSourceImpl implements CardDataSource {
    private final LinkedList<CardData> mData = new LinkedList<>();

    public CardDataSourceImpl(Resources resources) {
        String[] notes = resources.getStringArray(R.array.notes);
        for (int i = 0; i < notes.length; i++) {
            mData.add(new CardData(notes[i], R.drawable.drawing));
        }
    }

    @Override
    public List<CardData> getCardData() {
        return Collections.unmodifiableList(mData);
    }

    @Override
    public CardData getItemAt(int idx) {
        return mData.get(idx);
    }

    @Override
    public int getItemsCount() {
        return mData.size();
    }
}
