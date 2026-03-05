<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.List,tienda.*" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Productos - EVERNEST</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<header>
    <div id="cabecera"></div>
</header>

<div class="container content">
    <h2>Productos disponibles en EVERNEST</h2>
    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-5">
        <%
            int categoria = 1;
            AccesoBD con = AccesoBD.getInstance();
            List<ProductoBD> productos = con.obtenerProductosBD(categoria);

            for (ProductoBD producto : productos) {
                int codigo = producto.getCodigo();
                String descripcion = producto.getDescripcion();
                float precio = producto.getPrecio();
                int existencias = producto.getExistencias();
                String imagen = producto.getImagen();

                // Escapar manualmente para usar en el onclick
                String descripcionJS = descripcion.replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"");
        %>
        <div class="col mb-4">
            <div class="card">
                <img src="pics/<%=imagen%>" class="card-img-top" alt="<%=descripcion%>">
                <div class="card-body">
                    <h5 class="card-title"><%=descripcion%></h5>
                    <p><strong>Precio: <%=precio%>€</strong></p>
                    <% if (existencias > 0) { %>
                        <button id="boton-prod-<%=codigo%>" class="btn btn-primary"
                            onclick="anadirCarrito(<%=codigo%>, '<%=descripcionJS%>', 'pics/<%=imagen%>', <%=precio%>, <%=existencias%>)">
                            Añadir al carrito
                        </button>
                    <% } else { %>
                        <button class="btn btn-danger" disabled>
                            Sin existencias
                        </button>
                    <% } %>
                </div>
            </div>
        </div>
        <%
            }
        %>
    </div>
</div>

<footer class="footer">
    <div id="pie"></div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/plantilla.js"></script>
<script src="js/carrito.js"></script>

<!-- Comprobación dinámica para desactivar botón si no quedan existencias -->
<script>
document.addEventListener("DOMContentLoaded", () => {
    const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");

    carrito.forEach(p => {
        if (p.cantidad >= p.existencias) {
            const boton = document.getElementById(`boton-prod-${p.id}`);
            if (boton) {
                boton.classList.remove("btn-primary");
                boton.classList.add("btn-danger");
                boton.disabled = true;
                boton.textContent = "Sin existencias";
            }
        }
    });
});
</script>

</body>
</html>
