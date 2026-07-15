package com.decorcenter.controller;

import com.decorcenter.dao.UsuarioDAO;
import com.decorcenter.factory.DAOFactory;
import com.decorcenter.model.Usuario;
import com.decorcenter.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/registro"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String nombre = req.getParameter("nombre");
        String email = req.getParameter("email");
        String direccion = req.getParameter("direccion");
        String telefono = req.getParameter("telefono");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        // Validación básica
        if (password == null || !password.equals(confirmPassword)) {
            req.setAttribute("error", "Las contraseñas no coinciden.");
            req.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(req, resp);
            return;
        }

        if (password.length() < 6) {
            req.setAttribute("error", "La contraseña debe tener al menos 6 caracteres.");
            req.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(req, resp);
            return;
        }

        UsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();

        // Verificar si el correo ya existe
        if (usuarioDAO.existeEmail(email)) {
            req.setAttribute("error", "Este correo ya está registrado.");
            req.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(req, resp);
            return;
        }

        // Crear usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setDireccion(direccion);
        nuevoUsuario.setTelefono(telefono);
        nuevoUsuario.setPassword(PasswordUtil.hash(password));
        nuevoUsuario.setRol("CLIENTE");

        boolean exito = usuarioDAO.registrar(nuevoUsuario);

        if (exito) {
            // Registro exitoso → redirigir al login
            resp.sendRedirect(req.getContextPath() + "/login?registro=exito");
        } else {
            req.setAttribute("error", "Ocurrió un error al registrar tu cuenta. Intenta nuevamente.");
            req.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(req, resp);
        }
    }
}