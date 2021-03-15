package com.example.notes;
//  Создайте класс данных со структурой заметок: название заметки, описание заметки, дата создания и т. п.
//  Создайте фрагмент для вывода этих данных.
//  Встройте этот фрагмент в активити. У вас должен получиться экран с заметками, который мы будем улучшать с каждым новым уроком.
//  Добавьте фрагмент, в котором открывается заметка. По аналогии с примером из урока: если нажать на элемент списка в портретной ориентации — открывается новое окно, если нажать в ландшафтной — окно открывается рядом.
//* Разберитесь, как можно сделать, и сделайте корректировку даты создания при помощи DatePicker.


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}