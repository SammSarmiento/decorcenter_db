<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<main class="auth-container">
    <h2>Bienvenido de nuevo</h2>
    
    <c:if test="${not empty param.error}">
        <p style="color: #d9534f; font-size: 13px; margin-bottom: 15px; font-weight: 600;">Credenciales incorrectas o sesión expirada.</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="form-grupo">
            <label>Correo electrónico</label>
            <input type="email" name="email" required placeholder="ejemplo@utp.edu.pe">
        </div>
        
        <div class="form-grupo">
            <label>Contraseña</label>
            <input type="password" name="password" required placeholder="••••••">
        </div>
        
        <button type="submit" class="btn-primary btn-block">Iniciar sesión</button>
    </form>

    <div class="auth-links">
        <p>¿No tienes cuenta? <a href="${pageContext.request.contextPath}/registro">Regístrate aquí</a></p>
    </div>
    
    <div style="margin-top: 30px; font-size: 11px; color: #999; line-height: 1.6;">
        Demo admin: admin@decorcenter.com / admin<br>
        Demo usuario: cliente@decorcenter.com / user123
    </div>
</main>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
