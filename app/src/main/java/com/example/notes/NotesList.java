package com.example.notes;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;


public class NotesList extends Fragment {

    private static final String CURRENT_NOTE = "CurrentNote";
    private Note currentNote;
    private boolean isLandscape;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // непонятно
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }


    private void initList(View view) {

        LinearLayout layoutView = (LinearLayout) view;
        String[] dates = getResources().getStringArray(R.array.dates);
        String[] notes = getResources().getStringArray(R.array.notes);
        for (int i = 0; i < notes.length; i++) {
            String note = notes[i];
            String date = dates[i];
            LinearLayoutCompat subLayoutView = new LinearLayoutCompat(getContext());
            int subLayoutViewId = subLayoutView.getId();
            subLayoutView.setOrientation(LinearLayoutCompat.VERTICAL);
            TextView textviewName = new TextView(getContext());
            TextView textviewDate = new TextView(getContext());

            // оформление вьюшек
            int noteColor = Color.parseColor("#FFFAF096");

            textviewDate.setText(date);
            textviewDate.setTextColor(Color.GRAY);
            textviewDate.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textviewDate.setBackgroundColor(noteColor);

            textviewName.setText(note);
            textviewName.setTextSize(25);
            textviewName.setTextColor(Color.BLACK);
            textviewName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textviewName.setBackgroundColor(noteColor);

            // добавление вьюшек в layouts
            subLayoutView.addView(textviewName);
            subLayoutView.addView(textviewDate);
            layoutView.addView(subLayoutView);

            // вызов popup menu долгим нажатием
            subLayoutView.setOnLongClickListener(v -> {
                Activity activity = requireActivity();
                PopupMenu popupMenu = new PopupMenu(activity, v);
                Menu menu = popupMenu.getMenu();
                activity.getMenuInflater().inflate(R.menu.popup, menu);
                popupMenu.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.favorite_popup:
                            Toast.makeText(getContext(), "В Избранное", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.delete_popup:
                            Toast.makeText(getContext(), "Удалить", Toast.LENGTH_SHORT).show();
                            return true;
                    }
                    return true;
                });
                popupMenu.show();
                return true;
            });

            // обработка нажатия на заметку
            final int index = i;

            subLayoutView.setOnClickListener(v -> {
                currentNote = new Note(getResources().getStringArray(R.array.notes)[index], getResources().getStringArray(R.array.descriptions)[index], getResources().getStringArray(R.array.dates)[index]);
                showNote(currentNote);
            });
        }
    }

    // метод вызывает один из двух методов в зависимости от ориентации экрана
    private void showNote(Note note) {
        if (isLandscape) {
            showNoteLandscape(note);
        } else {
            showNotePortrait(note);
        }
    }

    private void showNotePortrait(Note note) {

        // создаём новый фрагмент с текущей позицией
        NotesDetailedFragment notesDetailed = NotesDetailedFragment.newInstance(note);
        // выполняем транзакцию по замене фрагмента (написано что-то непонятное)
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.noteDetailed, notesDetailed);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

        Intent intent = new Intent();
        intent.setClass(getActivity(), NotesDetailedActivity.class);
        intent.putExtra(NotesDetailedFragment.ARG_NOTE, note);
    }

    private void showNoteLandscape(Note note) {
        // создаём новый фрагмент с текущей позицией
        NotesDetailedFragment notesDetailed = NotesDetailedFragment.newInstance(note);
        // выполняем транзакцию по замене фрагмента (написано что-то непонятное)
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

        // если фрагмент уже появлялся - показываем сохраненную заметку
        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            // если не появлялся - показываем самую первую
            currentNote = new Note(getResources().getStringArray(R.array.notes)[0], getResources().getStringArray(R.array.descriptions)[0], getResources().getStringArray(R.array.dates)[0]);
        }
        if (isLandscape) {
            showNoteLandscape(currentNote);
        }
    }

    // сохраняем текущую отображаемую заметку
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }

}