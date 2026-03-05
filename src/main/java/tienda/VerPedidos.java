package tienda;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerPedidos extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("ENTRANDO EN VerPedidos.java");

        HttpSession sesion = request.getSession();
        Integer codigoUsuario = (Integer) sesion.getAttribute("codigo");

        // Depuración: Verificar código de usuario
        System.out.println("CODIGO USUARIO EN SESIÓN: " + codigoUsuario);
        if (codigoUsuario == null || codigoUsuario <= 0) {
            System.out.println("Usuario no autenticado, redirigiendo a loginUsuario.jsp");
            response.sendRedirect("loginUsuario.jsp");
            return;
        }

        // Manejo de mensajes de éxito o error
        String mensajeExito = (String) sesion.getAttribute("mensajeExito");
        String errorMensaje = (String) sesion.getAttribute("errorMensaje");
        if (mensajeExito != null) {
            request.setAttribute("mensajeExito", mensajeExito);
            sesion.removeAttribute("mensajeExito");
        }
        if (errorMensaje != null) {
            request.setAttribute("errorMensaje", errorMensaje);
            sesion.removeAttribute("errorMensaje");
        }

        AccesoBD con = AccesoBD.getInstance();
        Connection conexion = con.getConexion();

        // Depuración: Verificar conexión
        if (conexion == null) {
            System.err.println("ERROR: Conexión a la base de datos es null");
            sesion.setAttribute("errorMensaje", "No se pudo conectar a la base de datos");
            response.sendRedirect("error.jsp");
            return;
        }
        System.out.println("Conexión a la base de datos establecida");

        PreparedStatement psPedidos = null;
        ResultSet rsPedidos = null;
        PreparedStatement psDetalles = null;
        ResultSet rsDetalles = null;

        try {
            // Consulta de pedidos del usuario
            String sqlPedidos = "SELECT * FROM pedidos WHERE persona = ? ORDER BY fecha DESC";
            psPedidos = conexion.prepareStatement(sqlPedidos);
            psPedidos.setInt(1, codigoUsuario);
            System.out.println("Ejecutando consulta de pedidos: " + sqlPedidos + " con persona = " + codigoUsuario);
            rsPedidos = psPedidos.executeQuery();

            List<Map<String, Object>> pedidos = new ArrayList<>();
            int pedidoCount = 0;

            while (rsPedidos.next()) {
                pedidoCount++;
                int codigoPedido = rsPedidos.getInt("codigo");
                float importe = rsPedidos.getFloat("importe");
                java.sql.Date fecha = rsPedidos.getDate("fecha"); // Especificar java.sql.Date
                int estado = rsPedidos.getInt("estado");

                System.out.println("Procesando pedido #" + codigoPedido + ", importe: " + importe + ", fecha: " + fecha);

                // Consulta de detalles del pedido con información del producto
                String sqlDetalles = "SELECT d.*, p.descripcion, p.imagen FROM detalle d " +
                                    "JOIN productos p ON d.codigo_producto = p.codigo " +
                                    "WHERE d.codigo_pedido = ?";
                psDetalles = conexion.prepareStatement(sqlDetalles);
                psDetalles.setInt(1, codigoPedido);
                System.out.println("Ejecutando consulta de detalles para pedido #" + codigoPedido);
                rsDetalles = psDetalles.executeQuery();

                List<Map<String, Object>> detalles = new ArrayList<>();
                int detalleCount = 0;

                while (rsDetalles.next()) {
                    detalleCount++;
                    Map<String, Object> detalle = new HashMap<>();
                    detalle.put("codigo_producto", rsDetalles.getInt("codigo_producto"));
                    detalle.put("descripcion", rsDetalles.getString("descripcion"));
                    detalle.put("imagen", rsDetalles.getString("imagen"));
                    detalle.put("unidades", rsDetalles.getInt("unidades"));
                    detalle.put("precio_unitario", rsDetalles.getFloat("precio_unitario"));
                    detalles.add(detalle);
                    System.out.println("Añadido detalle #" + detalleCount + " para pedido #" + codigoPedido);
                }

                Map<String, Object> pedido = new HashMap<>();
                pedido.put("codigo", codigoPedido);
                pedido.put("fecha", fecha);
                pedido.put("importe", importe);
                pedido.put("estado", estado);
                pedido.put("detalles", detalles);

                pedidos.add(pedido);
                System.out.println("Añadido pedido #" + codigoPedido + " con " + detalleCount + " detalles");

                // Cerrar ResultSet y PreparedStatement de detalles
                rsDetalles.close();
                psDetalles.close();
            }

            System.out.println("Total de pedidos recuperados: " + pedidoCount);
            request.setAttribute("pedidos", pedidos);

            // Depuración: Verificar si verPedidos.jsp existe
            System.out.println("Redirigiendo a verPedidos.jsp con " + pedidos.size() + " pedidos");
            RequestDispatcher rd = request.getRequestDispatcher("verPedidos.jsp");
            rd.forward(request, response);

        } catch (SQLException e) {
            System.err.println("ERROR SQL en VerPedidos: " + e.getMessage());
            e.printStackTrace();
            sesion.setAttribute("errorMensaje", "Error en la base de datos: " + e.getMessage());
            response.sendRedirect("error.jsp");
        } catch (Exception e) {
            System.err.println("ERROR General en VerPedidos: " + e.getMessage());
            e.printStackTrace();
            sesion.setAttribute("errorMensaje", "Error inesperado: " + e.getMessage());
            response.sendRedirect("error.jsp");
        } finally {
            // Cerrar recursos en bloque finally
            try {
                if (rsDetalles != null) rsDetalles.close();
                if (psDetalles != null) psDetalles.close();
                if (rsPedidos != null) rsPedidos.close();
                if (psPedidos != null) psPedidos.close();
            } catch (SQLException e) {
                System.err.println("Error cerrando recursos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}