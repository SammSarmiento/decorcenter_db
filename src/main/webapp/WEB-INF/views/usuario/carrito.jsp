<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="tituloPagina" value="Mi carrito — DecorCenter" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="carrito-flotante">
    <div class="carrito-header">
        <h2>Mi carrito</h2>
        <a class="cerrar" href="${pageContext.request.contextPath}/home" aria-label="Cerrar">&times;</a>
    </div>

    <div class="carrito-body">
        <c:if test="${not empty error}">
            <div class="alerta alerta-error"><c:out value="${error}"/></div>
        </c:if>

        <c:choose>
            <c:when test="${empty items}">
                <p class="vacio">No hay productos en tu carrito de compras.</p>
            </c:when>
            <c:otherwise>
                <table class="tabla-carrito">
                    <thead>
                    <tr>
                        <th>Producto</th>
                        <th>Precio</th>
                        <th>Cant.</th>
                        <th>Subtotal</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${items}">
                        <tr>
                            <td><c:out value="${item.productoNombre}"/></td>
                            <td>S/ <fmt:formatNumber value="${item.precioUnitario}" pattern="#,##0.00"/></td>
                            <td><c:out value="${item.cantidad}"/></td>
                            <td>S/ <fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></td>
                            <td>
                                <a class="acciones-tabla eliminar" href="${pageContext.request.contextPath}/carrito/eliminar?productoId=${item.productoId}">Quitar</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <div class="resumen-carrito">
                    <span class="total">Total: S/ <fmt:formatNumber value="${total}" pattern="#,##0.00"/></span>
                    <a class="btn btn-acento" href="${pageContext.request.contextPath}/carrito/confirmar">Confirmar pedido</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
