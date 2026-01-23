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
    @Query("SELECT * FROM rutas")
    List<Ruta> readAllSync();


    @Query("SELECT * FROM rutas WHERE " +
            "(:filtro = 'Todas') OR " +
            "(:filtro = 'Facil' AND dificultad <= 1.5) OR " +
            "(:filtro = 'Media' AND dificultad > 1.5 AND dificultad <= 3.5) OR " +
            "(:filtro = 'Dificil' AND dificultad > 3.5)")
    LiveData<List<Ruta>> readFiltradas(String filtro);

}
