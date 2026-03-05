<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registro - EVERNEST</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<header>
    <div id="cabecera"></div>
</header>

<div class="container content">
    <h2>Registrarse</h2>

    <!-- Agregamos verificación al enviar el formulario -->
    <form method="post" action="registro.html" onsubmit="return verificarPasswords()">
        <input type="hidden" name="url" value="loginUsuario.jsp">

        <div class="mb-3">
            <label for="usuario" class="form-label">Nombre de usuario</label>
            <input type="text" class="form-control" name="usuario" id="usuario" required>
        </div>

        <div class="mb-3">
            <label for="clave1" class="form-label">Contraseña</label>
            <input type="password" class="form-control" name="clave" id="clave1" required>
        </div>

        <div class="mb-3">
            <label for="clave2" class="form-label">Repite la contraseña</label>
            <input type="password" class="form-control" id="clave2" required>
        </div>

        <div class="mb-3">
            <label for="nombre" class="form-label">Nombre</label>
            <input type="text" class="form-control" name="nombre" id="nombre" required>
        </div>

        <div class="mb-3">
            <label for="apellidos" class="form-label">Apellidos</label>
            <input type="text" class="form-control" name="apellidos" id="apellidos" required>
        </div>

        <div class="mb-3">
            <label for="domicilio" class="form-label">Domicilio</label>
            <input type="text" class="form-control" name="domicilio" id="domicilio" required>
        </div>

        <div class="mb-3">
            <label for="poblacion" class="form-label">Población</label>
            <input type="text" class="form-control" name="poblacion" id="poblacion" required>
        </div>

        <div class="mb-3">
            <label for="provincia" class="form-label">Provincia</label>
            <input type="text" class="form-control" name="provincia" id="provincia" required>
        </div>

        <div class="mb-3">
            <label for="cp" class="form-label">Código Postal</label>
            <input type="text" class="form-control" name="cp" id="cp" maxlength="5" required>
        </div>

        <div class="mb-3">
            <label for="telefono" class="form-label">Teléfono</label>
            <input type="text" class="form-control" name="telefono" id="telefono" maxlength="9" required>
        </div>

        <p id="error" class="text-danger"></p>

        <button type="submit" class="btn btn-primary">Registrar</button>
    </form>

    <p>¿Ya tienes cuenta? <a href="loginUsuario.jsp">Inicia sesión aquí</a></p>
</div>

<footer>
    <div id="pie"></div>
</footer>

<script src="js/plantilla.js"></script>
<script>
function verificarPasswords() {
    const clave1 = document.getElementById("clave1").value;
    const clave2 = document.getElementById("clave2").value;
    const error = document.getElementById("error");

    if (clave1 !== clave2) {
        error.textContent = "Las contraseñas no coinciden.";
        return false;
    }
    error.textContent = "";
    return true;
}
</script>

</body>
</html>
