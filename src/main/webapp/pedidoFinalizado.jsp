<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Integer codigoPedido = (Integer) request.getAttribute("codigoPedido");
    if (codigoPedido == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Pedido Finalizado - EVERNEST</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<header>
    <div id="cabecera"></div>
</header>

<div class="container content text-center" style="margin-top: 150px;">
    <div class="alert alert-success p-4 shadow">
        <h2 class="mb-3">¡Gracias por tu compra!</h2>
        <p class="fs-5">Tu pedido ha sido tramitado con éxito.</p>
        <p class="fs-5"><strong>Código del pedido:</strong> <span class="text-primary"><%= codigoPedido %></span></p>
    </div>

    <a href="productos.jsp" class="btn btn-outline-primary mt-4">Volver a productos</a>
</div>

<footer class="mt-5">
    <div id="pie"></div>
</footer>

<script>
    // Limpia el carrito del navegador al finalizar el pedido
    localStorage.removeItem("carrito");
</script>

<script src="js/plantilla.js"></script>
</body>
</html>
