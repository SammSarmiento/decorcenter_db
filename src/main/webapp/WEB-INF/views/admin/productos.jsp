<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<c:set var="tituloPagina" value="Productos — Panel Admin" />
<c:set var="paginaActiva" value="productos" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="panel-admin">
    <jsp:include page="/WEB-INF/views/includes/admin_sidebar.jsp" />

    <div class="contenido-admin">
        <div class="seccion-titulo">
            <h2>Productos</h2>
            <a class="btn btn-acento" href="${pageContext.request.contextPath}/admin/productos/nuevo">+ Nuevo producto</a>
        </div>

        <c:if test="${not empty errorAdmin}">
            <div class="alerta alerta-error" style="margin-bottom:20px;">
                <c:out value="${errorAdmin}"/>
            </div>
        </c:if>

        <table class="tabla-admin">
            <thead>
            <tr>
                <th>Nombre</th>
                <th>Categoría</th>
                <th>Precio</th>
                <th>Stock</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="p" items="${productos}">
                <tr>
                    <td><c:out value="${p.nombre}"/></td>
                    <td><c:out value="${p.categoriaNombre != null ? p.categoriaNombre : '—'}"/></td>
                    <td>S/ <fmt:formatNumber value="${p.precio}" pattern="#,##0.00"/></td>
                    <td><c:out value="${p.stock}"/></td>
                    <td class="acciones-tabla">
                        <a href="${pageContext.request.contextPath}/admin/productos/editar?id=${p.id}">Editar</a>
                        <a class="eliminar" href="${pageContext.request.contextPath}/admin/productos/eliminar?id=${p.id}"
                           onclick="return confirm('¿Eliminar este producto?');">Eliminar</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty productos}">
                <tr>
                    <td colspan="5" style="text-align:center;padding:30px;color:#888;">
                        No hay productos registrados.
                    </td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
