<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" import="java.util.*, tienda.Producto" %>
<%
    HttpSession sesion = request.getSession(false);
    if (sesion == null || sesion.getAttribute("codigo") == null) {
        response.sendRedirect("loginUsuario.jsp");
        return;
    }

    List<Producto> carrito = (List<Producto>) sesion.getAttribute("carritoJSON");
    if (carrito == null || carrito.isEmpty()) {
        response.sendRedirect("compra.jsp");
        return;
    }

    String nombre = (String) session.getAttribute("nombre");
    String apellidos = (String) session.getAttribute("apellidos");
    String domicilio = (String) session.getAttribute("domicilio");
    String cp = (String) session.getAttribute("cp");
    String poblacion = (String) session.getAttribute("poblacion");
    String provincia = (String) session.getAttribute("provincia");
    String telefono = (String) session.getAttribute("telefono");
    String email = (String) session.getAttribute("email"); // o "usuario"

%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Resumen del pedido - EVERNEST</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<header>
    <div id="cabecera"></div>
</header>

<div class="container content">
    <h2>Resumen del pedido</h2>

    <table class="table table-striped">
        <thead>
            <tr>
                <th>Producto</th>
                <th>Precio</th>
                <th>Cantidad</th>
                <th>Total</th>
            </tr>
        </thead>
        <tbody>
            <%
                float total = 0;
                for (Producto p : carrito) {
                    float subtotal = p.getPrecio() * p.getCantidad();
                    total += subtotal;
            %>
                <tr>
                    <td><%= p.getDescripcion() %></td>
                    <td><%= p.getPrecio() %> €</td>
                    <td><%= p.getCantidad() %></td>
                    <td><%= String.format("%.2f", subtotal) %> €</td>
                </tr>
            <%
                }
            %>
            <tr>
                <th colspan="3" class="text-end">Total:</th>
                <th><%= String.format("%.2f", total) %> €</th>
            </tr>
        </tbody>
    </table>

    <hr>

    <h4>Datos de envío</h4>
    <p><strong>Nombre:</strong> <%= nombre != null ? nombre : "No disponible" %> <%= apellidos != null ? apellidos : "" %></p>
    <p><strong>Dirección:</strong> <%= domicilio != null ? domicilio : "No disponible" %>, <%= cp != null ? cp : "" %> <%= poblacion != null ? poblacion : "" %> (<%= provincia != null ? provincia : "" %>)</p>
    <p><strong>Teléfono:</strong> <%= telefono != null ? telefono : "No disponible" %></p>
    <p><strong>Email:</strong> <%= email != null ? email : "No disponible" %></p>

    <hr>

    <form method="post" action="Tramitacion.html">
        <button class="btn btn-success">Confirmar pedido</button>
        <a href="compra.jsp" class="btn btn-danger ms-2">Cancelar</a>
    </form>
</div>

<footer>
    <div id="pie"></div>
</footer>

<script src="js/plantilla.js"></script>
</body>
</html>
