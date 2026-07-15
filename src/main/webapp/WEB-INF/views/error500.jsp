<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="tituloPagina" value="Ocurrió un error — DecorCenter" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="contenedor" style="text-align:center; padding:100px 20px;">
    <h1 class="titulo-serif">Algo salió mal</h1>
    <p>Ocurrió un error inesperado. Por favor intenta nuevamente.</p>
    <a class="btn btn-primario" href="${pageContext.request.contextPath}/home">Volver al inicio</a>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
