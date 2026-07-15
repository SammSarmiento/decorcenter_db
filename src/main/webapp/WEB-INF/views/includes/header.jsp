<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${not empty tituloPagina ? tituloPagina : 'DecorCenter'}"/></title>

    <!-- CSS INCRUSTADO A PRUEBA DE BLOQUEOS -->
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');

        :root {
            --dark: #1a1a1a;
            --bg-light: #ffffff;
            --bg-soft: #f4f3f1;
            --accent: #E31C23;      /* rojo del logo / ofertas */
            --accent-dark: #1c1c1c;
            --text-muted: #6b6b6b;
            --border: #eaeaea;
            --success: #1c7c3f;
            --radius: 6px;
        }

        * { margin: 0; padding: 0; box-sizing: border-box; }
        html { scroll-behavior: smooth; }
        body { background-color: var(--bg-light); color: var(--dark); font-family: 'Poppins', sans-serif; font-weight: 400; line-height: 1.5; }
        a { color: inherit; }
        img { max-width: 100%; display: block; }
        ul { list-style: none; }

        /* ================= Header ================= */
        header {
            display: flex; justify-content: space-between; align-items: center;
            padding: 18px 60px; background: var(--bg-light); border-bottom: 1px solid var(--border);
            gap: 30px; flex-wrap: wrap;
        }
        .logo a { text-decoration: none; color: var(--dark); }
        .logo h1 {
            font-size: 24px; font-weight: 700; letter-spacing: 0.5px; line-height: 1.05;
            text-transform: uppercase;
        }
        .logo h1 .accent-o { color: var(--accent); }
        .logo h1 .sub { display: block; font-weight: 500; font-size: 12px; letter-spacing: 3px; color: var(--dark); }

        .search-bar { flex: 1; max-width: 420px; }
        .search-bar form {
            display: flex; align-items: center; background: var(--bg-soft);
            border-radius: 30px; padding: 6px 8px 6px 20px; border: 1px solid var(--border);
        }
        .search-bar input { border: none; background: transparent; outline: none; width: 100%; padding: 8px 4px; font-family: 'Poppins', sans-serif; font-size: 14px; color: var(--dark); }
        .search-bar input::placeholder { color: #999; }
        .search-bar button { background: var(--dark); border: none; cursor: pointer; color: #fff; width: 34px; height: 34px; border-radius: 50%; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
        .search-bar button svg { width: 15px; height: 15px; }

        .user-actions { display: flex; align-items: center; gap: 18px; }
        .user-actions a {
            display: flex; align-items: center; gap: 8px; text-decoration: none; color: var(--dark);
            font-weight: 500; font-size: 13px; letter-spacing: 0.3px;
        }
        .user-actions a svg { width: 22px; height: 22px; stroke: var(--dark); flex-shrink: 0; }
        .user-actions a.texto-solo { font-weight: 600; }
        .user-actions .icon-label { display: none; }

        /* ================= Navegación ================= */
        .main-nav { background: var(--bg-soft); border-bottom: 1px solid var(--border); }
        .main-nav ul { display: flex; justify-content: center; gap: 45px; flex-wrap: wrap; padding: 15px 20px; }
        .main-nav a { text-decoration: none; color: var(--dark); font-size: 13.5px; font-weight: 500; letter-spacing: 0.3px; transition: color 0.25s; }
        .main-nav a:hover { color: var(--accent); }

        main { display: block; }

        /* ================= Hero Banner (banner simple, por si aún lo usas en alguna vista) ================= */
        .hero-banner {
            background: linear-gradient(rgba(20,20,20,0.55), rgba(20,20,20,0.35)), url('https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?q=80&w=2000&auto=format&fit=crop') center/cover;
            min-height: 46vh; display: flex; align-items: center; padding: 0 60px; color: #fff;
        }
        .banner-content h2 { font-size: 46px; font-weight: 600; line-height: 1.15; margin-bottom: 15px; }
        .banner-content p { font-weight: 300; letter-spacing: 0.3px; margin-bottom: 28px; opacity: 0.92; max-width: 480px; }
        .btn-primary {
            background: var(--dark); color: #fff; padding: 13px 32px; text-decoration: none;
            font-size: 13px; font-weight: 600; text-transform: uppercase; letter-spacing: 1.5px;
            border-radius: 30px; display: inline-block; border: none; cursor: pointer; transition: 0.25s;
        }
        .btn-primary:hover { background: var(--accent); }

        .whatsapp-help { text-align: center; padding: 14px; background: var(--dark); color: #fff; font-size: 13.5px; letter-spacing: 0.4px; }

        /* ================= Carrusel del inicio ================= */
        .hero-carousel { position: relative; overflow: hidden; height: 60vh; min-height: 420px; }
        .hc-track { width: 100%; height: 100%; position: relative; }
        .hc-slide {
            position: absolute; inset: 0; opacity: 0; visibility: hidden;
            background-size: cover; background-position: center;
            display: flex; align-items: center; padding: 0 60px;
            transition: opacity 0.7s ease;
        }
        .hc-slide::before {
            content: ""; position: absolute; inset: 0;
            background: linear-gradient(90deg, rgba(0,0,0,0.55) 0%, rgba(0,0,0,0.15) 55%, transparent 100%);
        }
        .hc-slide.active { opacity: 1; visibility: visible; }
        .hc-content { position: relative; z-index: 2; color: #fff; max-width: 560px; }
        .hc-content h2 { font-family: 'Poppins', sans-serif; font-weight: 600; font-size: 40px; line-height: 1.2; margin-bottom: 24px; }
        .hc-content .btn-primary { position: relative; z-index: 2; }

        .hc-arrow {
            position: absolute; top: 50%; transform: translateY(-50%);
            width: 42px; height: 42px; border-radius: 50%; border: none;
            background: rgba(255,255,255,0.85); color: var(--dark); font-size: 22px;
            cursor: pointer; z-index: 3; display: flex; align-items: center; justify-content: center;
            transition: background 0.2s;
        }
        .hc-arrow:hover { background: #fff; }
        .hc-prev { left: 20px; }
        .hc-next { right: 20px; }

        .hc-dots { position: absolute; bottom: 18px; left: 50%; transform: translateX(-50%); display: flex; gap: 8px; z-index: 3; }
        .hc-dots span { width: 9px; height: 9px; border-radius: 50%; background: rgba(255,255,255,0.6); cursor: pointer; transition: background 0.2s; }
        .hc-dots span.active { background: var(--accent); }

        @media (max-width: 700px) {
            .hero-carousel { height: 50vh; }
            .hc-content h2 { font-size: 26px; }
        }

        /* ================= Catálogo / grilla de productos ================= */
        .catalog { padding: 60px 60px 80px; text-align: center; }
        .catalog h3 { font-size: 30px; font-weight: 600; margin-bottom: 40px; color: var(--dark); }
        .product-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(230px, 1fr)); gap: 24px; }

        .product-card {
            background: #fff; border: 1px solid var(--border); border-radius: var(--radius);
            overflow: hidden; text-align: left; position: relative;
            transition: transform 0.25s ease, box-shadow 0.25s ease;
        }
        .product-card:hover { transform: translateY(-6px); box-shadow: 0 14px 30px rgba(0,0,0,0.08); }
        .product-card img { width: 100%; height: 190px; object-fit: cover; }
        .product-card h4 { font-size: 14.5px; font-weight: 500; padding: 14px 14px 0; min-height: 40px; }
        .price, .precio { font-size: 19px; font-weight: 700; color: var(--dark); margin: 8px 0; }
        .product-card .price, .product-card .precio { padding: 6px 14px 0; margin: 0; }
        .stock { font-size: 12px; color: var(--text-muted); margin: 4px 0 16px; }
        .product-card .stock { padding: 4px 14px 0; margin: 0; }
        .product-card .btn-secondary { margin: 14px; }
        .badge-oferta {
            position: absolute; top: 12px; left: 12px; background: var(--dark); color: #fff;
            font-size: 11px; font-weight: 700; padding: 5px 10px; border-radius: 20px; letter-spacing: 0.3px;
        }
        .precio-antes { text-decoration: line-through; color: #aaa; font-size: 13px; padding: 0 14px; }

        .btn-secondary {
            display: inline-block; border: 1px solid var(--dark); padding: 9px 22px; text-decoration: none;
            color: var(--dark); font-size: 12px; font-weight: 600; text-transform: uppercase; letter-spacing: 0.8px;
            border-radius: 30px; transition: 0.25s;
        }
        .btn-secondary:hover { background: var(--dark); color: #fff; }

        /* ================= Carrusel horizontal reutilizable (catálogo y más vendidos) ================= */
        .carrusel-wrap { position: relative; padding: 0 6px; }
        .carrusel-track {
            display: flex; gap: 24px; overflow-x: auto; scroll-behavior: smooth;
            scroll-snap-type: x mandatory; padding: 4px 4px 14px; scrollbar-width: thin;
        }
        .carrusel-track::-webkit-scrollbar { height: 6px; }
        .carrusel-track::-webkit-scrollbar-thumb { background: #ccc; border-radius: 10px; }
        .carrusel-track .product-card,
        .carrusel-track .bs-card { flex: 0 0 240px; scroll-snap-align: start; }

        .cat-arrow {
            position: absolute; top: 42%; transform: translateY(-50%);
            width: 42px; height: 42px; border-radius: 50%; border: none;
            background: #fff; box-shadow: 0 4px 14px rgba(0,0,0,0.15); color: var(--dark);
            font-size: 22px; cursor: pointer; z-index: 3; display: flex; align-items: center;
            justify-content: center; transition: background 0.2s, transform 0.2s;
        }
        .cat-arrow:hover { background: var(--bg-soft); }
        .cat-arrow.prev { left: -18px; }
        .cat-arrow.next { right: -18px; }
        @media (max-width: 700px) { .cat-arrow { display: none; } }

        .descuento-pill {
            display: inline-block; background: var(--accent); color: #fff; font-size: 10.5px;
            font-weight: 700; padding: 2px 8px; border-radius: 10px; margin-left: 8px; vertical-align: middle;
        }

        /* ================= Más vendidos ================= */
        .bestsellers { padding: 50px 60px 70px; background: var(--bg-soft); }
        .bestsellers h3 { text-align: center; font-size: 28px; font-weight: 600; color: var(--dark); margin-bottom: 35px; }
        .bs-track { display: flex; gap: 22px; overflow-x: auto; padding-bottom: 10px; scroll-snap-type: x mandatory; }
        .bs-track::-webkit-scrollbar { height: 6px; }
        .bs-track::-webkit-scrollbar-thumb { background: #ccc; border-radius: 10px; }

        .bs-card {
            position: relative; flex: 0 0 240px; background: #fff; border: 1px solid var(--border);
            border-radius: 10px; overflow: hidden; scroll-snap-align: start; transition: transform 0.25s;
        }
        .bs-card:hover { transform: translateY(-6px); box-shadow: 0 10px 24px rgba(0,0,0,0.08); }
        .bs-card img { width: 100%; height: 170px; object-fit: cover; }
        .bs-card h4 { font-size: 14.5px; font-weight: 500; padding: 12px 14px 0; min-height: 40px; }
        .bs-card .price { font-size: 18px; padding: 6px 14px 0; margin: 0 0 12px; }
        .bs-card .btn-secondary { margin: 0 14px 16px; padding: 8px 18px; font-size: 12px; }

        .bs-rank {
            position: absolute; top: 10px; left: 10px; background: var(--dark); color: #fff;
            font-size: 12px; font-weight: 700; padding: 4px 9px; border-radius: 20px; z-index: 2;
        }
        .bs-tag {
            position: absolute; top: 10px; right: 10px; background: var(--accent); color: #fff;
            font-size: 10.5px; font-weight: 700; padding: 4px 9px; border-radius: 20px; z-index: 2;
        }

        /* ================= Botones genéricos usados en otras vistas ================= */
        .btn {
            display: inline-block; padding: 12px 28px; border-radius: 30px; text-decoration: none;
            font-size: 13.5px; font-weight: 600; letter-spacing: 0.4px; border: none; cursor: pointer; transition: 0.25s;
        }
        .btn-primario { background: var(--dark); color: #fff; }
        .btn-primario:hover { background: var(--accent); }
        .btn-acento { background: var(--accent); color: #fff; }
        .btn-acento:hover { background: #c11017; }
        .btn-block { width: 100%; }

        /* ================= Contenedor genérico de páginas ================= */
        .contenedor { max-width: 1100px; margin: 0 auto; padding: 50px 30px; }
        .titulo-serif { font-size: 28px; font-weight: 600; margin-bottom: 20px; color: var(--dark); }
        .categoria {
            display: inline-block; background: var(--bg-soft); color: var(--text-muted);
            font-size: 11.5px; text-transform: uppercase; letter-spacing: 1px; padding: 5px 12px;
            border-radius: 20px; margin-bottom: 14px;
        }

        .alerta { padding: 14px 18px; border-radius: var(--radius); margin-bottom: 22px; font-size: 13.5px; }
        .alerta-error { background: #fdecec; color: #b3261e; border: 1px solid #f6c6c6; }

        /* ================= Formularios (Login, Registro y Admin) ================= */
        .auth-container, .form-card {
            max-width: 440px; margin: 60px auto; background: #fff;
            padding: 45px 40px; box-shadow: 0 10px 34px rgba(0,0,0,0.06); border-radius: 10px;
            text-align: center;
        }
        .auth-container h2, .form-card h2, .admin-container h2 {
            font-size: 26px; font-weight: 600; margin-bottom: 26px; color: var(--dark);
        }
        .form-grupo { margin-bottom: 20px; text-align: left; }
        .form-grupo label { display: block; font-size: 12px; font-weight: 600; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.6px; margin-bottom: 7px; }
        .form-grupo input, .form-grupo select, .form-grupo textarea {
            width: 100%; padding: 13px 14px; border: 1px solid var(--border);
            background: var(--bg-soft); font-family: 'Poppins', sans-serif; font-size: 14px;
            outline: none; transition: 0.25s; border-radius: var(--radius);
        }
        .form-grupo input:focus, .form-grupo select:focus, .form-grupo textarea:focus { border-color: var(--dark); background: #fff; }
        .form-grupo textarea { min-height: 90px; resize: vertical; }
        .auth-links, .enlace-secundario { margin-top: 22px; font-size: 13px; color: var(--text-muted); }
        .auth-links a, .enlace-secundario a { color: var(--accent); font-weight: 600; text-decoration: none; }

        .admin-container {
            max-width: 800px; margin: 45px auto; background: #fff; border-radius: 10px;
            padding: 40px; box-shadow: 0 10px 30px rgba(0,0,0,0.05);
        }

        /* ================= Panel de administración ================= */
        .panel-admin { display: flex; min-height: 70vh; }
        .sidebar-admin {
            width: 230px; flex-shrink: 0; background: var(--dark); color: #fff;
            display: flex; flex-direction: column; padding: 30px 0;
        }
        .sidebar-admin a {
            color: #cfcfcf; text-decoration: none; padding: 13px 28px; font-size: 14px; font-weight: 500;
            border-left: 3px solid transparent; transition: 0.2s;
        }
        .sidebar-admin a:hover { color: #fff; background: rgba(255,255,255,0.05); }
        .sidebar-admin a.activo { color: #fff; border-left-color: var(--accent); background: rgba(255,255,255,0.07); }

        .contenido-admin { flex: 1; padding: 40px 45px; background: var(--bg-soft); }
        .seccion-titulo { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; flex-wrap: wrap; gap: 12px; }
        .seccion-titulo h2 { font-size: 26px; font-weight: 600; }

        .tarjetas-resumen { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 20px; margin-bottom: 40px; }
        .tarjeta-stat { background: #fff; border: 1px solid var(--border); border-radius: 10px; padding: 24px; text-align: center; }
        .tarjeta-stat .valor { font-size: 32px; font-weight: 700; color: var(--dark); }
        .tarjeta-stat .etiqueta { font-size: 12.5px; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.5px; margin-top: 6px; }

        .tabla-admin, .tabla-carrito {
            width: 100%; border-collapse: collapse; background: #fff; border-radius: 10px;
            overflow: hidden; box-shadow: 0 4px 16px rgba(0,0,0,0.04);
        }
        .tabla-admin th, .tabla-carrito th {
            text-align: left; font-size: 12px; text-transform: uppercase; letter-spacing: 0.5px;
            color: var(--text-muted); background: var(--bg-soft); padding: 14px 16px; border-bottom: 1px solid var(--border);
        }
        .tabla-admin td, .tabla-carrito td { padding: 14px 16px; border-bottom: 1px solid var(--border); font-size: 14px; }
        .tabla-admin tr:last-child td, .tabla-carrito tr:last-child td { border-bottom: none; }

        .acciones-tabla a { text-decoration: none; color: var(--dark); font-weight: 600; font-size: 13px; margin-right: 14px; }
        .acciones-tabla a.eliminar { color: var(--accent); }

        .badge { display: inline-block; padding: 5px 12px; border-radius: 20px; font-size: 11.5px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.4px; }
        .badge-pendiente { background: #fff3cd; color: #92700c; }
        .badge-confirmado { background: #d9edff; color: #0b5aa8; }
        .badge-entregado { background: #dcf5e3; color: var(--success); }
        .badge-cancelado { background: #fdecec; color: #b3261e; }

        /* ================= Carrito ================= */
        .resumen-carrito { display: flex; justify-content: flex-end; align-items: center; gap: 22px; margin-top: 26px; }
        .resumen-carrito .total { font-size: 20px; font-weight: 700; }

        .carrito-flotante {
            max-width: 460px; margin: 60px auto; background: #fff; border-radius: 12px;
            box-shadow: 0 20px 50px rgba(0,0,0,0.14); overflow: hidden;
        }
        .carrito-flotante .carrito-header {
            display: flex; justify-content: space-between; align-items: center;
            padding: 22px 26px; border-bottom: 1px solid var(--border);
        }
        .carrito-flotante .carrito-header h2 { font-size: 20px; font-weight: 700; }
        .carrito-flotante .cerrar { text-decoration: none; color: var(--dark); font-size: 20px; line-height: 1; }
        .carrito-flotante .carrito-body { padding: 26px; }
        .carrito-flotante .vacio { color: var(--text-muted); font-size: 14px; text-align: center; padding: 30px 0; }
        .carrito-flotante .vacio a { color: var(--accent); font-weight: 600; text-decoration: none; }

        /* ================= Footer ================= */
        .footer { background: var(--bg-soft); padding: 55px 60px 30px; border-top: 1px solid var(--border); }
        .footer-top { display: grid; grid-template-columns: 1.2fr 1fr 1fr 1fr; gap: 40px; }
        .footer-brand .logo h1 { font-size: 22px; }
        .footer-brand p { font-size: 13px; color: var(--text-muted); margin-top: 12px; max-width: 220px; }
        .footer-col h4 { font-size: 13px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.6px; margin-bottom: 18px; }
        .footer-col ul li { margin-bottom: 12px; }
        .footer-col a { font-size: 13.5px; color: var(--text-muted); text-decoration: none; }
        .footer-col a:hover { color: var(--dark); }
        .footer-social { display: flex; gap: 12px; margin-bottom: 22px; }
        .footer-social a {
            width: 34px; height: 34px; border-radius: 50%; background: var(--dark); color: #fff;
            display: flex; align-items: center; justify-content: center; text-decoration: none;
        }
        .footer-social a svg { width: 16px; height: 16px; fill: #fff; }
        .footer-payments { display: flex; gap: 8px; flex-wrap: wrap; }
        .footer-payments span {
            border: 1px solid var(--border); background: #fff; border-radius: 4px; padding: 5px 9px;
            font-size: 11px; font-weight: 700; color: var(--text-muted); letter-spacing: 0.3px;
        }
        .footer-bottom { text-align: center; font-size: 12.5px; color: var(--text-muted); margin-top: 40px; padding-top: 22px; border-top: 1px solid var(--border); }

        .whatsapp-float {
            position: fixed; right: 26px; bottom: 26px; width: 58px; height: 58px; border-radius: 50%;
            background: #25D366; display: flex; align-items: center; justify-content: center;
            box-shadow: 0 8px 22px rgba(0,0,0,0.25); text-decoration: none; z-index: 999; transition: transform 0.2s;
        }
        .whatsapp-float:hover { transform: scale(1.06); }
        .whatsapp-float svg { width: 30px; height: 30px; fill: #fff; }

        /* ================= Responsive ================= */
        @media (max-width: 900px) {
            header { padding: 16px 24px; }
            .search-bar { order: 3; max-width: 100%; flex-basis: 100%; }
            .main-nav ul { gap: 22px; padding: 12px; }
            .hero-banner { padding: 0 24px; }
            .catalog { padding: 40px 24px 60px; }
            .bestsellers { padding: 40px 24px 60px; }
            .panel-admin { flex-direction: column; }
            .sidebar-admin { width: 100%; flex-direction: row; flex-wrap: wrap; padding: 12px; }
            .footer { padding: 40px 24px 24px; }
            .footer-top { grid-template-columns: 1fr 1fr; }
        }
        @media (max-width: 520px) {
            .user-actions a span.icon-label { display: none; }
            .footer-top { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>
    <header>
        <div class="logo">
            <a href="${pageContext.request.contextPath}/home">
                <h1>DEC<span class="accent-o">O</span>R<span class="sub">CENTER</span></h1>
            </a>
        </div>

        <div class="search-bar">
            <form action="${pageContext.request.contextPath}/home" method="get">
                <input type="text" name="buscar" placeholder="¿Qué estás buscando?">
                <button type="submit" aria-label="Buscar">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
                </button>
            </form>
        </div>

        <div class="user-actions">
            <c:choose>
                <c:when test="${usuario != null && usuario.admin}">
                    <a href="${pageContext.request.contextPath}/admin/dashboard"><span class="icon-label">Panel Admin</span>⚙️</a>
                    <a href="${pageContext.request.contextPath}/logout"><span class="icon-label">Salir</span>🚪</a>
                </c:when>
                <c:when test="${usuario != null}">
                    <a href="${pageContext.request.contextPath}/usuario/carrito" aria-label="Mi carrito">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4Z"></path><path d="M3 6h18"></path><path d="M16 10a4 4 0 0 1-8 0"></path></svg>
                    </a>
                    <a href="${pageContext.request.contextPath}/logout"><span class="icon-label">Salir</span>🚪</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" aria-label="Iniciar sesión">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>
                    </a>
                    <a href="${pageContext.request.contextPath}/usuario/carrito" aria-label="Mi carrito">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4Z"></path><path d="M3 6h18"></path><path d="M16 10a4 4 0 0 1-8 0"></path></svg>
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </header>

    <nav class="main-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/home?categoria=1">Pisos y Paredes</a></li>
            <li><a href="${pageContext.request.contextPath}/home?categoria=2">Tableros</a></li>
            <li><a href="${pageContext.request.contextPath}/home?categoria=3">Baño y Spa</a></li>
            <li><a href="${pageContext.request.contextPath}/home?categoria=4">Cocina y Electrohogar</a></li>
            <li><a href="${pageContext.request.contextPath}/home?ofertas=true">Ofertas</a></li>
        </ul>
    </nav>
    <main>
