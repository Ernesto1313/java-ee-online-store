<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mi perfil - EVERNEST</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header>
        <div id="cabecera"></div>
    </header>
    <div class="container content">
        <h2>Mi Perfil</h2>
        <h4>Información personal</h4>
        <p><strong>Nombre:</strong> ${nombre != null && !nombre.isEmpty() ? nombre : "No disponible"}</p>
        <p><strong>Apellidos:</strong> ${apellidos != null && !apellidos.isEmpty() ? apellidos : "No disponible"}</p>
        <p><strong>Dirección:</strong> ${domicilio != null && !domicilio.isEmpty() ? domicilio : "No disponible"}</p>
        <p><strong>Población:</strong> ${poblacion != null && !poblacion.isEmpty() ? poblacion : "No disponible"}</p>
        <p><strong>Provincia:</strong> ${provincia != null && !provincia.isEmpty() ? provincia : "No disponible"}</p>
        <p><strong>CP:</strong> ${cp != null && !cp.isEmpty() ? cp : "No disponible"}</p>
        <p><strong>Teléfono:</strong> ${telefono != null && !telefono.isEmpty() ? telefono : "No disponible"}</p>
        <%
            String errorMensaje = (String) session.getAttribute("errorMensaje");
            if (errorMensaje != null) {
                out.println("<div class='alert alert-danger mt-3'>" + errorMensaje + "</div>");
                session.removeAttribute("errorMensaje");
            }
            String mensaje = (String) session.getAttribute("mensaje");
            if (mensaje != null) {
                out.println("<div class='alert alert-success mt-3'>" + mensaje + "</div>");
                session.removeAttribute("mensaje");
            }
        %>
        <hr>
        <a href="editarUsuario.jsp" class="btn btn-warning mb-3">Modificarr datos</a>
        <a href="verPedidos.html" class="btn btn-primary mb-3">Ver mis pedidos</a>
        <a href="logout.html?url=loginUsuario.jsp" class="btn btn-danger mb-3">Cerrar sesión</a>
    </div>
    <footer>
        <div id="pie"></div>
    </footer>
    <script src="js/plantilla.js"></script>
</body>
</html>