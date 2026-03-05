<%@page language="java" contentType="text/html;charset=UTF-8" import="tienda.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Iniciar sesión - EVERNEST</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header>
        <div id="cabecera"></div>
    </header>

    <div class="container content">
        <h2>Iniciar sesión</h2>

        <%-- Mostrar mensaje de error si existe --%>
        <%
            String mensaje = (String) session.getAttribute("mensaje");
            if (mensaje != null) {
        %>
            <div class="alert alert-danger" role="alert">
                <%= mensaje %>
            </div>
        <%
                session.removeAttribute("mensaje");
            }
        %>

        <%-- Mostrar formulario si el usuario no ha iniciado sesión --%>
        <%
            if (session.getAttribute("codigo") == null || (Integer) session.getAttribute("codigo") <= 0) {
        %>
        <form id="loginForm" method="post" action="login.html">
            <input type="hidden" name="url" value="loginUsuario.jsp">
            <input type="hidden" name="carritoLleno" id="carritoLleno" value="false">

            <div class="mb-3">
                <label for="username" class="form-label">Nombre de usuario</label>
                <input type="text" class="form-control" id="username" name="usuario">
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Contraseña</label>
                <input type="password" class="form-control" id="password" name="clave">
            </div>

            <div class="mb-3">
                <input type="radio" name="tipoAcceso" value="Acceso" checked> Acceso
                <input type="radio" name="tipoAcceso" value="Registro"> Registro
            </div>

            <button type="submit" class="btn btn-primary">Entrar</button>
        </form>

        <%
            } else {
                response.sendRedirect("compra.jsp");
            }
        %>
    </div>

    <footer>
        <div id="pie"></div>
    </footer>

    <script>
        // Redirección si se selecciona "Registro"
        document.getElementById("loginForm").addEventListener("submit", function(event) {
    const tipoAcceso = document.querySelector('input[name="tipoAcceso"]:checked').value;
    if (tipoAcceso === "Registro") {
        event.preventDefault();
        // Redirigimos sin validar campos
        window.location.href = "registro.jsp";
    } else {
        // Solo si es acceso validamos el carrito
        const datos = localStorage.getItem("carrito");
        const carrito = datos ? JSON.parse(datos) : [];
        document.getElementById("carritoLleno").value = carrito.length > 0;
    }
});

    </script>

    <script>
        if (sessionStorage.getItem("postLoginRedirect")) {
            const destino = sessionStorage.getItem("postLoginRedirect");
            sessionStorage.removeItem("postLoginRedirect");
            window.location.href = destino;
        }
    </script>

    <script src="js/plantilla.js"></script>
</body>
</html>
