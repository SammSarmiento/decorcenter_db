<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="tituloPagina" value="Pedido confirmado — DecorCenter" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="contenedor">
    <div class="form-card" style="text-align:center;">
        <h2 class="titulo-serif" style="color:#1c5934;">¡Pedido confirmado! 🎉</h2>
        <p>Gracias por tu compra. Tu pedido <strong>#<c:out value="${pedido.id}"/></strong> ha sido registrado.</p>
        <p class="total" style="font-size:24px;">Total: S/ <fmt:formatNumber value="${pedido.total}" pattern="#,##0.00"/></p>
        <a class="btn btn-primario" href="${pageContext.request.contextPath}/home">Seguir comprando</a>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
