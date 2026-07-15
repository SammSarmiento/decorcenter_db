package com.decorcenter.controller;

import com.decorcenter.dao.UsuarioDAO;
import com.decorcenter.factory.DAOFactory;
import com.decorcenter.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        System.out.println("=== INTENTO DE LOGIN ===");
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        UsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();
        Usuario usuario = usuarioDAO.login(email, password);

        if (usuario != null) {
            System.out.println("LOGIN EXITOSO → Rol: " + usuario.getRol());
            HttpSession session = req.getSession();
            session.setAttribute("usuario", usuario);

            if (usuario.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
        } else {
            System.out.println("LOGIN FALLIDO - Usuario no encontrado o contraseña incorrecta");
            req.setAttribute("error", "Correo o contraseña incorrectos");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}