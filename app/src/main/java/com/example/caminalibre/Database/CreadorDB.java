package com.example.caminalibre.Database;



import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.caminalibre.Database.DAO.DAORUTA;
import com.example.caminalibre.modelo.Ruta;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Ruta.class}, version = 1)
public abstract class CreadorDB extends RoomDatabase {
    public abstract DAORUTA getDAO();
    private static CreadorDB INSTANCE;

    public static final ExecutorService ejecutarhilo =  Executors.newFixedThreadPool(4);
   public static CreadorDB getDatabase(Context context){
    if (INSTANCE == null){
        synchronized (CreadorDB.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder
                                (context.getApplicationContext(), CreadorDB.class, "rutas.db")
                        .allowMainThreadQueries() // permite realizar operaciones en el hilo principal
                        .fallbackToDestructiveMigration() // si modificas clase borra y crea
                        .build();
            }
        }
    }
    return INSTANCE;
   };
}
