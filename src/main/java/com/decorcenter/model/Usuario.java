package com.decorcenter.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Usuario implements Serializable {

    private int id;
    private String nombre;      // mapea a "nombres"
    private String apellido;    // mapea a "apellidos"
    private String email;
    private String password;
    private String rol;         // ADMIN o CLIENTE
    private String rut;
    private String direccion;
    private String telefono;
    private Timestamp fechaRegistro;

    public Usuario() {}

    public Usuario(String nombre, String apellido, String email, String password, String rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }


public boolean isAdmin() {
    return "ADMIN".equalsIgnoreCase(rol);
}

    public boolean isCliente() {
        return "CLIENTE".equalsIgnoreCase(rol);
    }
}