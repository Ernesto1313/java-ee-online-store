package tienda;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActualizarUsuario extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("ENTRANDO EN ActualizarUsuario.java");

        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("codigo") == null) {
            System.out.println("Usuario no autenticado, redirigiendo a loginUsuario.jsp");
            sesion.setAttribute("errorMensaje", "Debes iniciar sesión para modificar tus datos");
            response.sendRedirect("loginUsuario.jsp");
            return;
        }

        int codigoUsuario = (Integer) sesion.getAttribute("codigo");

        // Obtener datos del formulario
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String domicilio = request.getParameter("domicilio");
        String poblacion = request.getParameter("poblacion");
        String provincia = request.getParameter("provincia");
        String cp = request.getParameter("cp");
        String telefono = request.getParameter("telefono");
        String claveActual = request.getParameter("claveActual");
        String claveNueva = request.getParameter("claveNueva");
        String claveNuevaConfirm = request.getParameter("claveNuevaConfirm");

        // Validar datos
        if (nombre == null || nombre.trim().isEmpty() || apellidos == null || apellidos.trim().isEmpty()) {
            sesion.setAttribute("errorMensaje", "El nombre y los apellidos son obligatorios");
            response.sendRedirect("usuario.jsp");
            return;
        }

        // Validar nueva contraseña (si se proporciona)
        if (claveNueva != null && !claveNueva.isEmpty()) {
            if (!claveNueva.equals(claveNuevaConfirm)) {
                sesion.setAttribute("errorMensaje", "Las nuevas contraseñas no coinciden");
                response.sendRedirect("usuario.jsp");
                return;
            }
        }

        AccesoBD con = AccesoBD.getInstance();
        Connection conexion = con.getConexion();
        if (conexion == null) {
            System.err.println("ERROR: Conexión a la base de datos es null");
            sesion.setAttribute("errorMensaje", "No se pudo conectar a la base de datos");
            response.sendRedirect("error.jsp");
            return;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Verificar contraseña actual
            String sqlVerificar = "SELECT clave FROM usuarios WHERE codigo = ?";
            ps = conexion.prepareStatement(sqlVerificar);
            ps.setInt(1, codigoUsuario);
            rs = ps.executeQuery();
            if (!rs.next() || !rs.getString("clave").equals(claveActual)) {
                sesion.setAttribute("errorMensaje", "La contraseña actual es incorrecta");
                response.sendRedirect("usuario.jsp");
                return;
            }
            rs.close();
            ps.close();

            // Actualizar datos
            String sqlActualizar = "UPDATE usuarios SET nombre = ?, apellidos = ?, domicilio = ?, poblacion = ?, provincia = ?, cp = ?, telefono = ?" +
                                  (claveNueva != null && !claveNueva.isEmpty() ? ", clave = ?" : "") + " WHERE codigo = ?";
            ps = conexion.prepareStatement(sqlActualizar);
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, domicilio);
            ps.setString(4, poblacion);
            ps.setString(5, provincia);
            ps.setString(6, cp);
            ps.setString(7, telefono);
            if (claveNueva != null && !claveNueva.isEmpty()) {
                ps.setString(8, claveNueva);
                ps.setInt(9, codigoUsuario);
            } else {
                ps.setInt(8, codigoUsuario);
            }

            System.out.println("Actualizando datos para usuario: " + codigoUsuario);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                // Actualizar atributos de sesión
                sesion.setAttribute("nombre", nombre);
                sesion.setAttribute("apellidos", apellidos);
                sesion.setAttribute("domicilio", domicilio);
                sesion.setAttribute("poblacion", poblacion);
                sesion.setAttribute("provincia", provincia);
                sesion.setAttribute("cp", cp);
                sesion.setAttribute("telefono", telefono);

                sesion.setAttribute("mensaje", "Datos actualizados con éxito");
            } else {
                sesion.setAttribute("errorMensaje", "No se pudieron actualizar los datos");
            }

            response.sendRedirect("usuario.jsp");

        } catch (SQLException e) {
            System.err.println("ERROR SQL en ActualizarUsuario: " + e.getMessage());
            e.printStackTrace();
            sesion.setAttribute("errorMensaje", "Error en la base de datos: " + e.getMessage());
            response.sendRedirect("error.jsp");
        } catch (Exception e) {
            System.err.println("ERROR General en ActualizarUsuario: " + e.getMessage());
            e.printStackTrace();
            sesion.setAttribute("errorMensaje", "Error inesperado: " + e.getMessage());
            response.sendRedirect("error.jsp");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Error cerrando recursos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}