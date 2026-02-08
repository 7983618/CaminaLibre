package com.example.caminalibre.servicios;


import static com.example.caminalibre.R.raw.sequoia;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.example.caminalibre.R;

public class MusicService extends Service {
    public static boolean escuchando = false; // Chivato de estado (true=suena)
    private MediaPlayer mediaPlayer; // Reproductor de audio

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) { // Evita errores si el intent llega vacío
            String action = intent.getAction(); // Obtiene la orden (PLAY/STOP)
            if ("PLAY".equals(action)) { // Si la orden es reproducir
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) { // Si existe y no suena
                    mediaPlayer.start(); // Inicia la música
                    escuchando = true; // Marca como sonando
                }
            } else if ("STOP".equals(action)) { // Si la orden es parar
                if (mediaPlayer != null && mediaPlayer.isPlaying()) { // Si existe y suena
                    mediaPlayer.pause(); // Pausa la música
                    escuchando = false; // Marca como parado
                }
            }
        }
        return START_STICKY; // Si el sistema mata la app, intenta revivir el servicio
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // Se dispara al cerrar la app desde el gestor de tareas
        if (mediaPlayer != null) { // Si hay reproductor
            mediaPlayer.stop(); // Para la música
            mediaPlayer.release(); // Libera la memoria RAM
        }
        stopSelf(); // Cierra el servicio definitivamente
        super.onTaskRemoved(rootIntent); // Lógica base del sistema
    }

    @Override
    public void onDestroy() {
        super.onDestroy(); // Ciclo de vida base
        if (mediaPlayer != null) { // Si el reproductor existe
            if (mediaPlayer.isPlaying()) { // Si está sonando
                mediaPlayer.stop(); // Detiene el sonido
            }
            mediaPlayer.release(); // Suelta los recursos de hardware
        }
    }

    @Override
    public void onCreate() {
        super.onCreate(); // Ciclo de vida base
        mediaPlayer = MediaPlayer.create(this, sequoia); // Carga el archivo de audio
        mediaPlayer.setLooping(true); // Repetición infinita
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // No permite comunicación directa entre clases
    }
}
