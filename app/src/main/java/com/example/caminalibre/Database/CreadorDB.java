package com.example.caminalibre.Database;



import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.caminalibre.Database.DAO.DAO;
import com.example.caminalibre.modelo.Ruta;

@Database(entities = {Ruta.class}, version = 1)
public abstract class CreadorDB extends RoomDatabase {
    public abstract DAO getDAO();
    private static CreadorDB INSTANCE;

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
