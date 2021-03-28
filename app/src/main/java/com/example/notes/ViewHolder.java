package com.example.notes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

//определяем класс ViewHolder ВНУТРИ класса списка (NotesList)
//определяем в нем элементы UI (вьюшки), которые будут в нашем RecyclerView
//ViewHolder хранит соответствия между элементом списка и элементами UI
class ViewHolder extends RecyclerView.ViewHolder {
    public TextView text;
    public AppCompatImageView image;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        text = itemView.findViewById(R.id.list_item_text);
        image = itemView.findViewById(R.id.list_item_img);
    }

    //класс populate связывает данные карточки (CardView) и вьюшки в элементе CardView макета
    public void populate(NotesList fragment, Note data) {
        text.setText(data.getName());

        itemView.setOnLongClickListener((v) -> {
            fragment.setLastSelectedPosition(getLayoutPosition());
            return false;
        });
        fragment.registerForContextMenu(itemView);
    }
}
