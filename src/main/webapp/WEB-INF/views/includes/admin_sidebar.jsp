<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<aside class="sidebar-admin">
    <a href="${pageContext.request.contextPath}/admin/dashboard" class="${paginaActiva == 'dashboard' ? 'activo' : ''}">Dashboard</a>
    <a href="${pageContext.request.contextPath}/admin/productos" class="${paginaActiva == 'productos' ? 'activo' : ''}">Productos</a>
    <a href="${pageContext.request.contextPath}/admin/categorias" class="${paginaActiva == 'categorias' ? 'activo' : ''}">Categorí­as</a>
    <a href="${pageContext.request.contextPath}/home">Ver tienda</a>
    <a href="${pageContext.request.contextPath}/logout">Cerrar sesión</a>
</aside>
