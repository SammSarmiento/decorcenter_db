<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<section class="hero-carousel" id="heroCarousel">
    <div class="hc-track">
        <div class="hc-slide active" style="background-image:url('https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?q=80&w=2000&auto=format&fit=crop')">
            <div class="hc-content">
                <h2>Renueva tus espacios con<br>PORCELANOSA en Decor Center.</h2>
                <a href="${pageContext.request.contextPath}/home?categoria=1" class="btn-primary">Descubre más →</a>
            </div>
        </div>
        <div class="hc-slide" style="background-image:url('https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?q=80&w=2000&auto=format&fit=crop')">
            <div class="hc-content">
                <h2>Pisos que transforman<br>cualquier ambiente.</h2>
                <a href="${pageContext.request.contextPath}/home?categoria=1" class="btn-primary">Ver pisos →</a>
            </div>
        </div>
        <div class="hc-slide" style="background-image:url('https://images.unsplash.com/photo-1620626011761-996317b8d101?q=80&w=2000&auto=format&fit=crop')">
            <div class="hc-content">
                <h2>Baños de spa,<br>directo a tu casa.</h2>
                <a href="${pageContext.request.contextPath}/home?categoria=3" class="btn-primary">Ver baños →</a>
            </div>
        </div>
    </div>
    <button class="hc-arrow hc-prev" onclick="hcMove(-1)" aria-label="Anterior">‹</button>
    <button class="hc-arrow hc-next" onclick="hcMove(1)" aria-label="Siguiente">›</button>
    <div class="hc-dots" id="hcDots"></div>
</section>

<section class="whatsapp-help">
    <p>¿Tienes consultas? Uno de nuestros asesores digitales te ayudará 📱 991 682 946</p>
</section>

<!-- ===================== NUESTRO CATÁLOGO ===================== -->
<section class="catalog">
    <h3>Nuestro catálogo</h3>
    <div class="carrusel-wrap">
        <button class="cat-arrow prev" onclick="scrollTrack('catalogTrack', -1)" aria-label="Anterior">‹</button>
        <div class="carrusel-track" id="catalogTrack">
            <c:forEach var="producto" items="${productos}" end="9">
                <div class="product-card">
                    <c:choose>
                        <c:when test="${not empty producto.imagenUrl}">
                            <img src="${pageContext.request.contextPath}/${producto.imagenUrl}" alt="${producto.nombre}"
                                 onerror="this.src='https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=500&h=380&fit=crop&q=80'">
                        </c:when>
                        <c:otherwise>
                            <img src="https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=500&h=380&fit=crop&q=80" alt="Sin imagen">
                        </c:otherwise>
                    </c:choose>
                    <h4>${producto.nombre}</h4>
                    <p class="stock">Stock: ${producto.stock} unid.</p>
                    <p class="price">S/ <fmt:formatNumber value="${producto.precio}" pattern="#,##0.00"/></p>
                    <a href="${pageContext.request.contextPath}/producto?id=${producto.id}" class="btn-secondary">Ver producto</a>
                </div>
            </c:forEach>
        </div>
        <button class="cat-arrow next" onclick="scrollTrack('catalogTrack', 1)" aria-label="Siguiente">›</button>
    </div>
</section>

<!-- ===================== FAVORITOS (usando imagen_favorito) ===================== -->
<section class="bestsellers">
    <h3>Los favoritos de nuestros clientes</h3>
    <div class="carrusel-wrap">
        <button class="cat-arrow prev" onclick="scrollTrack('bsTrack', -1)" aria-label="Anterior">‹</button>
        <div class="carrusel-track" id="bsTrack">
            <c:forEach var="producto" items="${favoritos}" varStatus="st" end="4">
                
                <c:set var="descuento" value="${st.index % 4 eq 0 ? 31 : (st.index % 4 eq 1 ? 45 : (st.index % 4 eq 2 ? 20 : 0))}" />
                
                <div class="bs-card">
                    <span class="bs-rank">#${st.index + 1}</span>
                    <span class="bs-tag">🔥 Más vendido</span>

                    <!-- ✅ USA imagen_favorito si existe, si no usa imagenUrl -->
                    <c:choose>
                        <c:when test="${not empty producto.imagenFavorito}">
                            <img src="${pageContext.request.contextPath}/${producto.imagenFavorito}" 
                                 alt="${producto.nombre}">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/${producto.imagenUrl}" 
                                 alt="${producto.nombre}"
                                 onerror="this.src='https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=500&h=380&fit=crop&q=80'">
                        </c:otherwise>
                    </c:choose>

                    <h4>${producto.nombre}</h4>

                    <!-- Precio con descuento -->
                    <c:choose>
                        <c:when test="${descuento gt 0}">
                            <p class="precio-antes">S/ <fmt:formatNumber value="${producto.precio / (1 - descuento/100.0)}" pattern="#,##0.00"/></p>
                            <p class="price">S/ <fmt:formatNumber value="${producto.precio}" pattern="#,##0.00"/>
                                <span class="descuento-pill">-${descuento}%</span>
                            </p>
                        </c:when>
                        <c:otherwise>
                            <p class="price">S/ <fmt:formatNumber value="${producto.precio}" pattern="#,##0.00"/></p>
                        </c:otherwise>
                    </c:choose>

                    <a href="${pageContext.request.contextPath}/producto?id=${producto.id}" class="btn-secondary">Ver producto</a>
                </div>
            </c:forEach>
        </div>
        <button class="cat-arrow next" onclick="scrollTrack('bsTrack', 1)" aria-label="Siguiente">›</button>
    </div>
</section>

<script>
    (function () {
        const slides = document.querySelectorAll('#heroCarousel .hc-slide');
        const dotsBox = document.getElementById('hcDots');
        let current = 0, timer;
        slides.forEach((_, i) => {
            const dot = document.createElement('span');
            if (i === 0)
                dot.classList.add('active');
            dot.addEventListener('click', () => goTo(i));
            dotsBox.appendChild(dot);
        });
        const dots = dotsBox.querySelectorAll('span');
        function render() {
            slides.forEach((s, i) => s.classList.toggle('active', i === current));
            dots.forEach((d, i) => d.classList.toggle('active', i === current));
        }
        function goTo(i) {
            current = (i + slides.length) % slides.length;
            render();
            resetAutoplay();
        }
        window.hcMove = (dir) => goTo(current + dir);
        function resetAutoplay() {
            clearInterval(timer);
            timer = setInterval(() => goTo(current + 1), 6000);
        }
        resetAutoplay();
    })();

    function scrollTrack(id, dir) {
        const track = document.getElementById(id);
        if (!track) return;
        const card = track.querySelector('.product-card, .bs-card');
        const step = card ? card.offsetWidth + 24 : 320;
        track.scrollBy({left: dir * step, behavior: 'smooth'});
    }
</script>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />