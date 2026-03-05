package tienda;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class Usuario extends HttpServlet {
    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession sesion = request.getSession(false);
    if (sesion == null || sesion.getAttribute("codigo") == null) {
        response.sendRedirect("loginUsuario.jsp");
        return;
    }

    int codigo = (Integer) sesion.getAttribute("codigo");

    try {
        Connection con = AccesoBD.getInstance().getConexion();
        String query = "SELECT * FROM usuarios WHERE codigo=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, codigo);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            request.setAttribute("nombre", rs.getString("nombre"));
            request.setAttribute("apellidos", rs.getString("apellidos"));
            request.setAttribute("domicilio", rs.getString("domicilio"));
            request.setAttribute("poblacion", rs.getString("poblacion"));
            request.setAttribute("provincia", rs.getString("provincia"));
            request.setAttribute("cp", rs.getString("cp"));
            request.setAttribute("telefono", rs.getString("telefono"));
            request.setAttribute("email", rs.getString("usuario")); // suponiendo que usuario = email
            request.getRequestDispatcher("usuario.jsp").forward(request, response);
        }
        rs.close();
        ps.close();

    } catch (Exception e) {
        e.printStackTrace();
    }

    
}

}
