package com.example.notes;

import androidx.annotation.DrawableRes;

public class CardData {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CardData(String text, @DrawableRes int imageResourceId) {
        this.text = text;
    }
}
