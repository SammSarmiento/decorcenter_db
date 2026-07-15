package com.decorcenter.dao.impl;

import com.decorcenter.dao.PedidoDAO;
import com.decorcenter.model.DetallePedido;
import com.decorcenter.model.Pedido;
import com.decorcenter.util.ConexionBD;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public boolean crearPedido(Pedido pedido) {
        boolean exito = false;
        
        // SQL adaptado a tu tabla real
        String sqlPedido = "INSERT INTO pedidos (id_usuario, numero_boleta, subtotal, igv, total, estado) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false);

            // Calcular subtotal, IGV (15%) y total
            BigDecimal subtotal = BigDecimal.ZERO;
            for (DetallePedido d : pedido.getDetalles()) {
                subtotal = subtotal.add(d.getSubtotal());
            }
            BigDecimal igv = subtotal.multiply(new BigDecimal("0.15")).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal total = subtotal.add(igv);
            
            String numeroBoleta = generarNumeroBoleta(conn);

            try (PreparedStatement psPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                psPedido.setInt(1, pedido.getUsuarioId());
                psPedido.setString(2, numeroBoleta);
                psPedido.setBigDecimal(3, subtotal);
                psPedido.setBigDecimal(4, igv);
                psPedido.setBigDecimal(5, total);
                psPedido.setString(6, pedido.getEstado() != null ? pedido.getEstado() : "PENDIENTE");
                psPedido.executeUpdate();

                try (ResultSet rsKeys = psPedido.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        int idPedido = rsKeys.getInt(1);
                        pedido.setId(idPedido);
                        pedido.setNumeroBoleta(numeroBoleta);
                        pedido.setTotal(total);

                        try (PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle)) {
                            for (DetallePedido detalle : pedido.getDetalles()) {
                                psDetalle.setInt(1, idPedido);
                                psDetalle.setInt(2, detalle.getProductoId());
                                psDetalle.setInt(3, detalle.getCantidad());
                                psDetalle.setBigDecimal(4, detalle.getPrecioUnitario());
                                psDetalle.setBigDecimal(5, detalle.getSubtotal());
                                psDetalle.addBatch();
                            }
                            psDetalle.executeBatch();
                        }
                    }
                }
            }
            conn.commit();
            exito = true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return exito;
    }

    private String generarNumeroBoleta(Connection conn) throws SQLException {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 FROM pedidos";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int siguiente = rs.getInt(1);
                return "BOL-" + String.format("%06d", siguiente);
            }
        }
        return "BOL-000001";
    }

    @Override
    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos ORDER BY id DESC";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt("id"));
                p.setUsuarioId(rs.getInt("id_usuario"));
                p.setNumeroBoleta(rs.getString("numero_boleta"));
                p.setTotal(rs.getBigDecimal("total"));
                p.setEstado(rs.getString("estado"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}