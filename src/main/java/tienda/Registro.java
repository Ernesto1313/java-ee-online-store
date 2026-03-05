package tienda;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class Registro extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Recoger datos del formulario
        String usuario = request.getParameter("usuario");
        String clave = request.getParameter("clave");
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String domicilio = request.getParameter("domicilio");
        String poblacion = request.getParameter("poblacion");
        String provincia = request.getParameter("provincia");
        String cp = request.getParameter("cp");
        String telefono = request.getParameter("telefono");

        String url = request.getParameter("url"); // loginUsuario.jsp u otra

        AccesoBD con = AccesoBD.getInstance();

        try {
            con.abrirConexionBD();
            Connection conexion = con.getConexion();

            String sql = "INSERT INTO usuarios (usuario, clave, nombre, apellidos, domicilio, poblacion, provincia, cp, telefono, activo, admin) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1, 0)";

            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, clave);
            stmt.setString(3, nombre);
            stmt.setString(4, apellidos);
            stmt.setString(5, domicilio);
            stmt.setString(6, poblacion);
            stmt.setString(7, provincia);
            stmt.setString(8, cp);
            stmt.setString(9, telefono); 

            int filas = stmt.executeUpdate();

            if (filas > 0) {
                // Registro correcto: redirigir al login
                response.sendRedirect(url != null ? url : "loginUsuario.jsp");
            } else {
                // Error al insertar
                request.getSession().setAttribute("mensaje", "No se pudo registrar el usuario.");
                response.sendRedirect("registro.jsp");
            }

        } catch (Exception e) {
            System.err.println("Error en el registro: " + e.getMessage());
            request.getSession().setAttribute("mensaje", "Error en el registro: " + e.getMessage());
            response.sendRedirect("registro.jsp");
        }
    }
}
