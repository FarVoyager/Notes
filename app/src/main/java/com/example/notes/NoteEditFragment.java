package com.example.notes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


public class NoteEditFragment extends Fragment {

    private static final String ARG_ITEM_INDEX = "NoteEditFragment.item_index";
    private int mCurrentItemIndex = - 1;

    public static NoteEditFragment newInstance(int index) {
        NoteEditFragment fragment = new NoteEditFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_INDEX, index);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentItemIndex = getArguments().getInt(ARG_ITEM_INDEX, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_edit, container, false);

        final CardDataSource dataSource = CardDataSourceImpl.getInstance(getResources());
        CardData cardData = dataSource.getItemAt(mCurrentItemIndex);

        final TextInputEditText editText = view.findViewById(R.id.list_item_text);

        final MaterialButton btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> {
            cardData.setText(editText.getText().toString());
            System.out.println(cardData.getText() + " DATUM");
            getFragmentManager().popBackStack();
        });

        return  view;
    }
}