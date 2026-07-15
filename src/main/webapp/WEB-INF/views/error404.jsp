<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="tituloPagina" value="Página no encontrada — DecorCenter" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="contenedor" style="text-align:center; padding:100px 20px;">
    <h1 class="titulo-serif">404</h1>
    <p>La página que buscas no existe.</p>
    <a class="btn btn-primario" href="${pageContext.request.contextPath}/home">Volver al inicio</a>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
