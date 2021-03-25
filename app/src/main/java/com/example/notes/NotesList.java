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

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
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
        //создаем RecyclerView и пихаем его в макет fragment_notes_list
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_notes_list, container, false);
        recyclerView.setHasFixedSize(true);

        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        DividerItemDecoration decorator = new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL);
        decorator.setDrawable(getResources().getDrawable(R.drawable.decoration));
        recyclerView.addItemDecoration(decorator);

        //создаем layout manager для RecyclerView и связываем их
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //создаем adapter для RecyclerView и связываем их
        ViewHolderAdapter viewHolderAdapter = new ViewHolderAdapter(inflater, new CardDataSourceImpl(getResources()));
        viewHolderAdapter.setOnClickListener((v, position) -> {
            final int index = position;

            currentNote = new Note(getResources().getStringArray(R.array.notes)[index], getResources().getStringArray(R.array.descriptions)[index], getResources().getStringArray(R.array.dates)[index]);
            showNote(currentNote);
        });
        recyclerView.setAdapter(viewHolderAdapter);

        return recyclerView;
    }


    //определяем класс ViewHolderAdapter ВНУТРИ класса списка (NotesList)
    //в этом классе также реализуем слушатели нажатия
    private class ViewHolderAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final LayoutInflater mInflater;
        private final CardDataSource mDataSource;
        private OnClickListener mOnClickListener;

        public ViewHolderAdapter(LayoutInflater mInflater, CardDataSource mDataSource) {
            this.mInflater = mInflater;
            this.mDataSource = mDataSource;
        }

        public void setOnClickListener(OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = mInflater.inflate(R.layout.list_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CardData cardData = mDataSource.getItemAt(position);
            holder.populate(cardData);

            //клик по вьюшке
            holder.itemView.setOnClickListener(v -> {
                if (mOnClickListener != null) {
                    mOnClickListener.onItemClick(v, position);
                }
            });

            // вызов popup menu долгим нажатием
            holder.itemView.setOnLongClickListener(v -> {
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
                        case R.id.rename_popup:
                            Toast.makeText(getContext(), "Переименовать", Toast.LENGTH_SHORT).show();
                            return true;
                    }
                    return true;
                });
                popupMenu.show();
                return true;
            });
        }

        @Override
        public int getItemCount() {
            return mDataSource.getItemsCount();
        }
    }

    //определяем класс ViewHolder ВНУТРИ класса списка (NotesList)
    //определяем в нем элементы UI (вьюшки), которые будут в нашем RecyclerView
    //ViewHolder хранит соответствия между элементом списка и элементами UI
    private static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView text;
        public final AppCompatImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.list_item_text);
            image = itemView.findViewById(R.id.list_item_img);
        }

        //класс populate связывает данные карточки (CardView) и вьюшки в элементе CardView макета
        public void populate(CardData data) {
            text.setText(data.text);
            image.setImageResource(data.imageResourceId);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //интерфейс ВНУТРИ класса для обработки нажатия
    private interface OnClickListener {
        void onItemClick(View v, int position);
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