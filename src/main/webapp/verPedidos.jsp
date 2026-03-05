<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.SimpleDateFormat" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mis Pedidos - EVERNEST</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header>
        <div id="cabecera"></div>
    </header>

    <div class="container content">
        <h2>Mis Pedidos</h2>
        <% if (request.getAttribute("mensajeExito") != null) { %>
            <div class="alert alert-success" role="alert">
                <%= request.getAttribute("mensajeExito") %>
            </div>
        <% } %>
        <% if (request.getAttribute("errorMensaje") != null) { %>
            <div class="alert alert-danger" role="alert">
                <%= request.getAttribute("errorMensaje") %>
            </div>
        <% } %>
        <%
            List<Map<String, Object>> pedidos = (List<Map<String, Object>>) request.getAttribute("pedidos");
            if (pedidos == null || pedidos.isEmpty()) {
                out.println("<p>No tienes pedidos registrados.</p>");
            } else {
        %>
        <div class="row">
            <% for (Map<String, Object> pedido : pedidos) {
                int estado = (Integer) pedido.get("estado");
                String textoEstado;
                switch (estado) {
                    case 1:
                        textoEstado = "Pendiente";
                        break;
                    case 2:
                        textoEstado = "Enviado";
                        break;
                    case 3:
                        textoEstado = "Cancelado";
                        break;
                    default:
                        textoEstado = "Desconocido";
                        break;
                }
            %>
            <div class="col-md-4">
                <div class="card mb-3">
                    <div class="card-header">
                        Pedido #<%= pedido.get("codigo") %>
                    </div>
                    <div class="card-body">
                        <p><strong>Fecha:</strong> <%= new SimpleDateFormat("dd/MM/yyyy").format(pedido.get("fecha")) %></p>
                        <p><strong>Importe:</strong> <%= String.format("%.2f", pedido.get("importe")) %> €</p>
                        <h5>Detalles:</h5>
                        <ul>
                            <% 
                                List<Map<String, Object>> detalles = (List<Map<String, Object>>) pedido.get("detalles");
                                for (Map<String, Object> detalle : detalles) {
                            %>
                            <li>
                                <%= detalle.get("descripcion") %> (x<%= detalle.get("unidades") %>) - 
                                <%= String.format("%.2f", detalle.get("precio_unitario")) %> €
                            </li>
                            <% } %>
                        </ul>
                    </div>
                    <div class="card-footer d-flex justify-content-between align-items-center">
                        <span>Estado: <%= textoEstado %></span>
                        <% if (estado == 1) { %>
                            <form action="CancelarPedido" method="post">
                                <input type="hidden" name="codigoPedido" value="<%= pedido.get("codigo") %>">
                                <button type="submit" class="btn btn-danger btn-sm">Cancelar Pedido</button>
                            </form>
                        <% } %>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
        <% } %>
        <a href="usuario.jsp" class="btn btn-secondary mt-3">Volver</a>
    </div>

    <footer>
        <div id="pie"></div>
    </footer>

    <script src="js/plantilla.js"></script>
</body>
</html>