package com.decorcenter.filter;

import com.decorcenter.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI().toLowerCase();
        String context = req.getContextPath().toLowerCase();

        // 1. RECURSOS ESTÁTICOS - SIEMPRE DEJAR PASAR
        if (path.contains("/css/") || path.contains("/images/") || 
            path.contains("/js/") || path.contains("/assets/") ||
            path.endsWith(".jpg") || path.endsWith(".jpeg") || 
            path.endsWith(".png") || path.endsWith(".webp") || 
            path.endsWith(".gif") || path.endsWith(".css") || 
            path.endsWith(".js") || path.endsWith(".ico")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Rutas públicas
        if (path.endsWith("/login") || path.endsWith("/registro") || 
            path.endsWith("/home") || path.equals(context + "/") || path.equals(context)) {
            chain.doFilter(request, response);
            return;
        }

        // 3. Control de sesión
        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        // Rutas de admin
        if (path.contains("/admin/")) {
            if (usuario != null && "ADMIN".equalsIgnoreCase(usuario.getRol())) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(req.getContextPath() + "/login?error=no_autorizado");
            }
            return;
        }

        // Carrito y pedidos solo para clientes logueados
        if (path.contains("/carrito") || path.contains("/pedido")) {
            if (usuario == null) {
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}