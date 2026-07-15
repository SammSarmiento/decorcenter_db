package com.decorcenter.controller.admin;

import com.decorcenter.factory.DAOFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int totalProductos = DAOFactory.getProductoDAO().listarTodos().size();
        int totalCategorias = DAOFactory.getCategoriaDAO().listarTodas().size();
        int totalPedidos = DAOFactory.getPedidoDAO().listarTodos().size();

        req.setAttribute("totalProductos", totalProductos);
        req.setAttribute("totalCategorias", totalCategorias);
        req.setAttribute("totalPedidos", totalPedidos);
        req.setAttribute("pedidos", DAOFactory.getPedidoDAO().listarTodos());

        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }
}
