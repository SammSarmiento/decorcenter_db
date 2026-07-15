<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<main class="admin-container">
    <h2>${esEdicion ? 'Editar Producto' : 'Nuevo Producto'}</h2>

    <c:if test="${not empty param.error}">
        <div class="alerta alerta-error">Error al guardar. Revisa los datos e intenta de nuevo.</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/admin/producto" method="post" enctype="multipart/form-data">
        
        <c:if test="${esEdicion}">
            <input type="hidden" name="id" value="${producto.id}">
        </c:if>

        <div class="form-grupo">
            <label>Nombre:</label>
            <input type="text" name="nombre" required value="${producto != null ? producto.nombre : ''}">
        </div>

        <div class="form-grupo">
            <label>Descripción:</label>
            <textarea name="descripcion" required rows="3">${producto != null ? producto.descripcion : ''}</textarea>
        </div>

        <div class="form-grupo">
            <label>Precio (S/):</label>
            <input type="number" step="0.01" name="precio" required value="${producto != null ? producto.precio : ''}">
        </div>

        <div class="form-grupo">
            <label>Stock:</label>
            <input type="number" name="stock" required value="${producto != null ? producto.stock : ''}">
        </div>

        <div class="form-grupo">
            <label>Categoría:</label>
            <select name="categoriaId" required>
                <option value="1" ${producto != null && producto.categoriaId == 1 ? 'selected' : ''}>Pisos y Paredes</option>
                <option value="2" ${producto != null && producto.categoriaId == 2 ? 'selected' : ''}>Tableros</option>
                <option value="3" ${producto != null && producto.categoriaId == 3 ? 'selected' : ''}>Baño y Spa</option>
                <option value="4" ${producto != null && producto.categoriaId == 4 ? 'selected' : ''}>Cocina y Electrohogar</option>
            </select>
        </div>

        <div class="form-grupo">
            <label>Imagen (.jpg, .png)${esEdicion ? ' (dejar vacío para mantener la actual)' : ''}:</label>
            <input type="file" name="imagen" accept="image/png, image/jpeg, image/webp" ${esEdicion ? '' : 'required'}>
            
            <c:if test="${esEdicion && not empty producto.imagenUrl}">
                <div style="margin-top:10px;">
                    <p style="font-size:12px;color:#666;">Imagen actual:</p>
                    <img src="${pageContext.request.contextPath}/${producto.imagenUrl}" 
                         style="max-width:160px;max-height:120px;border-radius:8px;border:1px solid #ddd;" 
                         alt="Imagen actual">
                </div>
            </c:if>
        </div>

        <button type="submit" class="btn-primary btn-block">
            ${esEdicion ? 'Actualizar Producto' : 'Guardar Producto'}
        </button>
        
        <p style="margin-top:16px;text-align:center;">
            <a href="${pageContext.request.contextPath}/admin/productos" style="color:var(--accent);">← Volver al listado</a>
        </p>
    </form>
</main>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
