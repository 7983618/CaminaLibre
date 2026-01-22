package com.example.caminalibre.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.caminalibre.modelo.PuntoInteres;
import java.util.List;

@Dao
public interface DAOPuntosDeInteres {
    @Insert
    void inserall(List<PuntoInteres> puntosInteres);

    @Query("SELECT * FROM puntos_interes WHERE ruta_id = :id")
    public List<PuntoInteres> getPuntosDeInteres(int id);
}
