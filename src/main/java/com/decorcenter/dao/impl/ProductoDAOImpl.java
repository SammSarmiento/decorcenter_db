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
        String sql = "SELECT p.*, c.nombre AS categoria_nombre, " +
                     "(SELECT pi.imagen_url FROM producto_imagenes pi " +
                     " WHERE pi.producto_id = p.id ORDER BY pi.orden ASC LIMIT 1) AS imagen_url " +
                     "FROM productos p " +
                     "LEFT JOIN categorias c ON p.id_categoria = c.id " +
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
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock, id_categoria) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, producto.getNombre());
                ps.setString(2, producto.getDescripcion());
                ps.setBigDecimal(3, producto.getPrecio());
                ps.setInt(4, producto.getStock());
                ps.setInt(5, producto.getCategoriaId());

                if (ps.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        producto.setId(id);

                        if (producto.getImagenUrl() != null && !producto.getImagenUrl().trim().isEmpty()) {
                            String sqlImg = "INSERT INTO producto_imagenes (producto_id, imagen_url, orden) VALUES (?, ?, 0)";
                            try (PreparedStatement psi = conn.prepareStatement(sqlImg)) {
                                psi.setInt(1, id);
                                psi.setString(2, producto.getImagenUrl().trim());
                                psi.executeUpdate();
                            }
                        }
                    }
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS categoria_nombre, " +
                     "(SELECT pi.imagen_url FROM producto_imagenes pi " +
                     " WHERE pi.producto_id = p.id ORDER BY pi.orden ASC LIMIT 1) AS imagen_url " +
                     "FROM productos p " +
                     "LEFT JOIN categorias c ON p.id_categoria = c.id " +
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
        String sql = "SELECT p.*, c.nombre AS categoria_nombre, " +
                     "(SELECT pi.imagen_url FROM producto_imagenes pi " +
                     " WHERE pi.producto_id = p.id ORDER BY pi.orden ASC LIMIT 1) AS imagen_url " +
                     "FROM productos p " +
                     "LEFT JOIN categorias c ON p.id_categoria = c.id " +
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
        String sql = "SELECT p.*, c.nombre AS categoria_nombre, " +
                     "(SELECT pi.imagen_url FROM producto_imagenes pi " +
                     " WHERE pi.producto_id = p.id ORDER BY pi.orden ASC LIMIT 1) AS imagen_url " +
                     "FROM productos p " +
                     "LEFT JOIN categorias c ON p.id_categoria = c.id " +
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
        String sql = "UPDATE productos SET nombre=?, descripcion=?, precio=?, stock=?, id_categoria=? WHERE id=?";
        Connection conn = null;
        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, producto.getNombre());
                ps.setString(2, producto.getDescripcion());
                ps.setBigDecimal(3, producto.getPrecio());
                ps.setInt(4, producto.getStock());
                ps.setInt(5, producto.getCategoriaId());
                ps.setInt(6, producto.getId());

                if (ps.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }

                if (producto.getImagenUrl() != null && !producto.getImagenUrl().trim().isEmpty()) {
                    try (PreparedStatement del = conn.prepareStatement(
                            "DELETE FROM producto_imagenes WHERE producto_id = ?")) {
                        del.setInt(1, producto.getId());
                        del.executeUpdate();
                    }
                    try (PreparedStatement psi = conn.prepareStatement(
                            "INSERT INTO producto_imagenes (producto_id, imagen_url, orden) VALUES (?, ?, 0)")) {
                        psi.setInt(1, producto.getId());
                        psi.setString(2, producto.getImagenUrl().trim());
                        psi.executeUpdate();
                    }
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
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
        p.setCategoriaId(rs.getInt("id_categoria"));
        p.setImagenUrl(rs.getString("imagen_url"));

        try {
            p.setCategoriaNombre(rs.getString("categoria_nombre"));
        } catch (Exception ignored) {}

        return p;
    }
}
