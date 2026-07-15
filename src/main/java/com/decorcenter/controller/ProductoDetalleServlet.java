package com.decorcenter.controller;

import com.decorcenter.factory.DAOFactory;
import com.decorcenter.model.Producto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ProductoDetalleServlet", urlPatterns = {"/producto"})
public class ProductoDetalleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        Producto producto = DAOFactory.getProductoDAO().buscarPorId(id);

        if (producto == null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        req.setAttribute("producto", producto);
        req.getRequestDispatcher("/WEB-INF/views/producto_detalle.jsp").forward(req, resp);
    }
}
