package com.example.notes.data;

import com.example.notes.Note;

public interface Observer {
    void updateNote(Note note);
}
