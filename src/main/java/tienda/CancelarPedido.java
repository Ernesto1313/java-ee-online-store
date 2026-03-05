package tienda;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class CancelarPedido extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("ENTRANDO EN CancelarPedido.java");

        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("codigo") == null) {
            System.out.println("Usuario no autenticado, redirigiendo a loginUsuario.jsp");
            sesion.setAttribute("errorMensaje", "Debes iniciar sesión para cancelar un pedido");
            response.sendRedirect("loginUsuario.jsp");
            return;
        }

        int codigoUsuario = (Integer) sesion.getAttribute("codigo");
        int codigoPedido;
        try {
            codigoPedido = Integer.parseInt(request.getParameter("codigoPedido"));
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Código de pedido inválido");
            sesion.setAttribute("errorMensaje", "Código de pedido inválido");
            response.sendRedirect("error.jsp");
            return;
        }

        Connection con = null;
        PreparedStatement psCheckEstado = null;
        PreparedStatement psUpdateEstado = null;
        PreparedStatement psGetDetalles = null;
        PreparedStatement psUpdateExistencias = null;
        ResultSet rsCheckEstado = null;
        ResultSet rsDetalles = null;

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
            System.out.println("Iniciando transacción para cancelar pedido #" + codigoPedido);

            // Verificar que el pedido pertenece al usuario y está en estado pendiente
            String sqlCheckEstado = "SELECT estado FROM pedidos WHERE codigo = ? AND persona = ?";
            psCheckEstado = con.prepareStatement(sqlCheckEstado);
            psCheckEstado.setInt(1, codigoPedido);
            psCheckEstado.setInt(2, codigoUsuario);
            rsCheckEstado = psCheckEstado.executeQuery();

            if (!rsCheckEstado.next()) {
                throw new SQLException("El pedido no existe o no pertenece al usuario");
            }

            int estado = rsCheckEstado.getInt("estado");
            if (estado != 1) {
                throw new SQLException("El pedido no puede ser cancelado porque no está en estado pendiente");
            }

            // Obtener detalles del pedido
            String sqlDetalles = "SELECT codigo_producto, unidades FROM detalle WHERE codigo_pedido = ?";
            psGetDetalles = con.prepareStatement(sqlDetalles);
            psGetDetalles.setInt(1, codigoPedido);
            rsDetalles = psGetDetalles.executeQuery();

            // Actualizar existencias de los productos
            String sqlUpdateExistencias = "UPDATE productos SET existencias = existencias + ? WHERE codigo = ?";
            psUpdateExistencias = con.prepareStatement(sqlUpdateExistencias);

            while (rsDetalles.next()) {
                int codigoProducto = rsDetalles.getInt("codigo_producto");
                int unidades = rsDetalles.getInt("unidades");
                psUpdateExistencias.setInt(1, unidades);
                psUpdateExistencias.setInt(2, codigoProducto);
                int filasActualizadas = psUpdateExistencias.executeUpdate();
                if (filasActualizadas != 1) {
                    throw new SQLException("Error al restaurar existencias para producto " + codigoProducto);
                }
                System.out.println("Restauradas " + unidades + " unidades para producto #" + codigoProducto);
            }

            // Actualizar el estado del pedido a 3 (cancelado)
            String sqlUpdateEstado = "UPDATE pedidos SET estado = 3 WHERE codigo = ?";
            psUpdateEstado = con.prepareStatement(sqlUpdateEstado);
            psUpdateEstado.setInt(1, codigoPedido);
            int filasActualizadas = psUpdateEstado.executeUpdate();
            if (filasActualizadas != 1) {
                throw new SQLException("Error al actualizar el estado del pedido");
            }
            System.out.println("Estado del pedido #" + codigoPedido + " actualizado a cancelado");

            // Confirmar transacción
            con.commit();
            System.out.println("Transacción confirmada: pedido #" + codigoPedido + " cancelado");

            // Redirigir a la lista de pedidos con un mensaje de éxito
            sesion.setAttribute("mensajeExito", "El pedido #" + codigoPedido + " ha sido cancelado correctamente");
            response.sendRedirect(request.getContextPath() + "/verPedidos.jsp");

        } catch (SQLException e) {
            System.err.println("ERROR SQL en CancelarPedido: " + e.getMessage());
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
            System.err.println("ERROR General en CancelarPedido: " + e.getMessage());
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
                if (rsCheckEstado != null) rsCheckEstado.close();
                if (rsDetalles != null) rsDetalles.close();
                if (psCheckEstado != null) psCheckEstado.close();
                if (psGetDetalles != null) psGetDetalles.close();
                if (psUpdateExistencias != null) psUpdateExistencias.close();
                if (psUpdateEstado != null) psUpdateEstado.close();
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error cerrando recursos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}