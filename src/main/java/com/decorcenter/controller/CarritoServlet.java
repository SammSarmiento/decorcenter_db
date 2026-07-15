package com.decorcenter.controller;

import com.decorcenter.factory.DAOFactory;
import com.decorcenter.model.DetallePedido;
import com.decorcenter.model.Pedido;
import com.decorcenter.model.Producto;
import com.decorcenter.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "CarritoServlet", urlPatterns = {"/carrito", "/carrito/agregar", "/carrito/eliminar", "/carrito/confirmar"})
public class CarritoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String uri = req.getRequestURI();

        if (uri.endsWith("/carrito/eliminar")) {
            eliminarDelCarrito(req);
            resp.sendRedirect(req.getContextPath() + "/carrito");
            return;
        }

        if (uri.endsWith("/carrito/confirmar")) {
            confirmarPedido(req, resp);
            return;
        }

        mostrarCarrito(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String uri = req.getRequestURI();
        if (uri.endsWith("/carrito/agregar")) {
            agregarAlCarrito(req);
            resp.sendRedirect(req.getContextPath() + "/carrito");
        } else {
            mostrarCarrito(req, resp);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, DetallePedido> getCarrito(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Map<Integer, DetallePedido> carrito = (Map<Integer, DetallePedido>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new LinkedHashMap<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    private void agregarAlCarrito(HttpServletRequest req) {
        int productoId = Integer.parseInt(req.getParameter("productoId"));
        int cantidad = Integer.parseInt(req.getParameter("cantidad"));

        Producto producto = DAOFactory.getProductoDAO().buscarPorId(productoId);
        if (producto == null) return;

        Map<Integer, DetallePedido> carrito = getCarrito(req);
        DetallePedido item = carrito.get(productoId);

        if (item == null) {
            item = new DetallePedido();
            item.setProductoId(producto.getId());
            item.setProductoNombre(producto.getNombre());
            item.setProductoImagen(producto.getImagenUrl());
            item.setPrecioUnitario(producto.getPrecio());
            item.setCantidad(cantidad);
        } else {
            item.setCantidad(item.getCantidad() + cantidad);
        }
        item.recalcularSubtotal();
        carrito.put(productoId, item);
    }

    private void eliminarDelCarrito(HttpServletRequest req) {
        int productoId = Integer.parseInt(req.getParameter("productoId"));
        getCarrito(req).remove(productoId);
    }

    private void mostrarCarrito(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<Integer, DetallePedido> carrito = getCarrito(req);

        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedido d : carrito.values()) {
            total = total.add(d.getSubtotal());
        }

        req.setAttribute("items", carrito.values());
        req.setAttribute("total", total);
        req.getRequestDispatcher("/WEB-INF/views/usuario/carrito.jsp").forward(req, resp);
    }

    private void confirmarPedido(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Map<Integer, DetallePedido> carrito = getCarrito(req);

        if (carrito.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/carrito");
            return;
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuario.getId());
        pedido.setEstado("PENDIENTE");

        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedido d : carrito.values()) {
            total = total.add(d.getSubtotal());
            pedido.getDetalles().add(d);
        }
        pedido.setTotal(total);

        boolean creado = DAOFactory.getPedidoDAO().crearPedido(pedido);

        if (creado) {
            carrito.clear();
            req.setAttribute("pedido", pedido);
            req.getRequestDispatcher("/WEB-INF/views/usuario/pedido_confirmado.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", "No se pudo confirmar el pedido. Intenta nuevamente.");
            mostrarCarrito(req, resp);
        }
    }
}
