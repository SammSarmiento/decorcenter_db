package com.decorcenter.dao.impl;

import com.decorcenter.dao.UsuarioDAO;
import com.decorcenter.model.Usuario;
import com.decorcenter.util.ConexionBD;
import com.decorcenter.util.PasswordUtil;

import java.sql.*;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario buscarPorEmail(String email) {
        Usuario usuario = null;
        String sql = "SELECT id, nombres, apellidos, email, password, rol, rut, direccion, telefono FROM usuarios WHERE email = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = mapear(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    @Override
 
public Usuario login(String email, String password) {
    Usuario usuario = null;
    
    // 1. Buscamos al usuario solo por email
    String sql = "SELECT id, nombres, apellidos, email, password, rol, rut, direccion, telefono " +
                 "FROM usuarios WHERE email = ?";
    
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, email);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                usuario = mapear(rs);
                
                // 2. Hasheamos la contraseña que escribió el usuario
                String hashedInput = PasswordUtil.hash(password);
                
                // 3. Comparamos con la contraseña hasheada que está en la BD
                if (!usuario.getPassword().equals(hashedInput)) {
                    return null; // Contraseña incorrecta
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return usuario;
}

    @Override
    public boolean registrar(Usuario usuario) {
        // RUT eliminado del INSERT (ya no lo pedimos en el registro)
        String sql = "INSERT INTO usuarios (nombres, apellidos, email, password, rol, direccion, telefono) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido() != null ? usuario.getApellido() : "");
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getPassword());
            ps.setString(5, usuario.getRol() != null ? usuario.getRol() : "CLIENTE");
            ps.setString(6, usuario.getDireccion());
            ps.setString(7, usuario.getTelefono());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existeEmail(String email) {
        String sql = "SELECT 1 FROM usuarios WHERE email = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNombre(rs.getString("nombres"));
        u.setApellido(rs.getString("apellidos"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setRol(rs.getString("rol"));
        u.setRut(rs.getString("rut"));           // Lo mantenemos por si ya existe en la BD
        u.setDireccion(rs.getString("direccion"));
        u.setTelefono(rs.getString("telefono"));
        return u;
    }
}