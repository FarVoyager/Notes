package com.example.notes;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class NotesList extends Fragment {

    private static final String CURRENT_NOTE = "CurrentNote";
    private Note currentNote;
    private boolean isLandscape;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    private void initList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        String[] notes = getResources().getStringArray(R.array.notes);
        for (int i = 0; i <notes.length; i++) {
            String note = notes[i];
            TextView textview = new TextView(getContext());
            textview.setText(note);
            textview.setTextSize(25);
            layoutView.addView(textview);

            final int index = i;

            textview.setOnClickListener(v -> {
                currentNote = new Note(getResources().getStringArray(R.array.notes)[index], getResources().getStringArray(R.array.descriptions)[index]);
                showNote(currentNote);
            });
        }
    }

    private void showNote(Note note) {
        if (isLandscape) {
            showNoteLandscape(note);
        } else {
            showNotePortrait(note);
        }
    }

    private void showNotePortrait(Note note) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NotesDetailedActivity.class);
        intent.putExtra(NotesDetailedFragment.ARG_NOTE, note);
        startActivity(intent);
    }

    private void showNoteLandscape(Note note) {
        // Создаём новый фрагмент с текущей позицией
        NotesDetailedFragment notesDetailed = NotesDetailedFragment.newInstance(note);
        // Выполняем транзакцию по замене фрагмента
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.noteDetailed, notesDetailed);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            currentNote = new Note(getResources().getStringArray(R.array.notes)[0], getResources().getStringArray(R.array.descriptions)[0]);
        }
        if (isLandscape) {
            showNoteLandscape(currentNote);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }

}