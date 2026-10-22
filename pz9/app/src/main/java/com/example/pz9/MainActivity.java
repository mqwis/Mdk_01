package com.example.pz9;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.IOException;

public class MainActivity extends Activity
        implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    private static final String LOG_TAG = "myLogs";

    private static final String DATA_HTTP = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
    private static final String DATA_STREAM = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
    private static final String DATA_SD =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/music.mp3";
    private static final Uri DATA_URI = android.provider.Settings.System.DEFAULT_RINGTONE_URI;

    private MediaPlayer mediaPlayer;
    private AudioManager am;
    private CheckBox chbLoop;
    private TextView tvState;
    private TextView tvTrack;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        chbLoop = findViewById(R.id.chbLoop);
        tvState = findViewById(R.id.tvState);
        tvTrack = findViewById(R.id.tvTrack);
        progressBar = findViewById(R.id.progressBar);

        chbLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                if (mediaPlayer != null) mediaPlayer.setLooping(isChecked);
            }
        });
    }

    public void onClickStart(View v) {
        releaseMP();
        int id = v.getId();

        try {
            if (id == R.id.btnHttp) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(DATA_HTTP);
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.prepareAsync();
                tvTrack.setText("SoundHelix Song 1");
                tvState.setText("HTTP: loading...");

            } else if (id == R.id.btnStream) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(DATA_STREAM);
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.prepareAsync();
                tvTrack.setText("SoundHelix Stream");
                tvState.setText("STREAM: loading...");

            } else if (id == R.id.btnSd) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(DATA_SD);
                mediaPlayer.prepare();
                mediaPlayer.start();
                tvTrack.setText("SD Music");
                tvState.setText("SD: playing");

            } else if (id == R.id.btnUri) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(this, DATA_URI);
                mediaPlayer.prepare();
                mediaPlayer.start();
                tvTrack.setText("System Ringtone");
                tvState.setText("URI: playing");

            } else if (id == R.id.btnRaw) {
                mediaPlayer = MediaPlayer.create(this, R.raw.music);
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    tvTrack.setText("Local Track");
                    tvState.setText("RAW: playing");
                }
            }

            if (mediaPlayer != null) {
                mediaPlayer.setLooping(chbLoop.isChecked());
                mediaPlayer.setOnCompletionListener(this);
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare error", e);
            tvState.setText("Ошибка: файл не найден");
        }
    }

    public void onClick(View v) {
        if (mediaPlayer == null) return;
        int id = v.getId();

        if (id == R.id.btnPause) {
            if (mediaPlayer.isPlaying()) mediaPlayer.pause();
            tvState.setText("paused");

        } else if (id == R.id.btnResume) {
            mediaPlayer.start();
            tvState.setText("playing");

        } else if (id == R.id.btnStop) {
            mediaPlayer.stop();
            tvState.setText("stopped");

        } else if (id == R.id.btnBackward) {
            mediaPlayer.seekTo(Math.max(0, mediaPlayer.getCurrentPosition() - 5000));

        } else if (id == R.id.btnForward) {
            mediaPlayer.seekTo(Math.min(mediaPlayer.getDuration(),
                    mediaPlayer.getCurrentPosition() + 5000));

        } else if (id == R.id.btnInfo) {
            int pos = mediaPlayer.getCurrentPosition();
            int dur = mediaPlayer.getDuration();

            Log.d(LOG_TAG, "pos=" + pos + " dur=" + dur
                    + " looping=" + mediaPlayer.isLooping()
                    + " vol=" + am.getStreamVolume(AudioManager.STREAM_MUSIC));

            if (dur > 0) {
                progressBar.setMax(dur);
                progressBar.setProgress(pos);
            }
            tvState.setText("pos " + pos / 1000 + "s / " + dur / 1000 + "s");
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(LOG_TAG, "onPrepared");
        mp.start();
        tvState.setText("playing");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(LOG_TAG, "onCompletion");
        tvState.setText("completed");
    }

    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception ignored) {
            }
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
    }
}