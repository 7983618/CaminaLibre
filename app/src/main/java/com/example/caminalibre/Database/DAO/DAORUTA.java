package com.example.caminalibre.Database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.caminalibre.modelo.Ruta;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DAORUTA {

    @Insert
    void createAll(ArrayList<Ruta> rutas);

    @Update
    void update(Ruta ruta);

    @Insert
    void create(Ruta... ruta);

    @Delete
    void delete(Ruta ruta);

    @Query("SELECT * FROM rutas WHERE id = :id")
    Ruta read(int id);

    @Query("SELECT * FROM rutas")
    LiveData<List<Ruta>> readAll();

}
