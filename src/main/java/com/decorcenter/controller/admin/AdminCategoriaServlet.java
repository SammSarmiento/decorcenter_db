package com.decorcenter.controller.admin;

import com.decorcenter.factory.DAOFactory;
import com.decorcenter.model.Categoria;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "AdminCategoriaServlet", urlPatterns = {
        "/admin/categorias", "/admin/categorias/guardar", "/admin/categorias/eliminar"})
public class AdminCategoriaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String uri = req.getRequestURI();

        if (uri.endsWith("/admin/categorias/eliminar")) {
            int id = Integer.parseInt(req.getParameter("id"));
            DAOFactory.getCategoriaDAO().eliminar(id);
            resp.sendRedirect(req.getContextPath() + "/admin/categorias");
            return;
        }

        req.setAttribute("categorias", DAOFactory.getCategoriaDAO().listarTodas());
        req.getRequestDispatcher("/WEB-INF/views/admin/categorias.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");

        Categoria categoria = new Categoria();
        categoria.setNombre(req.getParameter("nombre"));
        categoria.setDescripcion(req.getParameter("descripcion"));

        if (idParam != null && !idParam.isEmpty()) {
            categoria.setId(Integer.parseInt(idParam));
            DAOFactory.getCategoriaDAO().actualizar(categoria);
        } else {
            DAOFactory.getCategoriaDAO().guardar(categoria);
        }

        resp.sendRedirect(req.getContextPath() + "/admin/categorias");
    }
}
