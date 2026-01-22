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
    void inserall(ArrayList<Ruta> rutas);

    @Update
    void updateRuta(Ruta ruta);

    @Insert
    void inserall(Ruta... ruta);

    @Delete
    void deleteRuta(Ruta ruta);

    @Query("SELECT * FROM rutas WHERE id = :id")
    Ruta getRuta(int id);

    @Query("SELECT * FROM rutas")
    LiveData<List<Ruta>> getAllRutas();

    @Query("SELECT * FROM rutas WHERE dificultad = :dificultad")
    LiveData<List<Ruta>> getRutasPorDificultad(float dificultad);
}
