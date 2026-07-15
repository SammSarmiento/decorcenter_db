<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="tituloPagina" value="Crear cuenta — DecorCenter" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="form-card">
    <h2 class="titulo-serif">Crea tu cuenta</h2>

    <c:if test="${not empty error}">
        <div class="alerta alerta-error"><c:out value="${error}"/></div>
    </c:if>

    <form action="${pageContext.request.contextPath}/registro" method="post">
        
        <div class="form-grupo">
            <label>Nombre completo</label>
            <input type="text" name="nombre" required placeholder="Ej: Samantha López">
        </div>

        <div class="form-grupo">
            <label>Correo electrónico</label>
            <input type="email" name="email" required placeholder="ejemplo@correo.com">
        </div>


        <div class="form-grupo">
            <label>Dirección</label>
            <input type="text" name="direccion" placeholder="Av. Ejemplo 123, Lima">
        </div>

        <div class="form-grupo">
            <label>Teléfono</label>
            <input type="text" name="telefono" placeholder="999 888 777">
        </div>

        <!-- NUEVOS CAMPOS DE CONTRASEÑA -->
        <div class="form-grupo">
            <label>Contraseña</label>
            <input type="password" name="password" required placeholder="Mínimo 6 caracteres">
        </div>

        <div class="form-grupo">
            <label>Confirmar contraseña</label>
            <input type="password" name="confirmPassword" required placeholder="Repite tu contraseña">
        </div>

        <button type="submit" class="btn btn-primario" style="width:100%;">Crear cuenta</button>
    </form>

    <p class="enlace-secundario">¿Ya tienes cuenta? <a href="${pageContext.request.contextPath}/login">Inicia sesión</a></p>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
