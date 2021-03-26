package com.example.notes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

//определяем класс ViewHolderAdapter ВНУТРИ класса списка (NotesList)
//в этом классе также реализуем слушатели нажатия
public class ViewHolderAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final NotesList mNotesList;
    private final NotesList mFragment;
    private final LayoutInflater mInflater;
    private final CardDataSource mDataSource;
    private NotesList.OnClickListener mOnClickListener;

    public ViewHolderAdapter(NotesList fragment, NotesList notesList, CardDataSource dataSource) {
        mFragment = fragment;
        mNotesList = notesList;
        mInflater = fragment.getLayoutInflater();
        mDataSource = dataSource;
    }

    public void setOnClickListener(NotesList.OnClickListener onClickListener) {
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
        holder.populate(mFragment, cardData);

        //клик по вьюшке
        holder.itemView.setOnClickListener(v -> {
            if (mOnClickListener != null) {
                mOnClickListener.onItemClick(v, position);
            }
        });

//        // вызов popup menu долгим нажатием
//        holder.itemView.setOnLongClickListener(v -> {
//            Activity activity = notesList.requireActivity();
//            PopupMenu popupMenu = new PopupMenu(activity, v);
//            Menu menu = popupMenu.getMenu();
//            activity.getMenuInflater().inflate(R.menu.popup, menu);
//            popupMenu.setOnMenuItemClickListener(item -> {
//                int id = item.getItemId();
//                switch (id) {
//                    case R.id.favorite_popup:
//                        Toast.makeText(notesList.getContext(), "В Избранное", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.delete_popup:
//                        Toast.makeText(notesList.getContext(), "Удалить", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.rename_popup:
//                        Toast.makeText(notesList.getContext(), "Переименовать", Toast.LENGTH_SHORT).show();
//                        return true;
//                }
//                return true;
//            });
//            popupMenu.show();
//            return true;
//        });
    }

    @Override
    public int getItemCount() {
        return mDataSource.getItemsCount();
    }
}
