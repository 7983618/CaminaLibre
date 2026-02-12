package com.example.caminalibre.servicios;


import static com.example.caminalibre.R.raw.sequoia;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.caminalibre.R;

public class MusicService extends Service {
    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_STOP = "STOP";
    private MediaPlayer mediaPlayer; // Reproductor de audio

    @Override
    public void onCreate() {
        super.onCreate(); // Ciclo de vida base
        mediaPlayer = MediaPlayer.create(this, R.raw.sequoia); // Carga el archivo de audio
        mediaPlayer.setLooping(true); // Repetición infinita
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getAction() == null) return START_STICKY;
        String action = intent.getAction();
        if (mediaPlayer == null) return START_STICKY;
        if (action.equals(ACTION_PLAY) && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        } else if (action.equals(ACTION_STOP) && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        return START_STICKY; // Si el sistema mata la app, intenta revivir el servicio
    }
    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}
}
