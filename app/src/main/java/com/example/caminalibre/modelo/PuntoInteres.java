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
    @ColumnInfo(name = "foto")
    private String foto;
    @ColumnInfo(name = "ruta_id")
    private long ruta_id;

    public PuntoInteres() {
    }

    public PuntoInteres(String nombre, double latitud, double longitud, String foto, long ruta_id) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.foto = foto;
        this.ruta_id = ruta_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public long getRuta_id() {
        return ruta_id;
    }

    public void setRuta_id(long ruta_id) {
        this.ruta_id = ruta_id;
    }
}
