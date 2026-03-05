<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    Integer codigo = (Integer) session.getAttribute("codigo");
    boolean logeado = (codigo != null && codigo > 0);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Carrito - EVERNEST</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="styles.css">
</head>

<body>
    <header>
        <div id="cabecera"></div>
    </header>

    <div class="container content">
        <h2>Carrito de compras</h2>

        <div id="productosCarrito">
            <!-- Los productos del carrito se mostrarán aquí -->
        </div>

        <div id="total" class="mt-4">
            <p><strong>Total: </strong><span id="totalAmount">0€</span></p>
        </div>

        <div class="mt-4">
            <% if (logeado) { %>
                <button class="btn btn-success" onclick="window.location.href='compra.jsp'">Finalizar Compra</button>
            <% } else { %>
                <form method="post" action="login.html">
                    <input type="hidden" name="url" value="compra.jsp">
                    <input type="hidden" name="mensaje" value="Debes iniciar sesión para finalizar la compra.">
                    <button type="submit" class="btn btn-success" onclick="verificarSesion()">Finalizar Compra</button>
                </form>
                
            <% } %>
        </div>
    </div>

    <footer class="footer">
        <div id="pie"></div>
    </footer>

    <script>
        function verificarSesion() {
            fetch('verificarSesion.jsp')
                .then(response => response.text())
                .then(data => {
                    if (data.trim() === "ok") {
                        window.location.href = "compra.jsp";
                    } else {
                        sessionStorage.setItem("postLoginRedirect", "compra.jsp");
                        window.location.href = "loginUsuario.jsp";
                    }
                });
        }
    </script>
    

    <script src="js/plantilla.js"></script>
    <script src="js/carrito.js"></script>
    <script>
        document.addEventListener("DOMContentLoaded", mostrarCarrito);
    </script>

</body>
</html>
