package com.example.notes;

import androidx.annotation.DrawableRes;

public class CardData {
    public final String text;
    public final @DrawableRes int imageResourceId;

    public CardData(String text, @DrawableRes int imageResourceId) {
        this.text = text;
        this.imageResourceId = imageResourceId;
    }
}
