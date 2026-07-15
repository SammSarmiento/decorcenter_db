<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="tituloPagina" value="${producto.nombre} — DecorCenter" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="contenedor">
    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:40px; align-items:start;">
        <div class="imagen" style="height:380px; border-radius:14px;">
            <c:choose>
                <c:when test="${not empty producto.imagenUrl}">
                    <img src="${pageContext.request.contextPath}/${producto.imagenUrl}" style="height:100%;width:100%;object-fit:cover;border-radius:14px;">
                </c:when>
                <c:otherwise>Sin imagen</c:otherwise>
            </c:choose>
        </div>

        <div>
            <span class="categoria"><c:out value="${producto.categoriaNombre}"/></span>
            <h1 class="titulo-serif"><c:out value="${producto.nombre}"/></h1>
            <p style="color:var(--gris-suave); line-height:1.6;"><c:out value="${producto.descripcion}"/></p>
            <p class="precio" style="font-size:28px;">S/ <fmt:formatNumber value="${producto.precio}" pattern="#,##0.00"/></p>
            <p class="stock">Disponibles: <c:out value="${producto.stock}"/> unidades</p>

            <c:choose>
                <c:when test="${sessionScope.usuario != null && !sessionScope.usuario.admin}">
                    <form action="${pageContext.request.contextPath}/carrito/agregar" method="post" style="margin-top:20px; display:flex; gap:12px; align-items:center;">
                        <input type="hidden" name="productoId" value="${producto.id}">
                        <div class="form-grupo" style="max-width:100px; margin-bottom:0;">
                            <input type="number" name="cantidad" value="1" min="1" max="${producto.stock}">
                        </div>
                        <button type="submit" class="btn btn-acento">Agregar al carrito</button>
                    </form>
                </c:when>
                <c:when test="${sessionScope.usuario == null}">
                    <a class="btn btn-primario" style="margin-top:20px;" href="${pageContext.request.contextPath}/login">Inicia sesión para comprar</a>
                </c:when>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
