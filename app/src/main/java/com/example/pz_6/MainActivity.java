package com.example.pz;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String[] titles = {
            "Сделать домашнее задание", "Сдать на права", "Посадить дерево",
            "Отжаться", "Пробежать 100м", "Выучить стихотворение"
    };

    private final int[] priorities = {0, 0, 1, 0, 2, 2};
    private final String[] labels = {"Высокий", "Средний", "Низкий"};

    private final int[] icons = {
            android.R.drawable.presence_busy,
            android.R.drawable.presence_away,
            android.R.drawable.presence_online
    };

    static class Row {
        String text;
        int priority;

        Row(String text, int priority) {
            this.text = text;
            this.priority = priority;
        }
    }

    private final List<Row> shown = new ArrayList<>();
    private BaseAdapter adapter;
    private TextView tvCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.spinner);
        ListView lv = findViewById(R.id.lv);
        tvCounter = findViewById(R.id.tvCounter);

        for (int i = 0; i < titles.length; i++) {
            shown.add(new Row(titles[i], priorities[i]));
        }

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return shown.size();
            }

            @Override
            public Object getItem(int position) {
                return shown.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(MainActivity.this, R.layout.list_item, null);
                }

                ImageView icon = convertView.findViewById(R.id.icon);
                TextView tv = convertView.findViewById(R.id.title);

                Row row = shown.get(position);
                icon.setImageResource(icons[row.priority]);
                tv.setText("[" + labels[row.priority] + "] " + row.text);

                return convertView;
            }
        };

        lv.setAdapter(adapter);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Все", "Высокий", "Средний", "Низкий"}
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                shown.clear();
                for (int i = 0; i < titles.length; i++) {
                    if (pos == 0 || priorities[i] == pos - 1) {
                        shown.add(new Row(titles[i], priorities[i]));
                    }
                }
                adapter.notifyDataSetChanged();
                updateCounter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        updateCounter();
    }

    private void updateCounter() {
        tvCounter.setText("Показано задач: " + shown.size());
    }
}