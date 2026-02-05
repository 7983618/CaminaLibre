package com.example.caminalibre.modelo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "rutas")
public class Ruta implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "nombreRuta")
    private String nombreRuta;

    @ColumnInfo(name = "localizacion")
    private String localizacion;
    private Tipo tipo;

    @ColumnInfo(name = "dificultad")
    private float dificultad;
    @ColumnInfo(name = "distancia")
    private double distancia;
    @ColumnInfo(name = "descripcion")
    private String descripcion;

    @ColumnInfo(name = "notas")
    private String notas;

    @ColumnInfo(name = "favorita")
    private boolean favorita;
    @ColumnInfo(name = "latitud")
    private double latitud;
    @ColumnInfo(name = "longitud")
    private double longitud;

    @ColumnInfo(name = "foto")
    private String foto;
    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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



    public Ruta(String nombreRuta, String localizacion, Tipo tipo, float dificultad, double distancia, String descripcion, String notas, boolean favorita){
        this.nombreRuta = nombreRuta;
        this.localizacion = localizacion;
        this.tipo = tipo;
        this.dificultad = dificultad;
        this.distancia = distancia;
        this.descripcion = descripcion;
        this.notas = notas;
        this.favorita = favorita;
    }

    public Ruta() {
    }
    @Ignore
    public Ruta(String nombreRuta, String localizacion, Tipo tipo, float dificultad, double distancia, String descripcion, String notas, boolean favorita, double latitud, double longitud) {
        this.nombreRuta = nombreRuta;
        this.localizacion = localizacion;
        this.tipo = tipo;
        this.dificultad = dificultad;
        this.distancia = distancia;
        this.descripcion = descripcion;
        this.notas = notas;
        this.favorita = favorita;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public boolean isFavorita() {
        return favorita;
    }

    public void setFavorita(boolean favorita) {
        this.favorita = favorita;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public float getDificultad() {
        return dificultad;
    }

    public void setDificultad(float dificultad) {
        this.dificultad = dificultad;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public void setNombreRuta(String nombreRuta) {
        this.nombreRuta = nombreRuta;
    }
}
