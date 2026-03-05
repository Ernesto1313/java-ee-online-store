package tienda;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.json.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class ProcesarPedido extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("SE EJECUTA ProcesarPedido");

        HttpSession sesion = request.getSession(true);
        AccesoBD con = AccesoBD.getInstance();
        ArrayList<Producto> carritoJSON = new ArrayList<>();

        try (JsonReader lector = Json.createReader(new InputStreamReader(request.getInputStream(), "utf-8"))) {
            JsonArray arrayProductos = lector.readArray();

            for (JsonObject obj : arrayProductos.getValuesAs(JsonObject.class)) {
                int codigoProducto = obj.getInt("codigo", -1);
                String descripcion = obj.getString("descripcion", "");
                String imagen = obj.getString("imagen", "");
                float precio = Float.parseFloat(obj.get("precio").toString());
                int cantidadSolicitada = obj.getInt("cantidad", 0);

                if (codigoProducto <= 0 || cantidadSolicitada <= 0) continue;

                int existencias = con.obtenerExistencias(codigoProducto);
                if (cantidadSolicitada > existencias) continue;

                Producto p = new Producto();
                p.setCodigo(codigoProducto);
                p.setDescripcion(descripcion);
                p.setImagen(imagen);
                p.setPrecio(precio);
                p.setCantidad(cantidadSolicitada);

                carritoJSON.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (carritoJSON.isEmpty()) {
            response.sendRedirect("compra.jsp"); // ❌ No había productos válidos
            return;
        }

        sesion.setAttribute("carritoJSON", carritoJSON);

        // Datos del usuario
        try {
            int codigo = (Integer) sesion.getAttribute("codigo");
            Connection conexion = con.getConexion();
            String sql = "SELECT * FROM usuarios WHERE codigo=?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                sesion.setAttribute("nombre", rs.getString("nombre"));
                sesion.setAttribute("apellidos", rs.getString("apellidos"));
                sesion.setAttribute("domicilio", rs.getString("domicilio"));
                sesion.setAttribute("poblacion", rs.getString("poblacion"));
                sesion.setAttribute("provincia", rs.getString("provincia"));
                sesion.setAttribute("cp", rs.getString("cp"));
                sesion.setAttribute("telefono", rs.getString("telefono"));
                sesion.setAttribute("email", rs.getString("usuario"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestDispatcher rd = request.getRequestDispatcher("resguardo.jsp");
        rd.forward(request, response);
    }
}
