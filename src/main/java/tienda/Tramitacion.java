package tienda;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Tramitacion extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("ENTRANDO EN Tramitacion.java");

        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("codigo") == null || sesion.getAttribute("carritoJSON") == null) {
            System.out.println("Sesión o carrito no válido, redirigiendo a index.jsp");
            sesion.setAttribute("errorMensaje", "Debes iniciar sesión y tener un carrito válido");
            response.sendRedirect("index.jsp");
            return;
        }

        int codigoUsuario = (Integer) sesion.getAttribute("codigo");
        ArrayList<Producto> carrito = (ArrayList<Producto>) sesion.getAttribute("carritoJSON");

        float total = 0;
        Connection con = null;
        PreparedStatement psPedido = null;
        PreparedStatement psLinea = null;
        PreparedStatement psUpdateExistencias = null;
        ResultSet rs = null;

        try {
            con = AccesoBD.getInstance().getConexion();
            if (con == null) {
                System.err.println("ERROR: Conexión a la base de datos es null");
                sesion.setAttribute("errorMensaje", "No se pudo conectar a la base de datos");
                response.sendRedirect("error.jsp");
                return;
            }

            // Iniciar transacción
            con.setAutoCommit(false);
            System.out.println("Iniciando transacción para confirmar pedido");

            // Calcular el total
            for (Producto p : carrito) {
                total += p.getCantidad() * p.getPrecio();
            }
            System.out.println("Importe total del pedido: " + total);

            // Validar existencias antes de procesar
            for (Producto p : carrito) {
                int existencias = AccesoBD.getInstance().obtenerExistencias(p.getCodigo());
                if (p.getCantidad() > existencias) {
                    throw new SQLException("No hay suficientes existencias para el producto " + p.getCodigo() +
                            ". Disponible: " + existencias + ", solicitado: " + p.getCantidad());
                }
            }

            // Insertar nuevo pedido con estado inicial = 1 (pendiente)
            String sqlPedido = "INSERT INTO pedidos (persona, fecha, importe, estado) VALUES (?, CURRENT_DATE, ?, ?)";
            psPedido = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setInt(1, codigoUsuario);
            psPedido.setFloat(2, total);
            psPedido.setInt(3, 1); // estado = 1 (pendiente)
            psPedido.executeUpdate();

            int codigoPedido = -1;
            rs = psPedido.getGeneratedKeys();
            if (rs.next()) {
                codigoPedido = rs.getInt(1);
            } else {
                throw new SQLException("No se pudo obtener el código del pedido");
            }
            System.out.println("Pedido creado con código: " + codigoPedido);

            // Insertar líneas de pedido en la tabla "detalle"
            String sqlLinea = "INSERT INTO detalle (codigo_pedido, codigo_producto, unidades, precio_unitario) VALUES (?, ?, ?, ?)";
            psLinea = con.prepareStatement(sqlLinea);
            for (Producto p : carrito) {
                psLinea.setInt(1, codigoPedido);
                psLinea.setInt(2, p.getCodigo());
                psLinea.setInt(3, p.getCantidad());
                psLinea.setFloat(4, p.getPrecio());
                psLinea.executeUpdate();
                System.out.println("Insertado detalle: pedido=" + codigoPedido + ", producto=" + p.getCodigo() +
                        ", unidades=" + p.getCantidad());
            }

            // Actualizar existencias
            String sqlUpdateExistencias = "UPDATE productos SET existencias = existencias - ? WHERE codigo = ?";
            psUpdateExistencias = con.prepareStatement(sqlUpdateExistencias);
            for (Producto p : carrito) {
                psUpdateExistencias.setInt(1, p.getCantidad());
                psUpdateExistencias.setInt(2, p.getCodigo());
                int filasActualizadas = psUpdateExistencias.executeUpdate();
                if (filasActualizadas != 1) {
                    throw new SQLException("Error al actualizar existencias para producto " + p.getCodigo());
                }
                System.out.println("Actualizadas existencias: producto=" + p.getCodigo() +
                        ", restadas " + p.getCantidad() + " unidades");
            }

            // Confirmar transacción
            con.commit();
            System.out.println("Transacción confirmada: pedido " + codigoPedido + " procesado");

            // Limpiar carrito y mostrar confirmación
            sesion.removeAttribute("carritoJSON");
            request.setAttribute("codigoPedido", codigoPedido);
            request.getRequestDispatcher("pedidoFinalizado.jsp").forward(request, response);

        } catch (SQLException e) {
            System.err.println("ERROR SQL en Tramitacion: " + e.getMessage());
            e.printStackTrace();
            try {
                if (con != null) {
                    System.out.println("Deshaciendo transacción debido a error");
                    con.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error en rollback: " + rollbackEx.getMessage());
                rollbackEx.printStackTrace();
            }
            sesion.setAttribute("errorMensaje", "Error en la base de datos: " + e.getMessage());
            response.sendRedirect("error.jsp");
        } catch (Exception e) {
            System.err.println("ERROR General en Tramitacion: " + e.getMessage());
            e.printStackTrace();
            try {
                if (con != null) {
                    System.out.println("Deshaciendo transacción debido a error");
                    con.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error en rollback: " + rollbackEx.getMessage());
                rollbackEx.printStackTrace();
            }
            sesion.setAttribute("errorMensaje", "Error inesperado: " + e.getMessage());
            response.sendRedirect("error.jsp");
        } finally {
            try {
                if (rs != null) rs.close();
                if (psPedido != null) psPedido.close();
                if (psLinea != null) psLinea.close();
                if (psUpdateExistencias != null) psUpdateExistencias.close();
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error cerrando recursos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}