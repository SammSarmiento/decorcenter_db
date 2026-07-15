<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="tituloPagina" value="Categorías — Panel Admin" />
<c:set var="paginaActiva" value="categorias" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="panel-admin">
    <jsp:include page="/WEB-INF/views/includes/admin_sidebar.jsp" />

    <div class="contenido-admin">
        <div class="seccion-titulo">
            <h2>Categorías</h2>
        </div>

        <div style="display:grid; grid-template-columns: 1fr 320px; gap:30px; align-items:start;">
            <table class="tabla-admin">
                <thead>
                <tr><th>Nombre</th><th>Descripción</th><th>Acciones</th></tr>
                </thead>
                <tbody>
                <c:forEach var="cat" items="${categorias}">
                    <tr>
                        <td><c:out value="${cat.nombre}"/></td>
                        <td><c:out value="${cat.descripcion}"/></td>
                        <td class="acciones-tabla">
                            <a class="eliminar" href="${pageContext.request.contextPath}/admin/categorias/eliminar?id=${cat.id}"
                               onclick="return confirm('¿Eliminar esta categoría?');">Eliminar</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <div class="form-card" style="margin:0;">
                <h3 class="titulo-serif" style="margin-top:0;">Nueva categoría</h3>
                <form action="${pageContext.request.contextPath}/admin/categorias/guardar" method="post">
                    <div class="form-grupo">
                        <label>Nombre</label>
                        <input type="text" name="nombre" required>
                    </div>
                    <div class="form-grupo">
                        <label>Descripción</label>
                        <textarea name="descripcion"></textarea>
                    </div>
                    <button type="submit" class="btn btn-primario" style="width:100%;">Agregar categoría</button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
