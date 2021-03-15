package com.example.notes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class NotesDetailedFragment extends Fragment {

    protected static final String ARG_NOTE = "note";

    private Note note;

    public NotesDetailedFragment() {
        // Required empty public constructor
    }


    public static NotesDetailedFragment newInstance(Note note) {
        NotesDetailedFragment fragment = new NotesDetailedFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Таким способом можно получить головной элемент из макета
        View view = inflater.inflate(R.layout.fragment_notes_detailed, container, false);
        // найти в контейнере элемент
        TextView noteDescription = view.findViewById(R.id.noteDescription);
        // Выбрать по индексу подходящий
        noteDescription.setText(note.getDescription());
        // Установить название города
        TextView noteName = view.findViewById(R.id.noteName);
        noteName.setText(note.getName());
        return view;

    }
}