package com.example.caminalibre.modelo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "puntos_interes",
        foreignKeys = @ForeignKey(entity = Ruta.class,
                parentColumns = "id",
                childColumns = "ruta_id",
                onDelete = ForeignKey.CASCADE))
public class PuntoInteres {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "nombre")
    private String nombre;
    @ColumnInfo(name = "latitud")
    private double latitud;
    @ColumnInfo(name = "longitud")
    private double longitud;
    @ColumnInfo(name = "imagen")
    private String ruta_imagen;
    @ColumnInfo(name = "ruta_id")
    private long ruta_id;
    
}
