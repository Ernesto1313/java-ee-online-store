<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Editar perfil - EVERNEST</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header>
        <div id="cabecera"></div>
    </header>
    <div class="container content">
        <h2>Editar Perfil</h2>
        <h4>Modificar información personal</h4>
        <form action="actualizarUsuario.html" method="post">
            <div class="mb-3">
                <label for="nombre" class="form-label"><strong>Nombre:</strong></label>
                <input type="text" class="form-control" id="nombre" name="nombre" value="${nombre != null && !nombre.isEmpty() ? nombre : ''}" required>
            </div>
            <div class="mb-3">
                <label for="apellidos" class="form-label"><strong>Apellidos:</strong></label>
                <input type="text" class="form-control" id="apellidos" name="apellidos" value="${apellidos != null && !apellidos.isEmpty() ? apellidos : ''}" required>
            </div>
            <div class="mb-3">
                <label for="domicilio" class="form-label"><strong>Dirección:</strong></label>
                <input type="text" class="form-control" id="domicilio" name="domicilio" value="${domicilio != null && !domicilio.isEmpty() ? domicilio : ''}">
            </div>
            <div class="mb-3">
                <label for="poblacion" class="form-label"><strong>Población:</strong></label>
                <input type="text" class="form-control" id="poblacion" name="poblacion" value="${poblacion != null && !poblacion.isEmpty() ? poblacion : ''}">
            </div>
            <div class="mb-3">
                <label for="provincia" class="form-label"><strong>Provincia:</strong></label>
                <input type="text" class="form-control" id="provincia" name="provincia" value="${provincia != null && !provincia.isEmpty() ? provincia : ''}">
            </div>
            <div class="mb-3">
                <label for="cp" class="form-label"><strong>CP:</strong></label>
                <input type="text" class="form-control" id="cp" name="cp" value="${cp != null && !cp.isEmpty() ? cp : ''}">
            </div>
            <div class="mb-3">
                <label for="telefono" class="form-label"><strong>Teléfono:</strong></label>
                <input type="text" class="form-control" id="telefono" name="telefono" value="${telefono != null && !telefono.isEmpty() ? telefono : ''}">
            </div>
            <div class="mb-3">
                <label for="claveActual" class="form-label"><strong>Contraseña actual:</strong></label>
                <input type="password" class="form-control" id="claveActual" name="claveActual" required>
            </div>
            <div class="mb-3">
                <label for="claveNueva" class="form-label"><strong>Nueva contraseña:</strong></label>
                <input type="password" class="form-control" id="claveNueva" name="claveNueva">
            </div>
            <div class="mb-3">
                <label for="claveNuevaConfirm" class="form-label"><strong>Confirmar nueva contraseña:</strong></label>
                <input type="password" class="form-control" id="claveNuevaConfirm" name="claveNuevaConfirm">
            </div>
            <button type="submit" class="btn btn-primary mb-3">Guardar cambios</button>
            <a href="usuario.jsp" class="btn btn-secondary mb-3">Volver</a>
        </form>
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
    </div>
    <footer>
        <div id="pie"></div>
    </footer>
    <script src="js/plantilla.js"></script>
</body>
</html>