<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
    Integer codigoUsuario = (Integer) session.getAttribute("codigo");
    if (codigoUsuario == null || codigoUsuario <= 0) {
        response.sendRedirect("loginUsuario.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Confirmar compra - EVERNEST</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header>
        <div id="cabecera"></div>
    </header>

    <div class="container content">
        <h2>Resumen de la compra</h2>

        <div id="productosCarrito">
            <!-- Productos del carrito -->
        </div>

        <div id="total" class="mt-4">
            <p><strong>Total: </strong><span id="totalAmount">0€</span></p>
        </div>

        <div class="mt-4 d-flex gap-3">
            <button class="btn btn-success" onclick="enviar()">Formalizar pedido</button>

        </div>
    </div>

    <footer>
        <div id="pie"></div>
    </footer>

    <!-- Scripts -->
    <script src="js/plantilla.js"></script>
    <script src="js/carrito.js"></script>
    <script src="js/libjson.js"></script>

    <script>
        document.addEventListener("DOMContentLoaded", () => {
            mostrarResumenCompra(); // carga desde localStorage y muestra
        });

        function enviar() {
            const datos = localStorage.getItem("carrito");
            const carritoLocal = datos ? JSON.parse(datos) : [];
            console.log("Contenido del carrito:", carritoLocal); // depuración
            EnviarCarrito("ProcesarPedido.html", carritoLocal);
        }
    </script>
</body>
</html>
