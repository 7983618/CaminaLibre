package com.example.caminalibre.Database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.caminalibre.modelo.PuntoInteres;
import java.util.List;

@Dao
public interface DAOPuntosDeInteres {
    @Insert
    void insert(PuntoInteres puntoInteres);

    @Insert
    void insertAll(List<PuntoInteres> puntosInteres);

    @Update
    void update(PuntoInteres puntoInteres);

    @Delete
    void delete(PuntoInteres puntoInteres);

    @Query("SELECT * FROM puntos_interes WHERE ruta_id = :id")
    LiveData<List<PuntoInteres>> getPuntosDeInteres(long id);
}
