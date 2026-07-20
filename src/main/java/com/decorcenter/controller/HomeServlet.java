package com.decorcenter.controller;

import com.decorcenter.factory.DAOFactory;
import com.decorcenter.model.Categoria;
import com.decorcenter.model.Producto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home", "/"})
public class HomeServlet extends HttpServlet {

    @Override
   
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    String categoriaParam = req.getParameter("categoria");
    String buscar = req.getParameter("buscar");


    List<Producto> productos;
    if (buscar != null && !buscar.trim().isEmpty()) {
        productos = DAOFactory.getProductoDAO().buscarPorNombre(buscar.trim());
    } else if (categoriaParam != null && !categoriaParam.isEmpty()) {
        productos = DAOFactory.getProductoDAO().listarPorCategoria(Integer.parseInt(categoriaParam));
    } else {
        productos = DAOFactory.getProductoDAO().listarTodos();
    }

    List<Producto> favoritos = new ArrayList<>();
    favoritos.add(DAOFactory.getProductoDAO().buscarPorId(10)); // favoritas1.jpg
    favoritos.add(DAOFactory.getProductoDAO().buscarPorId(5));  // favoritas2.jpg
    favoritos.add(DAOFactory.getProductoDAO().buscarPorId(8));  // favoritas3.jpg
    favoritos.add(DAOFactory.getProductoDAO().buscarPorId(3));  // favoritas4.jpg
    favoritos.add(DAOFactory.getProductoDAO().buscarPorId(12)); // favoritas5.jpg

    List<Categoria> categorias = DAOFactory.getCategoriaDAO().listarTodas();

    req.setAttribute("productos", productos);
    req.setAttribute("favoritos", favoritos);
    req.setAttribute("categorias", categorias);
    req.setAttribute("categoriaSeleccionada", categoriaParam);
    req.setAttribute("buscar", buscar);

    req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
}
}
