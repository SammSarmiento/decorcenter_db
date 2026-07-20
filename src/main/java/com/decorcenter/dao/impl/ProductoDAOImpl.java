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
        // OJO: la tabla "productos" NO tiene columna imagen_url (la imagen se
        // guarda aparte en "producto_imagenes"). Antes este INSERT intentaba
        // escribir en una columna inexistente y siempre fallaba.
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock, id_categoria) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection()) {
            conn.setAutoCommit(false);
            int nuevoId;
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, producto.getNombre());
                ps.setString(2, producto.getDescripcion());
                ps.setBigDecimal(3, producto.getPrecio());
                ps.setInt(4, producto.getStock());
                ps.setInt(5, producto.getCategoriaId());
                int filas = ps.executeUpdate();
                if (filas == 0) {
                    conn.rollback();
                    return false;
                }
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        conn.rollback();
                        return false;
                    }
                    nuevoId = keys.getInt(1);
                }
            }

            if (producto.getImagenDatos() != null && producto.getImagenDatos().length > 0) {
                guardarImagen(conn, nuevoId, producto.getImagenDatos(), producto.getImagenTipo());
            }

            conn.commit();
            producto.setId(nuevoId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void guardarImagen(Connection conn, int productoId, byte[] datos, String tipo) throws SQLException {
        // Modelo de "una imagen principal por producto": se borra la anterior
        try (PreparedStatement psDel = conn.prepareStatement(
                "DELETE FROM producto_imagenes WHERE producto_id = ?")) {
            psDel.setInt(1, productoId);
            psDel.executeUpdate();
        }

        int imagenId;
        try (PreparedStatement psIns = conn.prepareStatement(
                "INSERT INTO producto_imagenes (producto_id, imagen_url, imagen_datos, imagen_tipo, orden) " +
                "VALUES (?, NULL, ?, ?, 0)", Statement.RETURN_GENERATED_KEYS)) {
            psIns.setInt(1, productoId);
            psIns.setBytes(2, datos);
            psIns.setString(3, tipo);
            psIns.executeUpdate();
            try (ResultSet keys = psIns.getGeneratedKeys()) {
                keys.next();
                imagenId = keys.getInt(1);
            }
        }

        // La URL publica apunta al ImagenServlet, que sirve los bytes desde la BD
        try (PreparedStatement psUpd = conn.prepareStatement(
                "UPDATE producto_imagenes SET imagen_url = ? WHERE id = ?")) {
            psUpd.setString(1, "imagen?id=" + imagenId);
            psUpd.setInt(2, imagenId);
            psUpd.executeUpdate();
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
        // La columna imagen_url no existe en "productos": la imagen se
        // actualiza aparte, en producto_imagenes, solo si llega una nueva.
        String sql = "UPDATE productos SET nombre=?, descripcion=?, precio=?, stock=?, id_categoria=? WHERE id=?";

        try (Connection conn = ConexionBD.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, producto.getNombre());
                ps.setString(2, producto.getDescripcion());
                ps.setBigDecimal(3, producto.getPrecio());
                ps.setInt(4, producto.getStock());
                ps.setInt(5, producto.getCategoriaId());
                ps.setInt(6, producto.getId());
                int filas = ps.executeUpdate();
                if (filas == 0) {
                    conn.rollback();
                    return false;
                }
            }

            boolean tieneImagenNueva = producto.getImagenDatos() != null && producto.getImagenDatos().length > 0;
            if (tieneImagenNueva) {
                guardarImagen(conn, producto.getId(), producto.getImagenDatos(), producto.getImagenTipo());
            }

            conn.commit();
            return true;
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
    p.setCategoriaId(rs.getInt("id_categoria"));

    // Esta línea trae la imagen desde la subconsulta
    p.setImagenUrl(rs.getString("imagen_url"));

    // Solo intentamos leer categoria_nombre si existe
    try {
        p.setCategoriaNombre(rs.getString("categoria_nombre"));
    } catch (Exception ignored) {}

    return p;
}
}
