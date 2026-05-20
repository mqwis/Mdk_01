package com.example.pz_1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class DialogActivity extends Activity {

    private static final String TAG = "DialogActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog); // если файл называется dialog_layout.xml
        Log.d(TAG, "onCreate: вызван");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: вызван");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: вызван");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: вызван");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: вызван");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: вызван");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: вызван");
    }
}