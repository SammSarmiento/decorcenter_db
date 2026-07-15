package com.decorcenter.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Producto implements Serializable {

    private int id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private String imagenUrl;
    private int categoriaId;
    private String categoriaNombre; // usado para mostrar en las vistas (JOIN)

    public Producto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }
// Agrega este campo junto a los demás
    private String imagenFavorito;

// Agrega estos dos métodos
    public String getImagenFavorito() {
        return imagenFavorito;
    }

    public void setImagenFavorito(String imagenFavorito) {
        this.imagenFavorito = imagenFavorito;
    }
}
