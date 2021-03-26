package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;


public class NotesList extends Fragment {

    private static final String CURRENT_NOTE = "CurrentNote";
    private Note currentNote;
    private boolean isLandscape;

    protected int mLastSelectedPosition = -1;

    public CardDataSource mCardDataSource;
    public ViewHolderAdapter mViewHolderAdapter;
    public RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);


        //создаем RecyclerView и пихаем его в макет fragment_notes_list
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_notes_list, container, false);
        mRecyclerView.setHasFixedSize(true);

        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        DividerItemDecoration decorator = new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL);
        decorator.setDrawable(getResources().getDrawable(R.drawable.decoration));
        mRecyclerView.addItemDecoration(decorator);

        //ANIMATOR
        //ANIMATOR
        //ANIMATOR

        //создаем layout manager для RecyclerView и связываем их
        LinearLayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mCardDataSource = CardDataSourceImpl.getInstance(getResources());
        //создаем adapter для RecyclerView и связываем их
        mViewHolderAdapter = new ViewHolderAdapter(this, this, mCardDataSource);
        mViewHolderAdapter.setOnClickListener((v, position) -> {
            final int index = position;
            currentNote = new Note(getResources().getStringArray(R.array.notes)[index], getResources().getStringArray(R.array.descriptions)[index], getResources().getStringArray(R.array.dates)[index]);
            showNote(currentNote);
        });
        mRecyclerView.setAdapter(mViewHolderAdapter);

        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return mRecyclerView;
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
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.noteDetailed, notesDetailed);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

        Intent intent = new Intent();
        //заменил NoteDetailedActivity на NoteDetailedFragment для отвезки фрагмента NoteDetailed от 2-ой активити
        intent.setClass(getActivity(), NotesDetailedFragment.class);
        intent.putExtra(NotesDetailedFragment.ARG_NOTE, note);
    }

    private void showNoteLandscape(Note note) {
        // создаём новый фрагмент с текущей позицией
        NotesDetailedFragment notesDetailed = NotesDetailedFragment.newInstance(note);
        // выполняем транзакцию по замене фрагмента (написано что-то непонятное)
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.noteDetailed, notesDetailed);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

        Intent intent = new Intent();
        //заменил NoteDetailedActivity на NoteDetailedFragment для отвезки фрагмента NoteDetailed от 2-ой активити
        intent.setClass(getActivity(), NotesDetailedFragment.class);
        intent.putExtra(NotesDetailedFragment.ARG_NOTE, note);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.action_favorite:
            case R.id.action_settings:
                Toast.makeText(requireActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_add:
                mCardDataSource.add(new CardData("New Note", R.drawable.ic_launcher_background));
                int position = mCardDataSource.getItemsCount() - 1;
                mViewHolderAdapter.notifyItemInserted(position);
                mRecyclerView.scrollToPosition(position);
                return true;

            case R.id.action_about:
                About aboutPage = About.newInstance();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.noteDetailed, aboutPage);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                return true;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.context_menu_main, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.context_edit) {
            if (mLastSelectedPosition != -1) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.noteList, NoteEditFragment.newInstance(mLastSelectedPosition));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        } else if (item.getItemId() == R.id.context_delete) {
            if (mLastSelectedPosition != -1) {
                mCardDataSource.remove(mLastSelectedPosition);
                mViewHolderAdapter.notifyItemRemoved(mLastSelectedPosition);
            }
        } else {
            return super.onContextItemSelected(item);
        }
        return true;
    }

    public static NotesList newInstance() {
        NotesList fragment = new NotesList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    //интерфейс ВНУТРИ класса для обработки нажатия
    public interface OnClickListener {
        void onItemClick(View v, int position);
    }

    void setLastSelectedPosition(int lastSelectedPosition) {
        mLastSelectedPosition = lastSelectedPosition;
    }


}