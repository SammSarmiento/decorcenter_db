<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="tituloPagina" value="Panel Admin — DecorCenter" />
<c:set var="paginaActiva" value="dashboard" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="panel-admin">
    <jsp:include page="/WEB-INF/views/includes/admin_sidebar.jsp" />

    <div class="contenido-admin">
        <div class="seccion-titulo">
            <h2>Dashboard</h2>
        </div>

        <div class="tarjetas-resumen">
            <div class="tarjeta-stat">
                <div class="valor"><c:out value="${totalProductos}"/></div>
                <div class="etiqueta">Productos activos</div>
            </div>
            <div class="tarjeta-stat">
                <div class="valor"><c:out value="${totalCategorias}"/></div>
                <div class="etiqueta">Categorías</div>
            </div>
            <div class="tarjeta-stat">
                <div class="valor"><c:out value="${totalPedidos}"/></div>
                <div class="etiqueta">Pedidos totales</div>
            </div>
        </div>

        <h3 class="titulo-serif">Últimos pedidos</h3>
        <table class="tabla-admin">
            <thead>
            <tr>
                <th>#</th>
                <th>Usuario</th>
                <th>Fecha</th>
                <th>Total</th>
                <th>Estado</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="pedido" items="${pedidos}">
                <tr>
                    <td>#<c:out value="${pedido.id}"/></td>
                    <td>ID <c:out value="${pedido.usuarioId}"/></td>
                    <td><fmt:formatDate value="${pedido.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                    <td>S/ <fmt:formatNumber value="${pedido.total}" pattern="#,##0.00"/></td>
                    <td><span class="badge badge-${pedido.estado.toLowerCase()}"><c:out value="${pedido.estado}"/></span></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
