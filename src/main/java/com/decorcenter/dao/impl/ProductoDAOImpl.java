package com.decorcenter.dao.impl;

import com.decorcenter.dao.ProductoDAO;
import com.decorcenter.model.Producto;
import com.decorcenter.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public Producto buscarPorId(int id) {
        Producto producto = null;
        String sql = "SELECT p.*, c.nombre AS categoria_nombre " +
                     "FROM productos p LEFT JOIN categorias c ON p.id_categoria = c.id " +
                     "WHERE p.id = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    producto = mapear(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return producto;
    }

    @Override
    public boolean guardar(Producto producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock, imagen_url, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getImagenUrl());
            ps.setInt(6, producto.getCategoriaId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS categoria_nombre " +
                     "FROM productos p LEFT JOIN categorias c ON p.id_categoria = c.id " +
                     "ORDER BY p.id";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Producto> listarPorCategoria(int categoriaId) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS categoria_nombre " +
                     "FROM productos p LEFT JOIN categorias c ON p.id_categoria = c.id " +
                     "WHERE p.id_categoria = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoriaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Producto> buscarPorNombre(String texto) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS categoria_nombre " +
                     "FROM productos p LEFT JOIN categorias c ON p.id_categoria = c.id " +
                     "WHERE p.nombre ILIKE ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + texto + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    @Override
    public boolean eliminar(int id) {
        // Borramos primero los detalles de pedidos asociados para evitar error de FK
        String sqlDetalle = "DELETE FROM detalle_pedidos WHERE id_producto = ?";
        String sql = "DELETE FROM productos WHERE id = ?";
        try (Connection conn = ConexionBD.getConnection()) {
            try (PreparedStatement psDet = conn.prepareStatement(sqlDetalle)) {
                psDet.setInt(1, id);
                psDet.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Producto producto) {
        String sql;
        boolean tieneImagen = producto.getImagenUrl() != null && !producto.getImagenUrl().trim().isEmpty();
        
        if (tieneImagen) {
            sql = "UPDATE productos SET nombre=?, descripcion=?, precio=?, stock=?, imagen_url=?, id_categoria=? WHERE id=?";
        } else {
            sql = "UPDATE productos SET nombre=?, descripcion=?, precio=?, stock=?, id_categoria=? WHERE id=?";
        }
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            
            if (tieneImagen) {
                ps.setString(5, producto.getImagenUrl());
                ps.setInt(6, producto.getCategoriaId());
                ps.setInt(7, producto.getId());
            } else {
                ps.setInt(5, producto.getCategoriaId());
                ps.setInt(6, producto.getId());
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarStock(int productoId, int nuevoStock) {
        String sql = "UPDATE productos SET stock = ? WHERE id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nuevoStock);
            ps.setInt(2, productoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setStock(rs.getInt("stock"));
        p.setImagenUrl(rs.getString("imagen_url"));
        p.setCategoriaId(rs.getInt("id_categoria"));
        p.setImagenFavorito(rs.getString("imagen_favorito"));
        
        try {
            p.setCategoriaNombre(rs.getString("categoria_nombre"));
        } catch (Exception ignored) {}
        return p;
    }
}
