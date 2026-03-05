package tienda;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class Login extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String clave = request.getParameter("clave");
        String url = request.getParameter("url");
        String carritoLleno = request.getParameter("carritoLleno");

        HttpSession session = request.getSession(true);
        AccesoBD con = AccesoBD.getInstance();

        int codigo = -1;

        if (usuario != null && clave != null) {
            codigo = con.comprobarUsuarioBD(usuario, clave);
            if (codigo > 0) {
                session.setAttribute("codigo", codigo);
            } else {
                session.setAttribute("mensaje", "Usuario y/o clave incorrectos");
            }
        }

        if (codigo > 0) {
            if ("true".equals(carritoLleno)) {
                url = "compra.jsp";
            } else {
                url = "usuario.html";
            }
        }

        response.sendRedirect(url);

    }
}
