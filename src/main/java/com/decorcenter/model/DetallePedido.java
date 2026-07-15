package com.decorcenter.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Representa una línea de un pedido. También se reutiliza como
 * item del carrito de compras en sesión.
 */
public class DetallePedido implements Serializable {

    private int id;
    private int pedidoId;
    private int productoId;
    private String productoNombre;
    private String productoImagen;
    private BigDecimal precioUnitario;
    private int cantidad;
    private BigDecimal subtotal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public String getProductoImagen() {
        return productoImagen;
    }

    public void setProductoImagen(String productoImagen) {
        this.productoImagen = productoImagen;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public void recalcularSubtotal() {
        this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
