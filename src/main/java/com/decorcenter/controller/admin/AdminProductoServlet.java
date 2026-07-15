package com.decorcenter.controller.admin;

import com.decorcenter.factory.DAOFactory;
import com.decorcenter.model.Producto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.List;

@WebServlet(name = "AdminProductoServlet", urlPatterns = {
        "/admin/productos",
        "/admin/productos/nuevo",
        "/admin/productos/editar",
        "/admin/producto",
        "/admin/productos/eliminar"
})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 15
)
public class AdminProductoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        // ===== ELIMINAR =====
        if (uri.contains("/admin/productos/eliminar")) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                boolean ok = DAOFactory.getProductoDAO().eliminar(id);
                if (!ok) {
                    request.getSession().setAttribute("errorAdmin", "No se pudo eliminar el producto (puede tener pedidos asociados).");
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("errorAdmin", "Error al eliminar: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/admin/productos");
            return;
        }

        // ===== EDITAR (cargar formulario con datos) =====
        if (uri.contains("/admin/productos/editar")) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Producto producto = DAOFactory.getProductoDAO().buscarPorId(id);
                if (producto == null) {
                    response.sendRedirect(request.getContextPath() + "/admin/productos");
                    return;
                }
                request.setAttribute("producto", producto);
                request.setAttribute("esEdicion", true);
                request.getRequestDispatcher("/WEB-INF/views/admin/producto_form.jsp").forward(request, response);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/admin/productos");
                return;
            }
        }

        // ===== NUEVO =====
        if (uri.contains("/admin/productos/nuevo") || uri.endsWith("/admin/producto")) {
            request.setAttribute("esEdicion", false);
            request.getRequestDispatcher("/WEB-INF/views/admin/producto_form.jsp").forward(request, response);
            return;
        }

        // ===== LISTA =====
        List<Producto> productos = DAOFactory.getProductoDAO().listarTodos();
        request.setAttribute("productos", productos);
        
        // Mensaje de error de sesión (si hubo)
        Object error = request.getSession().getAttribute("errorAdmin");
        if (error != null) {
            request.setAttribute("errorAdmin", error);
            request.getSession().removeAttribute("errorAdmin");
        }
        
        request.getRequestDispatcher("/WEB-INF/views/admin/productos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String idParam = request.getParameter("id");
            boolean esEdicion = idParam != null && !idParam.trim().isEmpty();

            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            BigDecimal precio = new BigDecimal(request.getParameter("precio"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            int categoriaId = Integer.parseInt(request.getParameter("categoriaId"));

            // Procesar imagen (opcional en edición)
            Part filePart = request.getPart("imagen");
            String imagenUrl = null;

            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                fileName = fileName.replaceAll("[^a-zA-Z0-9.\\-_]", "_");

                String appPath = request.getServletContext().getRealPath("/");
                String uploadPath = appPath + "images";

                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                filePart.write(uploadPath + File.separator + fileName);
                imagenUrl = "images/" + fileName;
            }

            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setCategoriaId(categoriaId);
            producto.setImagenUrl(imagenUrl); // null si no se subió

            boolean ok;
            if (esEdicion) {
                producto.setId(Integer.parseInt(idParam));
                ok = DAOFactory.getProductoDAO().actualizar(producto);
            } else {
                ok = DAOFactory.getProductoDAO().guardar(producto);
            }

            if (ok) {
                response.sendRedirect(request.getContextPath() + "/admin/productos");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/productos/nuevo?error=true");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/productos/nuevo?error=true");
        }
    }
}
