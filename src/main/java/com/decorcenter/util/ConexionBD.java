package com.decorcenter.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * PATRÓN DE DISEÑO 1: SINGLETON
 * ------------------------------------------------------------
 * Garantiza que exista una única instancia encargada de
 * suministrar conexiones a la base de datos, evitando crear
 * múltiples configuraciones repetidas a lo largo de la app.
 */

public class ConexionBD {
    // Asegúrate de que el nombre de la BD coincida con el que creaste en pgAdmin
    private static final String URL = "jdbc:postgresql://localhost:5432/decorcenter_db";
    private static final String USER = "postgres"; 
    private static final String PASSWORD = "admin"; // Cambia esto por tu clave real

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error al cargar el driver de PostgreSQL", e);
        }
    }
}