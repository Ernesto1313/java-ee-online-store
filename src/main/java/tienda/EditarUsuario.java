package tienda;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditarUsuario extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("ENTRANDO EN EditarUsuario.java");

        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("codigo") == null) {
            System.out.println("Usuario no autenticado, redirigiendo a loginUsuario.jsp");
            sesion.setAttribute("errorMensaje", "Debes iniciar sesión para modificar tus datos");
            response.sendRedirect("loginUsuario.jsp");
            return;
        }

        int codigo = (Integer) sesion.getAttribute("codigo");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = AccesoBD.getInstance().getConexion();
            if (con == null) {
                System.err.println("ERROR: Conexión a la base de datos es null");
                sesion.setAttribute("errorMensaje", "No se pudo conectar a la base de datos");
                response.sendRedirect("error.jsp");
                return;
            }

            String query = "SELECT nombre, apellidos, domicilio, poblacion, provincia, cp, telefono FROM usuarios WHERE codigo = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, codigo);
            rs = ps.executeQuery();

            if (rs.next()) {
                request.setAttribute("nombre", rs.getString("nombre"));
                request.setAttribute("apellidos", rs.getString("apellidos"));
                request.setAttribute("domicilio", rs.getString("domicilio"));
                request.setAttribute("poblacion", rs.getString("poblacion"));
                request.setAttribute("provincia", rs.getString("provincia"));
                request.setAttribute("cp", rs.getString("cp"));
                request.setAttribute("telefono", rs.getString("telefono"));
                System.out.println("Datos del usuario cargados para código: " + codigo);
                request.getRequestDispatcher("editarUsuario.jsp").forward(request, response);
            } else {
                System.err.println("Usuario no encontrado para código: " + codigo);
                sesion.setAttribute("errorMensaje", "Usuario no encontrado");
                response.sendRedirect("error.jsp");
            }

        } catch (SQLException e) {
            System.err.println("ERROR SQL en EditarUsuario: " + e.getMessage());
            e.printStackTrace();
            sesion.setAttribute("errorMensaje", "Error en la base de datos: " + e.getMessage());
            response.sendRedirect("error.jsp");
        } catch (Exception e) {
            System.err.println("ERROR General en EditarUsuario: " + e.getMessage());
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